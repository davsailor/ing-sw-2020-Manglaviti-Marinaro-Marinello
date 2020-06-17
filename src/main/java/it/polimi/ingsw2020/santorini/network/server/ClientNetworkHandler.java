package it.polimi.ingsw2020.santorini.network.server;

import it.polimi.ingsw2020.santorini.controller.GameLogic;
import it.polimi.ingsw2020.santorini.exceptions.EndMatchException;
import it.polimi.ingsw2020.santorini.model.Match;
import it.polimi.ingsw2020.santorini.network.NetworkInterface;
import it.polimi.ingsw2020.santorini.utils.FirstHeaderType;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PlayerStatus;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.MatchSetupMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.MatchStateMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientNetworkHandler extends Thread implements NetworkInterface {

    private Socket client;
    private Server server;
    private String username;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ObjectInputStream pingIn;
    private ObjectOutputStream pingOut;
    private final ArrayList<Message> messageQueue;
    private boolean connected;

    /**
     * constructor of the class
     * @param client is the socket of the client that has to be handled
     */
    public ClientNetworkHandler(Socket client, Server server){
        this.server = server;
        this.client = client;
        ClientHandler clientHandler = new ClientHandler(this);
        clientHandler.start();
        this.messageQueue = new ArrayList<>();
        try {
            this.input = new ObjectInputStream(this.client.getInputStream());
            this.output = new ObjectOutputStream(this.client.getOutputStream());
            connected = true;
        } catch (IOException e) {
            System.out.println("error IO");
            connected = false;
        }
    }

    /*
     * getters and setters
     */
    public Socket getClient() {
        return client;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Server getServer() {
        return server;
    }

    synchronized public void setConnected(boolean status){
        this.connected = status;
    }

    /**
     * synchronized method to add a message from the queue
     * @param message the message to add
     */
    synchronized public void addMessageQueue(Message message) throws InterruptedException {
        messageQueue.add(message);
    }

    /**
     * synchronized method that gets the next message of the queue
     * @return the next message of the queue, null if the queue is empty
     */
    synchronized public Message getNextMessage() {
        if(messageQueue.isEmpty()) return null;
        else {
            Message message = messageQueue.get(0);
            messageQueue.remove(message);
            return message;
        }
    }

    /**
     * synchronized method that inspects the queue of messages
     * @return false if the queue is empty, true otherwise
     */
    synchronized public boolean hasNextMessage(){
        return !messageQueue.isEmpty();
    }

    /**
     * method that performs all the required actions to send a message to the client
     * @param message is the message that has to be sent
     */
    public void send(Message message) {
        try{
            output.reset();
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            System.out.println("server cannot send message to: " + message.getUsername());
        }
    }

    /**
     * method used to add a new message from the client to the queue
     * @param message the received message
     */
    @Override
    public void receive(Message message) throws InterruptedException {
        addMessageQueue(message);
    }

    /**
     * method used to check the connection server-side: it ping the client every SO_TIMEOUT / 4 milliseconds
     */
    @Override
    public void checkConnection() {
        try {
            while (connected) {
                Message ping = new Message(null);
                ping.buildPingMessage();
                send(ping);
                Thread.sleep(Server.SO_TIMEOUT / 4);
            }
        } catch (InterruptedException e) {
            setConnected(false);
        }
    }

    /**
     * the main thread of the client network handler of the server, associated to a specific client
     * it listens to the client and reads all messages coming from the client
     * then it adds the to the queue using receive method
     * if the connection fails, it performs all the action required to remove the client from
     * the list of virtual clients and terminates the connection
     * @see Thread#run()
     */
    @Override
    public void run(){
        Thread checkConnection = new Thread(this::checkConnection);
        checkConnection.start();
        while(connected){
            try {
                Message message = (Message) input.readObject();
                if(message.getFirstLevelHeader() != FirstHeaderType.PING)
                    receive(message);
            } catch (ClassNotFoundException | IOException | InterruptedException e) {
                System.out.println("connection error");
                setConnected(false);
            }
        }
        if(server.getVirtualClients().containsKey(username)) {
            server.getVirtualClients().remove(username);
            if(server.getPlayerInMatch().containsKey(username)){
                int matchID = server.getMatchFromUsername(username);
                Match match = server.getViewFromMatch(matchID).getMatch();
                int i;
                for (i = 0; i < match.getPlayers().length; ++i)
                    if (username.equals(match.getPlayers()[i].getNickname())) break;
                try {
                    GameLogic controller = server.getControllers().get(match.getMatchID());
                    if(controller.getTurnManager().getPhase() != null) {
                        if(i < match.getPlayers().length)
                            match.setEliminatedPlayer(i);
                        controller.getTurnManager().setStartTurn();
                        controller.getTurnManager().handlePhases(match);
                    } else {
                        if (username.equals(match.getCurrentPlayer().getNickname())) {
                            if(match.getCurrentPlayer().getDivinePower() != null) {
                                if (i < match.getPlayers().length)
                                    match.setEliminatedPlayer(i);
                                ArrayList<Message> orderMessage = new ArrayList<>();
                                for (int k = 0; k < match.getPlayers().length; ++k) {
                                    orderMessage.add(new Message(match.getPlayers()[k].getNickname()));
                                    orderMessage.get(k).buildTurnPlayerMessage(new MatchStateMessage(match.getPlayers()[match.getCurrentPlayerIndex()], match.getBoard().getBoard(), match.getPlayersAsList()));
                                }
                                match.notifyView(orderMessage);
                            } else {
                                if(match.getRemainingGods() == null){
                                    if (i < match.getPlayers().length)
                                        match.setEliminatedPlayer(i);
                                    match.setCurrentPlayerIndex(0);
                                    ArrayList<Message> orderMessage = new ArrayList<>();
                                    for (int k = 0; k < match.getPlayers().length; ++k) {
                                        match.getPlayers()[k].setStatus(PlayerStatus.WAITING);
                                        orderMessage.add(new Message(match.getPlayers()[k].getNickname()));
                                        orderMessage.get(k).buildMatchSetupMessage(new MatchSetupMessage(match, null));
                                    }
                                    match.notifyView(orderMessage);
                                } else {
                                    if (i < match.getPlayers().length)
                                        match.setEliminatedPlayer(i);
                                    ArrayList<Message> orderMessage = new ArrayList<>();
                                    for (int k = 0; k < match.getPlayers().length; ++k) {
                                        orderMessage.add(new Message(match.getPlayers()[k].getNickname()));
                                        orderMessage.get(k).buildGodInvocationMessage(new MatchSetupMessage(match, match.getRemainingGods()));
                                    }
                                    match.notifyView(orderMessage);
                                }
                            }
                        } else
                            if(i < match.getPlayers().length)
                                match.setEliminatedPlayer(i);
                    }
                } catch (EndMatchException e) {
                    match.notifyEndMatch(server);
                }
            } else {
                server.removeWaitingPlayers(username);
            }
        }
    }
}

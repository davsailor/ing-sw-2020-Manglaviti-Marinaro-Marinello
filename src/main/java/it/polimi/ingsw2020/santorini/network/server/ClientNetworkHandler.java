package it.polimi.ingsw2020.santorini.network.server;

import it.polimi.ingsw2020.santorini.controller.GameLogic;
import it.polimi.ingsw2020.santorini.exceptions.EndMatchException;
import it.polimi.ingsw2020.santorini.model.Match;
import it.polimi.ingsw2020.santorini.network.NetworkInterface;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.MatchStateMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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
    private final Object connectedLock;

    // magari da aggiungere un metodo per gestire la disconnessione

    /**
     * constructor of the class
     * @param client is the socket of the client that has to be handled
     */
    public ClientNetworkHandler(Socket client, Server server){
        this.server = server;
        this.client = client;
        ClientHandler clientHandler = new ClientHandler(this);
        clientHandler.start();
        connectedLock = new Object();
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

    public Socket getClient() {
        return client;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void send(Message message) {
        try{
            output.reset();
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            System.out.println("server cannot send message to: " + message.getUsername());
        }
    }

    @Override
    public void receive(Message message) {
        addMessageQueue(message);
    }

    @Override
    public void checkConnection(Socket client) {
        try {
            pingIn = new ObjectInputStream(client.getInputStream());
            pingOut = new ObjectOutputStream(client.getOutputStream());
            Message ping = new Message(null);
            while (connected) {
                pingOut.reset();
                pingOut.writeObject(ping);
                pingOut.flush();
                pingIn.readObject();
                Thread.sleep(Server.SO_TIMEOUT / 4);
            }
        } catch (SocketTimeoutException ex){
            System.out.println("timeout exception!");
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            //e.printStackTrace();
            //TODO: DOMANDA PER DANIELE -> COME POSSIAMO INTERROMPERE UN THREAD QUANDO CONNECTED DIVENTA FALSE?
            synchronized (connectedLock) {
                connected = false;
            }
        }
    }

    @Override
    public void run(){
        while(connected){
            try {
                Message message = (Message) input.readObject();
                receive(message);
            } catch (ClassNotFoundException | IOException e) {
                System.out.println("connection error");
                synchronized (connectedLock){
                    connected = false;
                }
            }
        }
        // gestire la disconnessione
        if(server.getVirtualClients().containsKey(username)) {
            server.getVirtualClients().remove(username);
            if(server.getPlayerInMatch().containsKey(username)){
                // finisce la partita per quelli da 3
                // si creano tutti i messaggi di tutti i giocatori
                // se il virtual client c'è il messaggio si invia, altrimenti non si invia
                int matchID = server.getMatchFromUsername(username);
                Match match = server.getViewFromMatch(matchID).getMatch();
                int i;
                for (i = 0; i < match.getNumberOfPlayers(); ++i)
                    if (username.equals(match.getPlayers()[i].getNickname())) break;
                try {
                    if(i < match.getPlayers().length)
                        match.setEliminatedPlayer(i);
                    GameLogic controller = server.getControllers().get(match.getMatchID());
                    if(controller.getTurnManager().getPhase() != null) {
                        controller.getTurnManager().setStartTurn();
                        controller.getTurnManager().handlePhases(match);
                    } else {
                        ArrayList<Message> orderMessage = new ArrayList<>();
                        for (int k = 0; k < match.getPlayers().length; ++k) {
                            orderMessage.add(new Message(match.getPlayers()[k].getNickname()));
                            orderMessage.get(i).buildTurnPlayerMessage(new MatchStateMessage(match.getPlayers()[match.getCurrentPlayerIndex()], match.getBoard().getBoard()));
                        }
                        match.notifyView(orderMessage);
                    }
                } catch (EndMatchException e) {
                    match.notifyEndMatch(server);
                }
            } else {
                server.removeWaitingPlayers(username);
            }

        }
    }

    public Server getServer() {
        return server;
    }

    synchronized public void removeMessageQueue(Message message){
        messageQueue.remove(message);
    }

    synchronized public void addMessageQueue(Message message) {
        messageQueue.add(message);
    }

    synchronized public boolean hasNextMessage(){
        return !messageQueue.isEmpty();
    }

    synchronized public Message getNextMessage(){
        if(messageQueue.isEmpty()) return null;
        return messageQueue.get(0);
    }
}

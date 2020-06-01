package it.polimi.ingsw2020.santorini.network.server;

import it.polimi.ingsw2020.santorini.controller.GameLogic;
import it.polimi.ingsw2020.santorini.model.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Set;

public class Server {

    public final static int PORT = 9999;
    private ServerSocket socket;
    public final static int SO_TIMEOUT = 8000;

    private int matchIDGen;
    private final HashMap<Player, Integer> waitingPlayers;
    private final HashMap<String, ClientNetworkHandler> virtualClients;
    private final HashMap<Integer, GameLogic> controllers;
    private final HashMap<Integer, VirtualView> virtualViews;
    private final HashMap<String, Integer> playerInMatch;

    /**
     * constructor of the class
     */
    public Server(){
        matchIDGen = 0;
        waitingPlayers = new HashMap<>();
        virtualClients = new HashMap<>();
        playerInMatch = new HashMap<>();
        try {
            socket = new ServerSocket(PORT);
            System.out.println("server ready to receive");
        } catch (IOException e) {
            System.out.println("cannot use server port");
            System.exit(10);
        }
        controllers = new HashMap<>();
        virtualViews = new HashMap<>();
    }

    /**
     * main thread of the class
     * @param args no args needed
     */
    public static void main(String[] args) {
        Server server = new Server();
        while(true) {
            try {
                Socket client = server.socket.accept();
                client.setSoTimeout(SO_TIMEOUT);
                ClientNetworkHandler clientNetworkHandler = new ClientNetworkHandler(client, server);
                clientNetworkHandler.start();
            } catch (IOException e) {
                System.out.println("socket connection failed!");
            }
        }
    }

    /*
     * getters and setters of the class
     */
    public ServerSocket getSocket() {
        return socket;
    }

    public HashMap<String, ClientNetworkHandler> getVirtualClients() {
        return virtualClients;
    }

    public HashMap<Integer, GameLogic> getControllers() {
        return controllers;
    }

    public HashMap<Integer, VirtualView> getVirtualViews() {
        return virtualViews;
    }

    public HashMap<String, Integer> getPlayerInMatch() {
        return playerInMatch;
    }

    public HashMap<Player, Integer> getWaitingPlayers() {
        return waitingPlayers;
    }

    /**
     * synchronized method that checks if a new match can be crated, looking at the waiting players queue
     * @param numberOfPlayers the number of players of the match we aro looking for
     */
    synchronized public void checkForMatches(int numberOfPlayers){
        if(waitingPlayers.values().stream()
                .filter(x -> x == numberOfPlayers)
                .count() >= numberOfPlayers) {
         //   synchronized (controllers) {
                controllers.put(matchIDGen, new GameLogic(this));
                virtualViews.put(matchIDGen, new VirtualView(controllers.get(matchIDGen)));
         //   }
            controllers.get(matchIDGen).initializeMatch(virtualViews.get(matchIDGen), numberOfPlayers);
        }
    }

    /**
     * synchronized method that inspects the queue of waiting players and filters it using the number of players
     * @param numberOfPlayers the number of players we want to filter the queue
     * @return a list containing all players that wants to play a match with two players
     */
    synchronized public ArrayList<Player> getWaitingPlayers(int numberOfPlayers) {
        ArrayList<Player> list = new ArrayList<>();
        for(Player p : waitingPlayers.keySet())
            if(waitingPlayers.get(p) == numberOfPlayers) list.add(p);
        return list;
    }

    /**
     * synchronized method that adds a new connected player to the waiting players queue
     * @param player the new connected player
     * @param numberOfPlayers the number that defines what match he wants to play
     */
    synchronized public void addWaitingPlayers(Player player, Integer numberOfPlayers) {
        waitingPlayers.put(player, numberOfPlayers);
    }

    /**
     * synchronized method that removes a player from the waiting players list
     * @param player the player that has to be removed
     */
    synchronized public void removeWaitingPlayers(Player player) {
        waitingPlayers.remove(player);
    }

    /**
     * overloaded version of the previous method
     * synchronized method that removes a player from the waiting players list finding him using the username
     * @param username the username of player that has to be removed
     */
    synchronized public void removeWaitingPlayers(String username){
        Set<Player> player = waitingPlayers.keySet();
        for(Player p : player)
            if (p.getNickname().equals(username)) {
                waitingPlayers.remove(p);
                break;
            }
    }

    /**
     * synchronized method that adds a new connected client to the virtual clients map
     * @param username the username of the client connected
     * @param handler the network handler associated to that client, used to communicate with the client
     */
    synchronized public void addVirtualClient(String username, ClientNetworkHandler handler){
        this.virtualClients.put(username, handler);
        System.out.println("il client appena connesso si chiama: " + username + "\n" + "client in attesa di fare partite");
    }

    /**
     * synchronized method that removes a disconnected client from the virtual clients map
     * @param username the usernamen of the disconnected client
     */
    synchronized public void removeVirtualClient(String username){
        virtualClients.remove(username);
    }

    /**
     * synchronized method that adds a new player that has joined a match to the list
     * @param username the username of the player
     * @param id the id of the match the player joined
     */
    synchronized public void addPlayerInMatch(String username, Integer id){
        playerInMatch.put(username, id);
    }

    /**
     * synchronized method that finds the matchID of the match the player is playing
     * @param username the username of that player
     * @return the matchID the player is playing
     */
    synchronized public int getMatchFromUsername(String username){
        return playerInMatch.get(username);
    }

    /**
     * synchronized method that finds the virtualView associated to a certain match
     * @param id the matchID of the match we want to know its virtualView
     * @return the virtualView associated to that match
     */
    synchronized public VirtualView getViewFromMatch(Integer id){
        return virtualViews.get(id);
    }

    /**
     * generator of matchID that are unique for the server
     * @return the generated matchID
     */
    public int generateMatchID(){
        int tempId = matchIDGen;
        ++matchIDGen;
        return tempId;
    }

    /**
     * test only, closes the connection
     */
    public void close(){
        try{
            socket.close();
        } catch (IOException e){
            System.out.println("cannot close server port");
        }
    }
}

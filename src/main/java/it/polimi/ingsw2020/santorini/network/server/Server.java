package it.polimi.ingsw2020.santorini.network.server;

import it.polimi.ingsw2020.santorini.controller.GameLogic;
import it.polimi.ingsw2020.santorini.model.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {

    public final static int PORT = 10001;
    private int matchIDGen;

    private ServerSocket socket;

    private final HashMap<Player, Integer> waitingPlayers;
    private final HashMap<String, ClientNetworkHandler> virtualClients;
    private final HashMap<Integer, GameLogic> controllers;
    private final HashMap<Integer, VirtualView> virtualViews;
    private final HashMap<String, Integer> playerInMatch;

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
    public static void main(String[] args) {
        Server server = new Server();
        while(true) {
            try {
                Socket client = server.socket.accept();
                ClientNetworkHandler clientNetworkHandler = new ClientNetworkHandler(client, server);
                clientNetworkHandler.start();
            } catch (IOException e) {
                System.out.println("connection failed!");
            }
        }
    }

    /**
     * the method checks for players who wants to have a match with an other player or other two players and creates the first
     * match that reaches the required of players
     * @param numberOfPlayers is the number of players that wants to join a particular kind of match
     */
    synchronized public void checkForMatches(int numberOfPlayers){
        if(waitingPlayers.values().stream()
                .filter(x -> x == numberOfPlayers)
                .count() >= numberOfPlayers) {
            controllers.put(matchIDGen, new GameLogic(this));
            virtualViews.put(matchIDGen, new VirtualView(controllers.get(matchIDGen)));
            controllers.get(matchIDGen).initializeMatch(virtualViews.get(matchIDGen), numberOfPlayers);
        }
    }

    /**
     * the method return a list of the players waiting to begin a match
     * @param numberOfPlayers number of players need to start a particular kind of match( 2 for a match of two players and 3 for the similar case )
     * @return an ArrayList of Player containing the players that want to do a match of 2 or 3 players (depends on numberOfPlayers)
     */
    synchronized public ArrayList<Player> getWaitingPlayers(int numberOfPlayers) {
        ArrayList<Player> list = new ArrayList<>();
        for(Player p : waitingPlayers.keySet())
            if(waitingPlayers.get(p) == numberOfPlayers) list.add(p);
        return list;
    }

    /**
     * the method adds a player to the hash map of waitingPlayers
     * @param player is the player that has to be added
     * @param numberOfPlayers is the key of the hash map
     */
    synchronized public void addWaitingPlayers(Player player, Integer numberOfPlayers) {
        waitingPlayers.put(player, numberOfPlayers);
    }

    /**
     * the method removes a player from the hash map
     * @param player is the player that has to be removed
     */
    synchronized public void removeWaitingPlayers(Player player) {
        waitingPlayers.remove(player);
    }

    /**
     * the method adds a Virtual Client to the VirtualClients HashMap
     * @param username is the username of the client that has to be added in the HashMap
     * @param handler is the ClientNetworkHandler of the client
     */
    public void addVirtualClient(String username, ClientNetworkHandler handler){
        this.virtualClients.put(username, handler);
        System.out.println("il client appena connesso si chiama: " + username + "\n" + "client in attesa di fare partite");
    }

    /**
     * the method returns the HashMap of the virtualClients
     * @return a HashMap of string and ClientNetworkHandler
     */
    public HashMap<String, ClientNetworkHandler> getVirtualClients() {
        return virtualClients;
    }

    /**
     * the method adds a player in the correspondent kind of match he wants to play in
     * @param username is the username of the client that will be associated with the match
     * @param id is the match's id and it si an Integer
     */
    synchronized public void addPlayerInMatch(String username, Integer id){
        playerInMatch.put(username, id);
    }

    /**
     * the method returns the match's ID in which the client is playing
     * @param username is the client's username and it is used to found the match's ID
     * @return the match's ID
     */
    synchronized public int getMatchFromUsername(String username){
        return playerInMatch.get(username);
    }

    /**
     * the method return the VirtualView of the match
     * @param id is the match'ID and it is used to found the Virtual View linked to the match
     * @return a VirtualView
     */
    synchronized public VirtualView getViewFromMatch(Integer id){
        return virtualViews.get(id);
    }

    /**
     * the method generates IDs for the Matches
     * @return an ID which is an integer
     */
    public int generateMatchID(){
        int tempId = matchIDGen;
        ++matchIDGen;
        return tempId;
    }

    /**
     * the method closes the server's socket
     */
    public void close(){
        try{
            socket.close();
        } catch (IOException e){
            System.out.println("cannot close server port");
        }

    }
}

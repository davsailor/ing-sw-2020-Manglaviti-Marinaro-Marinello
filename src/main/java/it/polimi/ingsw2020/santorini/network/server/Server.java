package it.polimi.ingsw2020.santorini.network.server;

import it.polimi.ingsw2020.santorini.controller.GameLogic;
import it.polimi.ingsw2020.santorini.model.Player;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class Server {

    public final static int PORT = 9999;
    private ServerSocket socket;

    public final static int PING_PORT = 8888;
    public final static int SO_TIMEOUT = 8000;
    private ServerSocket pingSocket;

    private int matchIDGen;
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
            pingSocket = new ServerSocket(PING_PORT);
            //pingSocket.setSoTimeout(SO_TIMEOUT);
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
        Thread acceptClient = new Thread(() -> {
        while(true) {
            try {
                Socket client = server.socket.accept();
                ClientNetworkHandler clientNetworkHandler = new ClientNetworkHandler(client, server);
                Socket pingClient = server.pingSocket.accept();
                clientNetworkHandler.start();
                Thread pingHandler = new Thread(() -> {
                    clientNetworkHandler.checkConnection(pingClient);
                });
                pingHandler.start();

            } catch (IOException e) {
                System.out.println("socket connection failed!");
            }
        }});
        acceptClient.start();
    }

    public ServerSocket getSocket() {
        return socket;
    }

    synchronized public void checkForMatches(int numberOfPlayers){
        if(waitingPlayers.values().stream()
                .filter(x -> x == numberOfPlayers)
                .count() >= numberOfPlayers) {
            controllers.put(matchIDGen, new GameLogic(this));
            virtualViews.put(matchIDGen, new VirtualView(controllers.get(matchIDGen)));
            controllers.get(matchIDGen).initializeMatch(virtualViews.get(matchIDGen), numberOfPlayers);
        }
    }

    synchronized public ArrayList<Player> getWaitingPlayers(int numberOfPlayers) {
        ArrayList<Player> list = new ArrayList<>();
        for(Player p : waitingPlayers.keySet())
            if(waitingPlayers.get(p) == numberOfPlayers) list.add(p);
        return list;
    }

    synchronized public void addWaitingPlayers(Player player, Integer numberOfPlayers) {
        waitingPlayers.put(player, numberOfPlayers);
    }

    synchronized public void removeWaitingPlayers(Player player) {
        waitingPlayers.remove(player);
    }

    synchronized public void removeWaitingPlayers(String username){
        Set<Player> player = waitingPlayers.keySet();
        for(Player p : player)
            if (p.getNickname().equals(username)) {
                waitingPlayers.remove(p);
                break;
            }
    }

    synchronized public void addVirtualClient(String username, ClientNetworkHandler handler){
        this.virtualClients.put(username, handler);
        System.out.println("il client appena connesso si chiama: " + username + "\n" + "client in attesa di fare partite");
    }

    synchronized public void removeVirtualClient(String username){
        virtualClients.remove(username);
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

    synchronized public void addPlayerInMatch(String username, Integer id){
        playerInMatch.put(username, id);
    }

    synchronized public int getMatchFromUsername(String username){
        return playerInMatch.get(username);
    }

    synchronized public VirtualView getViewFromMatch(Integer id){
        return virtualViews.get(id);
    }

    public int generateMatchID(){
        int tempId = matchIDGen;
        ++matchIDGen;
        return tempId;
    }

    public void close(){
        try{
            socket.close();
        } catch (IOException e){
            System.out.println("cannot close server port");
        }
    }
}

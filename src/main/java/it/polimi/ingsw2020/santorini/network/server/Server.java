package it.polimi.ingsw2020.santorini.network.server;

import it.polimi.ingsw2020.santorini.controller.GameLogic;
import it.polimi.ingsw2020.santorini.model.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {

    public final static int PORT = 9995;
    private int matchIDGen;

    private ServerSocket socket;

    private final ArrayList<Player> waitingPlayersMatch2;
    private final ArrayList<Player> waitingPlayersMatch3;

    private final HashMap<String, ClientNetworkHandler> virtualClients;
    private final HashMap<Integer, GameLogic> controllers;
    private final HashMap<Integer, VirtualView> virtualViews;
    private final HashMap<String, Integer> playerInMatch;

    public Server(){
        matchIDGen = 0;
        waitingPlayersMatch2 = new ArrayList<>();
        waitingPlayersMatch3 = new ArrayList<>();
        virtualClients = new HashMap<>();
        playerInMatch = new HashMap<>();
        try {
            socket = new ServerSocket(PORT);
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

    synchronized public void checkForMatches(){
        if(waitingPlayersMatch2.size() == 2) {
            controllers.put(matchIDGen, new GameLogic(this));
            virtualViews.put(matchIDGen, new VirtualView(controllers.get(matchIDGen)));
            controllers.get(matchIDGen).initializeMatch2(virtualViews.get(matchIDGen));
        }
        else if(waitingPlayersMatch3.size() == 3) {
            controllers.put(matchIDGen, new GameLogic(this));
            virtualViews.put(matchIDGen, new VirtualView(controllers.get(matchIDGen)));
            controllers.get(matchIDGen).initializeMatch3(virtualViews.get(matchIDGen));
        }
    }

    public ArrayList<Player> getWaitingPlayersMatch2() {
        return waitingPlayersMatch2;
    }

    synchronized public void addWaitingPlayersMatch2(Player player) {
        waitingPlayersMatch2.add(player);
    }

    synchronized public void removeWaitingPlayersMatch2(Player player) {
        waitingPlayersMatch2.remove(player);
    }

    public ArrayList<Player> getWaitingPlayersMatch3() {
        return waitingPlayersMatch3;
    }

    synchronized public void addWaitingPlayersMatch3(Player player) {
        waitingPlayersMatch3.add(player);
    }

    synchronized public void removeWaitingPlayersMatch3(Player player) {
        waitingPlayersMatch3.remove(player);
    }

    public void addVirtualClient(String username, ClientNetworkHandler handler){
        this.virtualClients.put(username, handler);
        System.out.println("il client appena connesso si chiama: " + username + "\n" + "client in attesa di fare partite");
    }

    public HashMap<String, ClientNetworkHandler> getVirtualClients() {
        return virtualClients;
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
}

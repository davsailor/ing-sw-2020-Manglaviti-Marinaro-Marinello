package it.polimi.ingsw2020.santorini.network.server;

import it.polimi.ingsw2020.santorini.controller.GameLogic;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.utils.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    /**
     * CLASSE CHE HA IL MAIN, I RIFERIMENTI ALLA VIRTUAL VIEW, LE INTERFACCE...
     */
    public final static int PORT = 9995;
    private ServerSocket socket;
    private final ArrayList<Player> waitingPlayersMatch2;
    private final ArrayList<Player> waitingPlayersMatch3;
    private final HashMap<String, ClientHandler> virtualClients;
    private GameLogic controller;

    public Server(){
        waitingPlayersMatch2 = new ArrayList<>();
        waitingPlayersMatch3 = new ArrayList<>();
        virtualClients = new HashMap<>();
        try {
            socket = new ServerSocket(PORT);
        } catch (IOException e) {
            System.out.println("cannot use server port");
            System.exit(10);
        }
        controller = new GameLogic(this);
    }

    public static void main(String[] args) {
        Server server = new Server();
        while(true)
        {
            try {
                Socket client = server.socket.accept();
                ClientHandler clientHandler = new ClientHandler(client, server);
                clientHandler.start();
            } catch (IOException e) {
                System.out.println("connection failed!");
            }
        }
    }

    synchronized public void checkForMatches(){
        if(waitingPlayersMatch2.size() == 2) controller.initializeMatch2(this);
        else if(waitingPlayersMatch3.size() == 3) controller.initializeMatch3(this);
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

    public void addVirtualClient(String username, ClientHandler handler){
        this.virtualClients.put(username, handler);
    }

    public HashMap<String, ClientHandler> getVirtualClients() {
        return virtualClients;
    }

    public GameLogic getController() {
        return controller;
    }

    public void setController(GameLogic controller) {
        this.controller = controller;
    }
}

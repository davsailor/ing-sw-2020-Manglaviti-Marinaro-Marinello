package it.polimi.ingsw2020.santorini.network.server;

import it.polimi.ingsw2020.santorini.model.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    /**
     * CLASSE CHE HA IL MAIN, I RIFERIMENTI ALLA VIRTUAL VIEW, LE INTERFACCE...
     */
    public final static int PORT = 9999;
    private ServerSocket socket;
    private ArrayList<Player> waitingPlayers;

    public Server(){
        waitingPlayers = new ArrayList<Player>();
        try {
            socket = new ServerSocket(PORT);
        } catch (IOException e) {
            System.out.println("a socket nun sa fira");
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        while(true)
        {
            try {
                Socket client = server.socket.accept();
                ClientHandler clientHandler = new ClientHandler(client);
                clientHandler.start();
            } catch (IOException e) {
                System.out.println(" a cunnessiuni nun s'ha dda fari");
            }
        }
    }
}

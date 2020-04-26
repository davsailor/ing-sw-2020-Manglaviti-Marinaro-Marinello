package it.polimi.ingsw2020.santorini.network.server;

import it.polimi.ingsw2020.santorini.network.NetworkInterface;
import it.polimi.ingsw2020.santorini.utils.Message;

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
    private ClientHandler clientHandler;
    private final ArrayList<Message> messageQueue;

    // magari da aggiungere un metodo per gestire la disconnessione

    /**
     * constructor of the class
     * @param client is the socket of the client that has to be handled
     */
    public ClientNetworkHandler(Socket client, Server server){
        this.server = server;
        this.client = client;
        this.clientHandler = new ClientHandler(this);
        this.clientHandler.start();
        this.messageQueue = new ArrayList<Message>();
        try {
            this.input = new ObjectInputStream(client.getInputStream());
            this.output = new ObjectOutputStream(client.getOutputStream());
        } catch (IOException e) {
            System.out.println("error IO");
            System.exit(2);
        }
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
    public void run(){
        while(true){
            try {
                Message message = (Message) input.readObject();
                receive(message);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                break;
            } catch (IOException e){
                System.out.println("IO exception");
                break;
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

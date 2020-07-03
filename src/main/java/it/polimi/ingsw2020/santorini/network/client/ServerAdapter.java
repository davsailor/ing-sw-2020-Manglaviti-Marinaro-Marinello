package it.polimi.ingsw2020.santorini.network.client;

import it.polimi.ingsw2020.santorini.network.NetworkInterface;
import it.polimi.ingsw2020.santorini.network.server.Server;
import it.polimi.ingsw2020.santorini.utils.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("deprecation")
public class ServerAdapter extends Thread implements NetworkInterface {
    private Client client;
    private Socket server;
    private boolean connected;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    /*
     * constructor of the class
     */
    public ServerAdapter(Client client, String ip) throws IOException{
        this.client = client;
        server = new Socket(ip, Server.PORT);
        server.setSoTimeout(Server.SO_TIMEOUT);
        out = new ObjectOutputStream(server.getOutputStream());
        in = new ObjectInputStream(server.getInputStream());
        connected = true;
    }

    synchronized public void setConnected(boolean status){
        this.connected = status;
    }

    /**
     * method that performs all the required actions to send a message to the server
     * @param message is the message that has to be sent
     */
    public void send(Message message){
        try{
            out.reset();
            out.writeObject(message);
            out.flush();
        } catch (IOException e){
            System.out.println("cannot send message to server");
            setConnected(false);
        }
    }

    /**
     * method used to add a new message from the server to the queue
     * @param message the received message
     */
    @Override
    public void receive(Message message){
        client.addMessageQueue(message);
    }

    /**
     * method used to check the connection client-side: it ping the server every SO_TIMEOUT / 2 milliseconds
     */
    @Override
    public void checkConnection() {
        try {
            while (true) {
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
     * the main thread of the network handler of the client
     * it listens to the server and reads all messages coming from the server
     * then it adds the to the queue using receive method
     * if the connection fails, the client is closed
     * @see Thread#run()
     */
    public void run() {
        Thread checkConnection = new Thread(this::checkConnection);
        checkConnection.start();
        while(connected){
            try {
                Message message = (Message) in.readObject();
                receive(message);
            } catch (SocketTimeoutException so) {
                System.out.println("Socket timed out");
                setConnected(false);
            } catch (IOException | ClassNotFoundException io){
                setConnected(false);
            }
        }
        System.out.println("server unreachable");
        System.exit(2);
    }
}

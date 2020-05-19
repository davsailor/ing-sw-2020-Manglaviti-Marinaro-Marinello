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
    private String ip;
    private boolean connected;
    private final Object connectedLock;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final static Logger LOGGER = Logger.getLogger("ServerAdapter");

    /*
     * constructor of the class
     */
    public ServerAdapter(Client client, String ip) throws IOException{
        this.client = client;
        this.ip = ip;
        server = new Socket(ip, Server.PORT);
        out = new ObjectOutputStream(server.getOutputStream());
        in = new ObjectInputStream(server.getInputStream());
        connected = true;
        connectedLock = new Object();
        Thread checkConnection = new Thread(() -> {checkConnection(null);});
        checkConnection.start();
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
        }
    }

    /**
     * method used to add a new message from the server to the queue
     * @param message the received message
     */
    @Override
    public void receive(Message message) {
        client.addMessageQueue(message);
    }

    /**
     * method used to check the connection client-side: it ping the server every SO_TIMEOUT / 4 milliseconds
     * @param client the client that requested the ping
     */
    @Override
    public void checkConnection(Socket client) {
        Socket probeSocket;
        ObjectInputStream pingIn;
        ObjectOutputStream pingOut;
        try {
            //Thread.sleep(1000);
            probeSocket = new Socket(ip, Server.PING_PORT);
            probeSocket.setSoTimeout(Server.SO_TIMEOUT);
            pingOut = new ObjectOutputStream(probeSocket.getOutputStream());
            pingIn = new ObjectInputStream(probeSocket.getInputStream());
            Message ping = new Message(null);
            while (true) {
                pingIn.readObject();
                Thread.sleep(Server.SO_TIMEOUT / 4);
                pingOut.reset();
                pingOut.writeObject(ping);
                pingOut.flush();
            }
        } catch (SocketTimeoutException ex){
            System.out.println("Socket timed out! Server is unreachable!");
            synchronized (connectedLock) {
                connected = false;
            }
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            System.out.println("io exception");
            synchronized (connectedLock) {
                connected = false;
            }
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
        LOGGER.log(Level.CONFIG, "ServerAdapter.run(): " + Thread.currentThread().getName());
        while(connected){
            try {
                Message message = (Message) in.readObject();
                receive(message);
            } catch (IOException | ClassNotFoundException e){
                synchronized (connectedLock) {
                    connected = false;
                }
            }
        }
        System.exit(2);
    }
}

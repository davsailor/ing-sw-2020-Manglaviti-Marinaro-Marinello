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

    public void send(Message message){
        try{
            out.reset();
            out.writeObject(message);
            out.flush();
        } catch (IOException e){
            System.out.println("cannot send message to server");
        }
    }

    @Override
    public void receive(Message message) {
        client.addMessageQueue(message);
    }

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
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
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

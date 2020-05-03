package it.polimi.ingsw2020.santorini.network.client;

import it.polimi.ingsw2020.santorini.network.NetworkInterface;
import it.polimi.ingsw2020.santorini.network.server.Server;
import it.polimi.ingsw2020.santorini.utils.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("deprecation")
public class ServerAdapter extends Thread implements NetworkInterface {
    private Client client;
    private Socket server;
    private boolean connected;
    private boolean listening;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final static Logger LOGGER = Logger.getLogger("ServerAdapter");

    public ServerAdapter(Client client, String ip){
        this.client = client;
        try {
            server = new Socket(ip, Server.PORT);
            out = new ObjectOutputStream(server.getOutputStream());
            in = new ObjectInputStream(server.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        listening = true;
        while(this.listening && this.connected){
            try {
                Message message = (Message) in.readObject();
                receive(message);
            } catch (ClassNotFoundException e) {
                this.listening = false;
                System.out.println("class not found");
            } catch (IOException e){
                this.listening = false;
                System.out.println("IO exception");
            }
        }
    }
}

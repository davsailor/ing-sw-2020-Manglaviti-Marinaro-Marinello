package it.polimi.ingsw2020.santorini.network.client;

import it.polimi.ingsw2020.santorini.exceptions.UnexpectedMessageException;
import it.polimi.ingsw2020.santorini.network.NetworkInterface;
import it.polimi.ingsw2020.santorini.network.server.Server;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.view.ViewInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class ServerAdapter extends Thread implements NetworkInterface {
    /**
     * classe che apre la connessione, verifica che non cada e riceve i messaggi --> server adapter delle slide
     */

    private Client client;
    private ViewInterface view;
    private String ipAddress;
    private Socket server;
    private boolean connected;
    private boolean listening;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isListening() {
        return listening;
    }

    public void setListening(boolean listening) {
        this.listening = listening;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public Socket getServer() {
        return server;
    }

    public ServerAdapter(Client client, String ipAddress){
        this.client = client;
        this.ipAddress = ipAddress;
        this.view = client.getView();
        try {
            this.server = new Socket(ipAddress, Server.PORT);
            out = new ObjectOutputStream(server.getOutputStream());
            in = new ObjectInputStream(server.getInputStream());
            connected = true;
        } catch (IOException e) {
            System.out.println("cannot connect to server");   // per @davsailor e i polentoni "il server deve capire"
            connected = false;
            System.exit(1);
        }
        // controllo periodico della connessione
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {public void run() {checkConnection();}};
        timer.schedule(task, 0, 3000);
    }

    /**
     * method that sends a message to the server
     * @param message is the message that has to be sent
     * @throws IOException
     */
    @Override
    public void send(Message message) throws IOException {
        out.reset();
        out.writeObject(message);
        out.flush();
    }

    /**
     * it is the backbone of the class and it is used to receive messages from the server
     * serve per ricevere i messaggi
     */
    @Override
    public void run(){
        listening = true;
        while(this.listening && this.connected){
            try {
                Message message = (Message) in.readObject();
                client.handleMessage(message);
                // questo serve per chiudere la sessione con il server e fermarci
            } catch (ClassNotFoundException e) {
                this.listening = false;
                System.out.println("class not found");
            } catch (UnexpectedMessageException e){
                this.listening = false;
                System.out.println("unexpected message");
            } catch (IOException e){
                this.listening = false;
                System.out.println("IO exception");
            }
        }

    }

    /**
     * method that checks that state of the connection. it is used in other methods through a timer
     * serve per verificare lo stato della connessione. Schedulato con un timer
     */
    public void checkConnection() {
        Socket probeSocket;
        if(connected) {
            try {
                probeSocket = new Socket(ipAddress, 8888);
                probeSocket.close();
            } catch (IOException e) {
                this.connected = false;
            }
            if (!this.connected) {
                // inserire gestione della disconnessione
            }
        }
    }

    // da vedere se il metodo per gestire i messaggi lo mettiamo qua o in client?
}

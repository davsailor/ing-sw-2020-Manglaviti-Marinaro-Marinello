package it.polimi.ingsw2020.santorini.network.client;

import it.polimi.ingsw2020.santorini.exceptions.UnexpectedMessageException;
import it.polimi.ingsw2020.santorini.network.NetworkInterface;
import it.polimi.ingsw2020.santorini.network.server.Server;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.SampleMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerAdapter extends Thread implements NetworkInterface {
    /**
     * classe che apre la connessione, verifica che non cada e riceve i messaggi --> server adapter delle slide
     */

    private Client client;
    private String ipAddress;
    private Socket server;
    boolean connected;
    boolean isListening = true;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ObjectOutputStream getOut() {
        return out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public ServerAdapter(Client client, String ipAddress){
        this.client = client;
        this.ipAddress = ipAddress;

        try {
            this.server = new Socket(ipAddress, Server.PORT);
            out = new ObjectOutputStream(server.getOutputStream());
            in = new ObjectInputStream(server.getInputStream());
            connected = true;
        } catch (IOException e) {
            System.out.println("u server ha dda capì");   // per @davsailor e i polentoni "il server deve capire"
            connected = false;
            System.exit(1);
        }
    }

    @Override
    public void send(Message message) throws IOException {
        System.out.println("1");
        out.reset();
        System.out.println("2");
        out.writeObject(message);
        System.out.println("3");
        out.flush();
        System.out.println("4");
    }

    // serve per ricevere i messaggi
    @Override
    public void run(){
        while(isListening){
            try {
                Message messageOut = new Message();
                SampleMessage payload = new SampleMessage("Messaggio inviato dal client");
                messageOut.buildSampleMessage(payload);
                send(messageOut);
                System.out.println("5");
                Message message = (Message) in.readObject();
                System.out.println("il server ha letto\n");
                switch(message.getHeaderMessageType()){
                    case PROVA:
                        SampleMessage mes = message.deserializeSampleMessage(message.getSerializedPayload());
                        System.out.println("Messaggio del server ricevuto!");
                        System.out.printf("%s\n", mes.getProva());
                        break;
                    default:
                        isListening = false;
                        throw new UnexpectedMessageException();
                }
                // questo serve per chiudere la sessione con il server e fermarci
                //server.close();
                //isListening = false;
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
            } catch (UnexpectedMessageException e){
                System.out.println("unexpected message");
            } catch (IOException e){
                System.out.println("IO exception");
            }
        }

    }

    // serve per verificare lo stato della connessione. verrà schedulato con un timer
    public void checkConnection() {
        Socket probeSocket;
        if(connected) {
            try {
                probeSocket = new Socket(ipAddress, 8888);
                probeSocket.close();
            } catch (IOException e) {
                connected = false;
            }
            if (!connected) {
                // inserire gestione della disconnessione
            }
        }
    }

    // da vedere se il metodo per gestire i messaggi lo mettiamo qua o in client?
}

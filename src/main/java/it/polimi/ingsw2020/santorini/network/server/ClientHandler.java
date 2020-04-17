package it.polimi.ingsw2020.santorini.network.server;

import it.polimi.ingsw2020.santorini.exceptions.UnexpectedMessageException;
import it.polimi.ingsw2020.santorini.network.NetworkInterface;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.SampleMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread implements NetworkInterface {

    private Socket client;
    // private Server server;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    // magari da aggiungere un metodo per gestire la disconnessione

    /**
     * constructor of the class
     * @param client is the socket of the client that has to be handled
     */
    public ClientHandler(Socket client){
        this.client = client;
        try {
            this.input = new ObjectInputStream(client.getInputStream());
            this.output = new ObjectOutputStream(client.getOutputStream());
        } catch (IOException e) {
            System.out.println("error IO");
            System.exit(2);
        }
    }

    @Override
    public void run(){
        try{
            handleClient();
        }
        catch (IOException e){
            System.out.println("client" + client.getInetAddress() + "connection dropped");
        }
    }

    public void handleClient() throws IOException{
        System.out.println("Connected to: " + client.getInetAddress());
        try{
            while(true){

                /*  gestione dei messaggi
                    un messaggio sarà fatto in questo modo: header + payload
                    header: ci dice che tipo di messaggio è
                    payload: contiene le infromazioni che, se lette interpretando il tipo di messaggio correttamente, avranno un senso
                 */

                Message message = (Message) input.readObject();
                System.out.println("il server ha letto\n");
                switch(message.getHeaderMessageType()){
                    case PROVA:
                        SampleMessage mes = message.deserializeSampleMessage(message.getSerializedPayload());
                        System.out.println("Messaggio dal Client ricevuto correttamente! Testo:");
                        System.out.printf("%s\n", mes.getProva());
                        SampleMessage payOut = new SampleMessage("Messaggio inviato dal server");
                        Message mesOut = new Message();
                        mesOut.buildSampleMessage(payOut);
                        send(mesOut);
                        break;
                    default:
                        throw new UnexpectedMessageException();
                }
            }
        }
        catch (ClassNotFoundException e) {
            System.out.println("class not found");
        }
        catch (UnexpectedMessageException e) {
            System.out.println("unexpected message");
        }
    }

    @Override
    public void send(Message message) throws IOException {
        output.reset();
        output.writeObject(message);
        output.flush();
    }
}
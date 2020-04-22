package it.polimi.ingsw2020.santorini.network.server;

import it.polimi.ingsw2020.santorini.exceptions.*;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.network.NetworkInterface;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread implements NetworkInterface {

    private Socket client;
    private Server server;
    private String username;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    // magari da aggiungere un metodo per gestire la disconnessione

    /**
     * constructor of the class
     * @param client is the socket of the client that has to be handled
     */
    public ClientHandler(Socket client, Server server){
        this.server = server;
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
            System.out.println("Client " + client.getInetAddress() + " connection dropped");
        }
    }

    /**
     * This method handle messages received from the client. The messages are composed by 3 sections:
     * 1) FirstLevelHandler that represents a first classification
     * 2) SecondLevelHandler that represents a second classification
     * 3)Payload
     * @throws IOException
     */
    public void handleClient() throws IOException{
        try{
            while(true){

                /*  gestione dei messaggi
                    un messaggio sarà fatto in questo modo: header + payload
                    header: ci dice che tipo di messaggio è
                    payload: contiene le infromazioni che, se lette interpretando il tipo di messaggio correttamente, avranno un senso
                 */

                Message message = (Message) input.readObject();
                System.out.println("Message received from client: " + client.getInetAddress() + "\n");
                switch(message.getFirstLevelHeader()){//diversi case ancora da aggiungere
                    case SETUP:
                        setupMessageHandler(message);
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

    /**
     *The method handles the login message creating a new player(for the server) and adding it to a list of players
     * depending on which kind of game they have chosen(with 2 or 3 players)
     * @param message it's the deserialized login message that has to be handled
     * @throws UnavailableUsernameException it signals that the username chosen by the player is already taken
     */
    private void loginHandler(LoginMessage message) throws UnavailableUsernameException {
        if(server.getVirtualClients().containsKey(message.getUsername()) || message.getUsername().equals("All"))
            throw new UnavailableUsernameException();
        else {
            Player player = new Player(message.getUsername(), message.getBirthDate());
            if (message.getNumberOfPlayers() == 2) server.addWaitingPlayersMatch2(player);
            else if (message.getNumberOfPlayers() == 3) server.addWaitingPlayersMatch3(player);
            server.addVirtualClient(message.getUsername(), this);
        }
    }

    /**
     * the method deserializes a login message invoking the respective handlers to handle it
     * @param message received by the server with FirstHeaderType.SETUP
     */
    public void setupMessageHandler(Message message){
        switch(message.getSecondLevelHeader()){
            case LOGIN:
                LoginMessage mes = message.deserializeLoginMessage(message.getSerializedPayload());
                try{
                    loginHandler(mes);
                } catch (UnavailableUsernameException e){
                    Message error = new Message();
                    error.buildUsernameErrorMessage(new UsernameErrorMessage("Your Username is not available!"));
                    try{
                        send(error);
                    } catch(IOException f){
                        // do nothing
                    }
                }
        }
    }

    @Override
    public void send(Message message) throws IOException {
        output.reset();
        output.writeObject(message);
        output.flush();
    }
}
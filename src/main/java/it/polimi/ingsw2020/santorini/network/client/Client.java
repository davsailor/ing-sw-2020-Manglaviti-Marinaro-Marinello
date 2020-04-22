package it.polimi.ingsw2020.santorini.network.client;

import it.polimi.ingsw2020.santorini.exceptions.UnexpectedMessageException;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.UsernameErrorMessage;
import it.polimi.ingsw2020.santorini.view.CLI;
import it.polimi.ingsw2020.santorini.view.ViewInterface;

import java.util.Date;

public class Client {
    /**
     * classe client che farà partire la cli, saprà gestire i messaggi ecc...
     */

    // vogliamo tenere la view come parametro del client? probabilmente si
    // vogliamo tenere l'ip del server? probabilmente si
    // input e output stream potrebbero servire? probabilmente si per comunicare i comandi
    private String username;
    private Date birthDate;
    private ServerAdapter adapter;
    private ViewInterface view;

    /**
     * getter of the attribute username
     * @return the user name
     */
    public String getUsername() {
        return username;
    }

    /**
     * setter of the attribute username
     * @param username is the name that will be assigned to the attribute username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * getter of the attribute birthDate
     * @return the birth date of the player
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * setter of the attribute
     * @param birthDate is the birth day of the player that will be assigned to the attribute
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * getter of the attribute
     * @return the server adapter of the client
     */
    public ServerAdapter getAdapter() {
        return adapter;
    }

    /**
     * setter of the attribute
     * @param adapter the server adapter that will be assigned to the attribute
     */
    public void setAdapter(ServerAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * getter of the attribute
     * @return the viewInterface
     */
    public ViewInterface getView() {
        return view;
    }


    public static void main(String[] args) {
       /* Client client = new Client();
        ServerAdapter adapter = new ServerAdapter(client, "127.0.0.1");
        client.setAdapter(adapter);

        // controllo periodico della conccessione
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {public void run() {adapter.checkConnection();}};
        timer.schedule(task, 0, 10000);

        // partono le interfacce addette alla comunicazione
        adapter.start();
*/
        Client client = new Client();
        client.view = new CLI(client);
        client.view.displaySetupWindow();
    }

    /**
     * method that handle the messages received by the server based on his FirstLevelHeader
     * (has to be extend)
     * @param message received by the server and has to be deserialized
     * @throws UnexpectedMessageException is the exception launched when an message has an unknown header
     */
    public void handleMessage(Message message) throws UnexpectedMessageException {
        switch(message.getFirstLevelHeader()){
            case SETUP:
                setupMessageHandler(message);
                break;
            case ERROR:
                errorMessageHandler(message);
                break;
            default:
                throw new UnexpectedMessageException();
        }
    }

    /**
     * method that handle the messages received by the server based on his SecondLevelHeader
     * @param message is the message that has to be deserialized
     * @throws UnexpectedMessageException is the exception launched when an message has an unknown header
     */
    public void setupMessageHandler(Message message) throws UnexpectedMessageException {
        switch(message.getSecondLevelHeader()){
            case LOGIN:
                break;
            default:
                throw new UnexpectedMessageException();
        }
    }

    /**
     * method that manages the messages that launch exceptions
     * @param message that launches exceptions
     * @throws UnexpectedMessageException is the exception launched when an message has an unknown header
     */
    public void errorMessageHandler(Message message) throws UnexpectedMessageException {
        switch(message.getSecondLevelHeader()){
            case USERNAME_ERROR:
                UsernameErrorMessage mes = message.deserializeUsernameErrorMessage(message.getSerializedPayload());
                view.displayErrorMessage(mes.getError());
                view.displayNewUsernameWindow();
                break;
            default:
                throw new UnexpectedMessageException();
        }
    }
    // metodo per gestire i messaggi lo mettiamo qua o in server adapter?
    //AGGIUNGERE METODO CHE SERIALIZZA I MESSAGGI CHE LANCIANO ERRORI
}

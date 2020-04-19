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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public ServerAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(ServerAdapter adapter) {
        this.adapter = adapter;
    }

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

    public void setupMessageHandler(Message message) throws UnexpectedMessageException {
        switch(message.getSecondLevelHeader()){
            case LOGIN:
                break;
            default:
                throw new UnexpectedMessageException();
        }
    }

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
}

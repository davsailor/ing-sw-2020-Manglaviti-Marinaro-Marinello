package it.polimi.ingsw2020.santorini.network.client;

import it.polimi.ingsw2020.santorini.exceptions.UnexpectedMessageException;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.*;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("deprecation")
public class ViewAdapter extends Thread {
    private Client client;
    private final static Logger LOGGER = Logger.getLogger("ViewAdapter");

    public ViewAdapter(Client client){
        this.client = client;
    }

    private void handleMessage() {
        Message message = client.getNextMessage();
        client.removeMessageQueue(message);
        System.out.println(message.getFirstLevelHeader() + ", " + message.getSecondLevelHeader());
        switch (message.getFirstLevelHeader()) {
            case SETUP:
                setupMessageHandler(message);
                break;
            case LOADING:
                loadingMessageHandler(message);
                break;
            case ERROR:
                errorMessageHandler(message);
                break;
            default:
                break;
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
        LOGGER.setLevel(Level.CONFIG);
        LOGGER.log(Level.CONFIG, "ViewAdapter.run(): " + Thread.currentThread().getName());
        while(true){
            while(!client.hasNextMessage());
            handleMessage();
        }
    }

    /**
     * method that handle the messages received by the server based on his SecondLevelHeader
     * @param message is the message that has to be deserialized
     */
    public void setupMessageHandler(Message message) {
        switch(message.getSecondLevelHeader()){
            case MATCH:
                MatchSetupMessage matchSetupMessage = message.deserializeMatchSetupMessage(message.getSerializedPayload());
                client.getView().displayMatchSetupWindow(matchSetupMessage);
                break;
            default:
                break;
        }
    }

    private void loadingMessageHandler(Message message) {
        switch(message.getSecondLevelHeader()){
            case LOGIN:
                CorrectLoginMessage mes = message.deserializeCorrectLoginMessage(message.getSerializedPayload());
                client.getView().displayLoadingWindow(mes.getText());
                break;
            default:
                break;
        }
    }

    /**
     * method that manages the messages that launch exceptions
     * @param message that launches exceptions
     */
    public void errorMessageHandler(Message message){
        switch(message.getSecondLevelHeader()){
            case USERNAME_ERROR:
                UsernameErrorMessage mes = message.deserializeUsernameErrorMessage(message.getSerializedPayload());
                client.getView().displayErrorMessage(mes.getError());
                client.getView().displayNewUsernameWindow();
                break;
            default:
                break;
        }
    }
}

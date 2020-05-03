package it.polimi.ingsw2020.santorini.network.client;

import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.errors.IllegalPositionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.errors.UsernameErrorMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.CorrectLoginMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.MatchSetupMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.TurnPlayerMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.UpdateMessage;

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
        //System.out.println(message.getFirstLevelHeader() + ", " + message.getSecondLevelHeader());
        switch (message.getFirstLevelHeader()) {
            case SETUP:
                setupMessageHandler(message);
                break;
            case LOADING:
                loadingMessageHandler(message);
                break;
            case ASK:
                askMessageHandler(message);
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
            case PLAYER_SELECTION:
                TurnPlayerMessage turnPlayerMessage = message.deserializeTurnPlayerMessage(message.getSerializedPayload());
                client.getView().displaySelectionBuilderWindow(turnPlayerMessage);
            default:
                break;
        }
    }

    public void askMessageHandler(Message message) {
        switch(message.getSecondLevelHeader()){
            case ACTIVATE_GOD:
                client.getView().displayWouldActivate(message.deserializeActivationRequestInfoMessage(message.getSerializedPayload()));
                break;
            case SELECT_PARAMETERS:
                client.getView().displayParametersSelection(message.deserializeActivationRequestInfoMessage(message.getSerializedPayload()));
                break;
            case SELECT_BUILDER:
                client.getView().displayChooseBuilder(message.deserializeTurnPlayerMessage(message.getSerializedPayload()));
                break;
            case SELECT_CELL_MOVE:
                client.getView().displayPossibleMoves(message.deserializeAskMoveSelectionMessage());
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
            case MATCH:
                UpdateMessage startMessage = message.deserializeUpdateMessage(message.getSerializedPayload());
                client.getView().updateMatch(startMessage);
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
            case INVALID_CELL_SELECTION:
                IllegalPositionMessage illegalPositionMessage = message.deserializeIllegalPositionMessage(message.getSerializedPayload());
                if(illegalPositionMessage.isBuilderMToChange() || illegalPositionMessage.isBuilderFToChange())
                    client.getView().displayNewSelectionBuilderWindow(illegalPositionMessage);
                break;
            default:
                break;
        }
    }
}
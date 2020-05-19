package it.polimi.ingsw2020.santorini.network.client;

import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.errors.IllegalPositionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.errors.GenericErrorMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.*;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("deprecation")
public class ViewAdapter extends Thread {
    private Client client;
    private final static Logger LOGGER = Logger.getLogger("ViewAdapter");
    private final ArrayList<Thread> cliThreads;

    /*
     * constructor of the class
     */
    public ViewAdapter(Client client){
        this.client = client;
        cliThreads = new ArrayList<>();
    }

    /**
     * this is the message handler. it is the first clearing house of the messages and redirects them to the
     * proper method, looking at the firstLevelHeader of the message
     * @param message the message to handle
     */
    private void handleMessage(Message message) {
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
        cliThreads.remove(Thread.currentThread());
    }

    /**
     * method that handle the messages received by the server with SETUP as the firstLevelHeader.
     * this is the second clearing house for SETUP messages.
     * after the deserialization of the message, it selects the correct view that has to be shown to the client
     * @param message is the message that has to be handled
     */
    public void setupMessageHandler(Message message) {
        switch(message.getSecondLevelHeader()){
            case MATCH:
                MatchSetupMessage matchSetupMessage = message.deserializeMatchSetupMessage(message.getSerializedPayload());
                client.getView().displayMatchSetupWindow(matchSetupMessage);
                break;
            case PLAYER_SELECTION:
                MatchStateMessage matchStateMessage = message.deserializeMatchStateMessage(message.getSerializedPayload());
                client.getView().displaySelectionBuilderWindow(matchStateMessage);
            default:
                break;
        }
    }

    /**
     * method that handle the messages received by the server with ASK as the firstLevelHeader.
     * this is the second clearing house for ASK messages.
     * after the deserialization of the message, it selects the correct view that has to be shown to the client
     * @param message is the message that has to be handled
     */
    public void askMessageHandler(Message message) {
        switch(message.getSecondLevelHeader()){
            case ACTIVATE_GOD:
                client.getView().displayWouldActivate(message.deserializeMatchStateMessage(message.getSerializedPayload()));
                break;
            case SELECT_PARAMETERS:
                client.getView().displayParametersSelection(message.deserializeMatchStateMessage(message.getSerializedPayload()));
                break;
            case SELECT_BUILDER:
                client.getView().displayChooseBuilder(message.deserializeMatchStateMessage(message.getSerializedPayload()));
                break;
            case SELECT_CELL_MOVE:
                client.getView().displayPossibleMoves(message.deserializeAskMoveSelectionMessage());
                break;
            case SELECT_CELL_BUILD:
                client.getView().displayPossibleBuildings(message.deserializeAskBuildSelectionMessage());
            default:
                break;
        }
    }

    /**
     * method that handle the messages received by the server with LOADING as the firstLevelHeader.
     * this is the second clearing house for LOADING messages.
     * after the deserialization of the message, it selects the correct view that has to be shown to the client
     * @param message is the message that has to be handled
     */
    private void loadingMessageHandler(Message message) {
        switch(message.getSecondLevelHeader()){
            case LOGIN:
                CorrectLoginMessage mes = message.deserializeCorrectLoginMessage(message.getSerializedPayload());
                client.getView().displayLoadingWindow(mes.getText());
                break;
            case MATCH:
                UpdateMessage updateMessage = message.deserializeUpdateMessage(message.getSerializedPayload());
                client.getView().updateMatch(updateMessage);
                break;
            case END_MATCH:
                EndMatchMessage endMatchMessage = message.deserializeEndMatchMessage();
                String name = Thread.currentThread().getName();
                for(Thread t : cliThreads)
                    if(!t.getName().equals(Thread.currentThread().getName())) {
                        cliThreads.remove(t);
                        try {
                            t.wait();
                            t.interrupt();
                        } catch (Exception ignored) {}
                    }
                client.getView().displayEndMatch(endMatchMessage.getWinner());
            default:
                break;
        }
    }

    /**
     * method that handle the messages received by the server with ERROR as the firstLevelHeader.
     * this is the second clearing house for ERROR messages.
     * after the deserialization of the message, it selects the correct view that has to be shown to the client
     * @param message is the message that has to be handled
     */
    public void errorMessageHandler(Message message){
        switch(message.getSecondLevelHeader()){
            case USERNAME_ERROR:
                GenericErrorMessage mes = message.deserializeUsernameErrorMessage(message.getSerializedPayload());
                client.getView().displayErrorMessage(mes.getError());
                client.getView().displaySetupWindow(false);
                break;
            case INVALID_CELL_SELECTION:
                IllegalPositionMessage illegalPositionMessage = message.deserializeIllegalPositionMessage(message.getSerializedPayload());
                if(illegalPositionMessage.isBuilderMToChange() || illegalPositionMessage.isBuilderFToChange())
                    client.getView().displayNewSelectionBuilderWindow(illegalPositionMessage);
                break;
            case INVALID_MOVE:
                GenericErrorMessage invalidMoveMessage = message.deserializeInvalidMoveMessage();
                client.getView().displayErrorMessage(invalidMoveMessage.getError());
                Message nextPhaseMessage = new Message(client.getUsername());
                nextPhaseMessage.buildNextPhaseMessage();
                client.getNetworkHandler().send(nextPhaseMessage);
                break;
        }
    }

    /**
     * the main thread of the class. it checks the queue and, when it is not empty, remove its first messages and tries
     * to handle it
     * @see Thread#run()
     */
    public void run() {
        LOGGER.setLevel(Level.CONFIG);
        LOGGER.log(Level.CONFIG, "ViewAdapter.run(): " + Thread.currentThread().getName());
        while(true){
            while(!client.hasNextMessage());
            Message message = client.getNextMessage();
            client.removeMessageQueue(message);
            Thread thread = new Thread (() -> handleMessage(message));
            cliThreads.add(thread);
            thread.start();
        }
    }
}
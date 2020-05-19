package it.polimi.ingsw2020.santorini.network.server;

import it.polimi.ingsw2020.santorini.exceptions.*;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.errors.GenericErrorMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.CorrectLoginMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.LoginMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.NewMatchMessage;

public class ClientHandler extends Thread{
    private ClientNetworkHandler owner;

    /*
     * constructor of the class
     */
    public ClientHandler(ClientNetworkHandler owner){
        this.owner = owner;
    }

    /**
     * this is the message handler. it is the first clearing house of the messages and redirects them to the
     * proper method, looking at the firstLevelHeader of the message
     *
     */
    public synchronized void handleMessage() {
        Message message = owner.getNextMessage();
        owner.removeMessageQueue(message);
        switch (message.getFirstLevelHeader()) {
            case SETUP:
                setupMessageHandler(message);
                break;
            default:
                try {
                    owner.getServer().getViewFromMatch(owner.getServer().getMatchFromUsername(owner.getUsername())).notifyController(message);
                } catch (NullPointerException ignored) {}
        }
    }

    /**
     * method that handle the messages received by the client with SETUP as the firstLevelHeader.
     * this is the second clearing house for SETUP messages.
     * after the deserialization of the message, it performs all required actions to satisfy the request
     * @param message is the message that has to be handled
     */
    public void setupMessageHandler(Message message){
        switch(message.getSecondLevelHeader()){
            case LOGIN:
                LoginMessage mes = message.deserializeLoginMessage(message.getSerializedPayload());
                try{
                    loginHandler(mes);
                } catch (UnavailableUsernameException e){
                    // username è messo a null perchè non serve specificarlo, visto che qui siamo nella zona di
                    // competenza dell'interfaccia legata a quel client
                    // questa è già una prima barriera prima di accedere alla virtualview
                    Message error = new Message(null);
                    error.buildUsernameErrorMessage(new GenericErrorMessage("Your Username is not available!"));
                    owner.send(error);
                }
                break;
            case NEW_MATCH:
                NewMatchMessage newMatchMessage = message.deserializeNewMatchMessage();
                if(newMatchMessage.isWantNewMatch()) {
                    owner.getServer().addWaitingPlayers(new Player(owner.getUsername(), newMatchMessage.getBirthDate()), newMatchMessage.getSelectedMatch());
                    owner.getServer().checkForMatches(newMatchMessage.getSelectedMatch());
                }
                else
                    owner.getServer().removeVirtualClient(owner.getUsername());
                break;
            default:
                break;
        }
    }

    /**
     * method that handle the messages received by the client with LOGIN as the secondLevelHeader.
     * it performs all required actions to log in the client
     * @param message is the message that has to be handled
     */
    private void loginHandler(LoginMessage message) throws UnavailableUsernameException {
        if (owner.getServer().getVirtualClients().containsKey(message.getUsername()) || message.getUsername().equals("All")) {
            throw new UnavailableUsernameException();
        } else {
            Player player = new Player(message.getUsername(), message.getBirthDate());
            owner.setUsername(message.getUsername());
            // username è messo a null perchè non serve specificarlo, visto che qui siamo nella zona di
            // competenza dell'interfaccia legata a quel client
            // questa è già una prima barriera prima di accedere alla virtualview
            Message correct = new Message(null);
            correct.buildCorrectLoginMessage(new CorrectLoginMessage());
            owner.send(correct);
            owner.getServer().addWaitingPlayers(player, message.getNumberOfPlayers());
            owner.getServer().addVirtualClient(message.getUsername(), owner);
            owner.getServer().checkForMatches(message.getNumberOfPlayers());
        }
    }

    /**
     * the main thread of the class. it checks the queue and, when it is not empty, remove its first messages and tries
     * to handle it
     * @see Thread#run()
     */
    @Override
    public void run() {
        while(true){
            while(!owner.hasNextMessage());
            handleMessage();
        }
    }
}

package it.polimi.ingsw2020.santorini.network.server;

import it.polimi.ingsw2020.santorini.exceptions.*;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.*;

public class ClientHandler extends Thread{
    private ClientNetworkHandler owner;

    public ClientHandler(ClientNetworkHandler owner){
        this.owner = owner;
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
    @Override
    public void run() {
        while(true){
            while(!owner.hasNextMessage());
            handleMessage();
        }
    }

    public void handleMessage() {
        Message message = owner.getNextMessage();
        owner.removeMessageQueue(message);
        switch (message.getFirstLevelHeader()) {
            case SETUP:
                setupMessageHandler(message);
                break;
            default:
                owner.getServer().getViewFromMatch(owner.getServer().getMatchFromUsername(owner.getUsername())).notifyController(message);
        }
    }

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
                    error.buildUsernameErrorMessage(new UsernameErrorMessage("Your Username is not available!"));
                    owner.send(error);
                }
                break;
            default:
                break;
        }
    }

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
            if (message.getNumberOfPlayers() == 2) owner.getServer().addWaitingPlayersMatch2(player);
            else if (message.getNumberOfPlayers() == 3) owner.getServer().addWaitingPlayersMatch3(player);
            owner.getServer().addVirtualClient(message.getUsername(), owner);
            owner.getServer().checkForMatches();
        }
    }
}

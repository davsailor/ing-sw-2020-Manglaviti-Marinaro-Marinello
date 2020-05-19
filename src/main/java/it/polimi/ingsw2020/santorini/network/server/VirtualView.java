package it.polimi.ingsw2020.santorini.network.server;

import it.polimi.ingsw2020.santorini.controller.GameLogic;
import it.polimi.ingsw2020.santorini.model.Match;
import it.polimi.ingsw2020.santorini.utils.Message;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("deprecation")

public class VirtualView extends Observable implements Observer {
    /*
     * classe usate come intermediario tra la view e il controller
     */
    private Server server;
    private Match match;

    /**
     * constructor of the class
     * @param controller the observer of the virtualView
     */
    public VirtualView(GameLogic controller){
        this.server = controller.getServer();
        addObserver(controller);
    }

    /*
     * getters and setters
     */
    public Server getServer() {
        return server;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Match getMatch() {
        return match;
    }

    /**
     * method used to notify the observer
     * @param message the message the controller has to parse to understand the request of the client
     */
    public void notifyController(Message message){
        Message mes = message;
        setChanged();
        notifyObservers(mes);
    }

    /**
     * method used to notify all clients after the model has been modified
     * @param match the match modified
     * @param messageList the messages that has to be sent to the corresponding clients
     */
    @Override
    public void update(Observable match, Object messageList) {
        ArrayList<Message> list = (ArrayList<Message>) messageList;
        for(Message mes : list)
            if(server.getVirtualClients().containsKey(mes.getUsername()))
                server.getVirtualClients().get(mes.getUsername()).send(mes);
    }
}

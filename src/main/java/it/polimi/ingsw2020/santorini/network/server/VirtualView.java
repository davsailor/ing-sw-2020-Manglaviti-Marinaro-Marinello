package it.polimi.ingsw2020.santorini.network.server;

import it.polimi.ingsw2020.santorini.controller.GameLogic;
import it.polimi.ingsw2020.santorini.model.Match;
import it.polimi.ingsw2020.santorini.utils.Message;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("deprecation")

public class VirtualView extends Observable implements Observer {
    /**
     * classe usate come intermediario tra la view e il controller
     */
    private Server server;
    private Match match;
    private ArrayList<Observer> observers;

    public VirtualView(Server server){
        this.server = server;
        addObserver(server.getController());
    }

    public void notifyController(Message message){
        Message mes = message;
        setChanged();
        notifyObservers(mes);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}

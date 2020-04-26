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
    private Match match;        // non è mai usata... serve davvero?

    public VirtualView(GameLogic controller){
        this.server = controller.getServer();
        addObserver(controller);
    }

    public void notifyController(Message message){
        Message mes = message;
        setChanged();
        notifyObservers(mes);
    }

    public Server getServer() {
        return server;
    }

    @Override
    public void update(Observable match, Object messageList) {
        ArrayList<Message> list = (ArrayList<Message>) messageList;
        for(Message mes : list){
            server.getVirtualClients().get(mes.getUsername()).send(mes);
        }
    }
}

package it.polimi.ingsw2020.santorini.controller;

import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.network.server.Server;
import it.polimi.ingsw2020.santorini.network.server.VirtualView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
@SuppressWarnings("deprecation")

public class GameLogic implements Observer {

    /**
     *  colui che si occupa di creare il match e gestire i messaggi (fare una prima scrematura)
     */
    private VirtualView view;
    private TurnLogic turnManager;
    private Server server;
    private Match match;
    //private ArrayList<Player> waitingPlayers;
    //private ArrayList<Player> disconnectedPlayers;

    public GameLogic(Server server){
        view = new VirtualView();
        turnManager = new TurnLogic();
        this.server = server;
        this.match = null;
    }

    public void initializeMatch2(Server server){
        this.match = new Match(new Board(new GodDeck()), 2, this);
        Player[] orderedList = new Player[2];
        Player player1 = server.getWaitingPlayersMatch2().get(0);
        Player player2 = server.getWaitingPlayersMatch2().get(1);
        server.removeWaitingPlayersMatch2(player1);
        server.removeWaitingPlayersMatch2(player2);
        if(player1.getBirthdate().compareTo(player2.getBirthdate()) >= 0) {
            orderedList[0] = player1;
            orderedList[1] = player2;
        }
        else {
            orderedList[0] = player2;
            orderedList[1] = player1;
        }
        match.setPlayers(orderedList);
    }

    public void initializeMatch3(Server server){
        this.match = new Match(new Board(new GodDeck()), 3, this);
        Player[] orderedList = new Player[3];
        Player player1 = server.getWaitingPlayersMatch3().get(0);
        Player player2 = server.getWaitingPlayersMatch3().get(1);
        Player player3 = server.getWaitingPlayersMatch3().get(2);
        server.removeWaitingPlayersMatch3(player1);
        server.removeWaitingPlayersMatch3(player2);
        server.removeWaitingPlayersMatch3(player3);
        if(player1.getBirthdate().compareTo(player2.getBirthdate()) >= 0) {
            buildOrderedList3(orderedList, player1, player2, player3);
        }
        else {
            buildOrderedList3(orderedList, player2, player1, player3);
        }
        match.setPlayers(orderedList);
    }

    private void buildOrderedList3(Player[] orderedList, Player player1, Player player2, Player player3) {
        if(player1.getBirthdate().compareTo(player3.getBirthdate()) >= 0) {
            orderedList[0] = player1;
            if (player2.getBirthdate().compareTo(player3.getBirthdate()) >= 0) {
                orderedList[1] = player2;
                orderedList[2] = player3;
            } else {
                orderedList[1] = player3;
                orderedList[2] = player2;
            }
        } else {
            orderedList[0] = player3;
            orderedList[1] = player1;
            orderedList[2] = player2;
        }
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}

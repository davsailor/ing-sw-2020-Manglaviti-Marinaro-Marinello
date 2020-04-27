package it.polimi.ingsw2020.santorini.controller;

import it.polimi.ingsw2020.santorini.exceptions.UnexpectedMessageException;
import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.network.server.Server;
import it.polimi.ingsw2020.santorini.network.server.VirtualView;
import it.polimi.ingsw2020.santorini.utils.AccessType;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PlayerStatus;
import it.polimi.ingsw2020.santorini.utils.messages.*;

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
        turnManager = new TurnLogic();
        this.server = server;
        this.match = null;
    }

    public void setView(VirtualView view) {
        this.view = view;
    }

    public Server getServer() {
        return server;
    }

    public void initializeMatch2(VirtualView view) {
        System.out.println("creo il match da 2");
        setView(view);
        this.match = new Match(new Board(new GodDeck()), 2, view);
        Player[] orderedList = new Player[2];
        Player player1 = server.getWaitingPlayersMatch2().get(0);
        Player player2 = server.getWaitingPlayersMatch2().get(1);
        server.removeWaitingPlayersMatch2(player1);
        server.removeWaitingPlayersMatch2(player2);
        server.addPlayerInMatch(player1.getNickname(), match.getMatchID());
        server.addPlayerInMatch(player2.getNickname(), match.getMatchID());
        if(player1.getBirthDate().compareTo(player2.getBirthDate()) >= 0) {
            orderedList[0] = player1;
            orderedList[1] = player2;
        }
        else {
            orderedList[0] = player2;
            orderedList[1] = player1;
        }
        match.initialize(orderedList);
    }

    public void initializeMatch3(VirtualView view) {
        System.out.println("creo il match da 3");
        setView(view);
        this.match = new Match(new Board(new GodDeck()), 3, view);
        Player[] orderedList = new Player[3];
        Player player1 = server.getWaitingPlayersMatch3().get(0);
        Player player2 = server.getWaitingPlayersMatch3().get(1);
        Player player3 = server.getWaitingPlayersMatch3().get(2);
        server.removeWaitingPlayersMatch3(player1);
        server.removeWaitingPlayersMatch3(player2);
        server.removeWaitingPlayersMatch3(player3);
        server.addPlayerInMatch(player1.getNickname(), match.getMatchID());
        server.addPlayerInMatch(player2.getNickname(), match.getMatchID());
        server.addPlayerInMatch(player3.getNickname(), match.getMatchID());
        if(player1.getBirthDate().compareTo(player2.getBirthDate()) >= 0) {
            buildOrderedList3(orderedList, player1, player2, player3);
        }
        else {
            buildOrderedList3(orderedList, player2, player1, player3);
        }
        match.initialize(orderedList);
    }

    private void buildOrderedList3(Player[] orderedList, Player player1, Player player2, Player player3) {
        if(player1.getBirthDate().compareTo(player3.getBirthDate()) >= 0) {
            orderedList[0] = player1;
            if (player2.getBirthDate().compareTo(player3.getBirthDate()) >= 0) {
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
    public void update(Observable view, Object mes) {
        Message message = (Message) mes;
        try{
            switch(message.getFirstLevelHeader()){
                case SYNCHRONIZATION:
                    synchronizationHandler(message);
                    break;
                case ASK:
                    askHandler(message);
                    break;
                case VERIFY:
                    validationHandler(message);
                    break;
                default:
                    throw new UnexpectedMessageException();
            }
        } catch(UnexpectedMessageException e) {
            System.out.println("unexpected message");
        }
    }

    public void askHandler(Message message){
        switch (message.getSecondLevelHeader()){
            default:
                break;
        }
    }

    synchronized public void synchronizationHandler(Message message){
        switch(message.getSecondLevelHeader()){
            case BEGIN_MATCH:
                Player current = match.getPlayerByName(message.getUsername());
                current.setStatus(PlayerStatus.READY);
                Player[] players = match.getPlayers();
                boolean allReady = true;
                for(int i = 0; i < match.getNumberOfPlayers() && allReady; ++i){
                    if (players[i].getStatus() != PlayerStatus.READY) allReady = false;
                }

                if(allReady) {
                    ArrayList<Message> orderMessage = new ArrayList<>();
                    for (int i = 0; i < match.getNumberOfPlayers(); ++i) {
                        orderMessage.add(new Message(players[i].getNickname()));
                        orderMessage.get(i).buildTurnPlayerMessage(new TurnPlayerMessage(players[match.getCurrentPlayerIndex()].getNickname(), match.getBoard().getBoard()));
                    }
                    match.notifyView(orderMessage);
                }
                break;
            default:
                break;
        }
    }

    public void validationHandler(Message message){
        switch(message.getSecondLevelHeader()){
            case CORRECT_SELECTION_POS:
                SelectedBuilderPosMessage selectedBuilderPosMessage = message.deserializeSelectedBuilderPosMessage(message.getSerializedPayload());
                Cell[][] board = match.getBoard().getBoard();
                boolean builderFToChange, builderMToChange;
                builderFToChange = true;
                builderMToChange = true;
                if(selectedBuilderPosMessage.getBuilderF() != null && board[selectedBuilderPosMessage.getBuilderF()[0]][selectedBuilderPosMessage.getBuilderF()[1]].getStatus() == AccessType.FREE) builderFToChange = false;
                if(selectedBuilderPosMessage.getBuilderM() != null && board[selectedBuilderPosMessage.getBuilderM()[0]][selectedBuilderPosMessage.getBuilderM()[1]].getStatus() == AccessType.FREE) builderMToChange = false;

                if(!builderFToChange){
                    board[selectedBuilderPosMessage.getBuilderF()[0]][selectedBuilderPosMessage.getBuilderF()[1]].setStatus(AccessType.OCCUPIED);
                    match.getPlayerByName(selectedBuilderPosMessage.getUsername()).setBuilderF(new Builder(match.getPlayerByName(selectedBuilderPosMessage.getUsername()),'\u2640', match.getBoard())); //♚
                    board[selectedBuilderPosMessage.getBuilderF()[0]][selectedBuilderPosMessage.getBuilderF()[1]].setBuilder(match.getPlayerByName(selectedBuilderPosMessage.getUsername()).getBuilderF());
                }

                if(!builderMToChange){
                    board[selectedBuilderPosMessage.getBuilderM()[0]][selectedBuilderPosMessage.getBuilderM()[1]].setStatus(AccessType.OCCUPIED);
                    match.getPlayerByName(selectedBuilderPosMessage.getUsername()).setBuilderM(new Builder(match.getPlayerByName(selectedBuilderPosMessage.getUsername()),'\u2642', match.getBoard())); // ♛
                    board[selectedBuilderPosMessage.getBuilderM()[0]][selectedBuilderPosMessage.getBuilderM()[1]].setBuilder(match.getPlayerByName(selectedBuilderPosMessage.getUsername()).getBuilderM());
                }

                if(builderFToChange || builderMToChange) {
                    Message illegalPosition = new Message(message.getUsername());
                    IllegalPositionMessage illegalPositionMessage = new IllegalPositionMessage(message.getUsername(), builderFToChange, builderMToChange);
                    illegalPosition.buildIllegalPositionMessage(illegalPositionMessage);
                    ArrayList<Message> list = new ArrayList<>();
                    list.add(illegalPosition);
                    match.notifyView(list);
                } else {
                    match.getPlayerByName(message.getUsername()).setStatus(PlayerStatus.PLAYING);
                    match.setNextPlayer();

                    Player[] playingPlayers = match.getPlayers();
                    boolean allPlaying = true;

                    for (int i = 0; i < match.getNumberOfPlayers() && allPlaying; ++i) {
                        if (playingPlayers[i].getStatus() != PlayerStatus.PLAYING) allPlaying = false;
                    }

                    if (allPlaying) {
                        ArrayList<Message> listOfStartMessage = new ArrayList<>();
                        match.setCurrentPlayerIndex(0);
                        for (int i = 0; i < match.getNumberOfPlayers(); ++i) {
                            listOfStartMessage.add(new Message(match.getPlayers()[i].getNickname()));
                            listOfStartMessage.get(i).buildMatchStartMessage(new MatchStartMessage(match));
                            match.notifyView(listOfStartMessage);
                        }
                    } else {
                        ArrayList<Message> orderMessage = new ArrayList<>();
                        for (int i = 0; i < match.getNumberOfPlayers(); ++i) {
                            orderMessage.add(new Message(playingPlayers[i].getNickname()));
                            orderMessage.get(i).buildTurnPlayerMessage(new TurnPlayerMessage(match.getCurrentPlayer().getNickname(), match.getBoard().getBoard()));
                        }
                        match.notifyView(orderMessage);
                    }
                }
                break;
            default:
                break;
        }

    }
}
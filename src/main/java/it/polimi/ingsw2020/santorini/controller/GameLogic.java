package it.polimi.ingsw2020.santorini.controller;

import it.polimi.ingsw2020.santorini.exceptions.UnexpectedMessageException;
import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.network.server.Server;
import it.polimi.ingsw2020.santorini.network.server.VirtualView;
import it.polimi.ingsw2020.santorini.utils.AccessType;
import it.polimi.ingsw2020.santorini.utils.ActionType;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PlayerStatus;
import it.polimi.ingsw2020.santorini.utils.messages.actions.SelectedBuilderMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.SelectedBuilderPositionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.errors.IllegalPositionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.TurnPlayerMessage;

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

    /**
     * Setter of view Attribute
     * @param view that has to be set
     */
    public void setView(VirtualView view) {
        this.view = view;
    }

    /**
     * Getter of the server attribute
     * @return the attribute server
     */
    public Server getServer() {
        return server;
    }

    /**
     * The method initialize matches that have the required number of players. The matches created will have a reference to their relatives VirtualView
     * @param view is the reference of GameLogic's VirtualView that will be the match VirtualView
     * @param numberOfPlayers is the number of player that will take part to the match
     */
    public void initializeMatch(VirtualView view, int numberOfPlayers) {
        System.out.printf("creo il match da %d\n", numberOfPlayers);
        setView(view);
        this.match = new Match(new Board(new GodDeck()), numberOfPlayers, view);
        Player[] list = new Player[numberOfPlayers];
        ArrayList<Player> queue = server.getWaitingPlayers(numberOfPlayers);
        for(int i = 0; i < numberOfPlayers; ++i){
            list[i] = queue.get(i);
            server.removeWaitingPlayers(list[i]);
            server.addPlayerInMatch(list[i].getNickname(), match.getMatchID());
        }
        bubbleSort(list);
        match.initialize(list);
    }

    /**
     * THe method will order the list of players based on their birth date
     * @param list is the reference to the list of players that has to be ordered by the method
     */
    private void bubbleSort(Player[] list){
        boolean ended = false;
        Player temp;
        int index = list.length - 1;
        if(list.length == 1) return;
        while(!ended){
            ended = true;
            for(int i = 0; i < index; ++i){
                if(list[i].getBirthDate().compareTo(list[i+1].getBirthDate()) < 0) {
                    temp = list[i];
                    list[i] = list[i+1];
                    list[i+1] = temp;
                    ended = false;
                }
            }
            --index;
        }
    }

    /**
     * The method receives messages from the VirtualView and distributes them by their First Header
     * @param view is the view observed by the controller
     * @param mes is the message that has been received, is received has an Object.
     */
    @Override
    public void update(Observable view, Object mes) {
        Message message = (Message) mes;
        try{
            switch(message.getFirstLevelHeader()){
                case SYNCHRONIZATION:
                    synchronizationHandler(message);
                    break;
                case VERIFY:
                    validationHandler(message);
                    break;
                case DO:
                    doHandler(message);
                    break;
                default:
                    throw new UnexpectedMessageException();
            }
        } catch(UnexpectedMessageException e) {
            System.out.println("unexpected message");
        }
    }

    /**
     * The method handles all the messages that have DO as First Header, messages that in fact represents an action specified by the Second Header
     * @param message is the message that has to be handled by the method
     */
    public void doHandler(Message message) {
        //System.out.println(message.getUsername() + message.getFirstLevelHeader() + message.getSecondLevelHeader());
        switch(message.getSecondLevelHeader()){
            case NEXT_PHASE:
                turnManager.handlePhases(match, message.getUsername());
                break;
            case ACTIVATE_GOD:
                turnManager.requestManager(ActionType.ACTIVATE_GOD, match, message.getUsername(), message); // c'è da aggiungere il payload
                break;
            case SELECT_PARAMETERS:
                turnManager.requestManager(ActionType.SELECT_PARAMETERS, match, message.getUsername(), message);
                break;
            case SELECT_BUILDER:
                SelectedBuilderMessage selectedBuilderMessage = message.deserializeSelectedBuilderMessage();
                if(selectedBuilderMessage.getGender() == 'M')
                    match.getPlayerByName(message.getUsername()).setPlayingBuilder(
                            match.getPlayerByName(message.getUsername()).getBuilderM());
                else
                    match.getPlayerByName(message.getUsername()).setPlayingBuilder(
                            match.getPlayerByName(message.getUsername()).getBuilderF());
                turnManager.handlePhases(match, message.getUsername()); // c'è da aggiungere il payload
                break;
            case SELECT_CELL_MOVE:
                turnManager.requestManager(ActionType.SELECT_CELL_MOVE, match, message.getUsername(), message);
                break;
        }
    }

    /**
     * The method handles messages that have SYNCHRONIZATION as First Header, messages that are used to start and setup the match
     * @param message is the message that has to be handled by the method
     */
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
                        orderMessage.get(i).buildTurnPlayerMessage(new TurnPlayerMessage(players[match.getCurrentPlayerIndex()], match.getBoard().getBoard()));
                    }
                    match.notifyView(orderMessage);
                }
                break;
            default:
                break;
        }
    }

    /**
     * the method handles messages that have VERIFY as First Header by their Second Header
     * CORRECT_SELECTION_POS: messages that are used to verify if the player selected correct position for his builder during Setup
     * @param message the message that has to be handled
     */
    public void validationHandler(Message message){
        switch(message.getSecondLevelHeader()){
            case CORRECT_SELECTION_POS:
                SelectedBuilderPositionMessage selectedBuilderPositionMessage = message.deserializeSelectedBuilderPosMessage(message.getSerializedPayload());
                Cell[][] board = match.getBoard().getBoard();
                boolean builderFToChange, builderMToChange;
                builderFToChange = true;
                builderMToChange = true;
                if(selectedBuilderPositionMessage.getBuilderF() != null && board[selectedBuilderPositionMessage.getBuilderF()[0]][selectedBuilderPositionMessage.getBuilderF()[1]].getStatus() == AccessType.FREE) builderFToChange = false;
                if(selectedBuilderPositionMessage.getBuilderM() != null && board[selectedBuilderPositionMessage.getBuilderM()[0]][selectedBuilderPositionMessage.getBuilderM()[1]].getStatus() == AccessType.FREE) builderMToChange = false;

                if(!builderFToChange){
                    board[selectedBuilderPositionMessage.getBuilderF()[0]][selectedBuilderPositionMessage.getBuilderF()[1]].
                            setStatus(AccessType.OCCUPIED);
                    match.getPlayerByName(selectedBuilderPositionMessage.getUsername()).
                            setBuilderF(new Builder(
                                    match.getPlayerByName(selectedBuilderPositionMessage.getUsername()),'\u2640', match.getBoard(), selectedBuilderPositionMessage.getBuilderF())); //♚
                    board[selectedBuilderPositionMessage.getBuilderF()[0]][selectedBuilderPositionMessage.getBuilderF()[1]].
                            setBuilder(match.getPlayerByName(selectedBuilderPositionMessage.getUsername()).getBuilderF());
                }

                if(!builderMToChange){
                    board[selectedBuilderPositionMessage.getBuilderM()[0]][selectedBuilderPositionMessage.getBuilderM()[1]].
                            setStatus(AccessType.OCCUPIED);
                    match.getPlayerByName(selectedBuilderPositionMessage.getUsername()).
                            setBuilderM(new Builder(match.getPlayerByName(selectedBuilderPositionMessage.getUsername()),'\u2642', match.getBoard(), selectedBuilderPositionMessage.getBuilderM())); // ♛
                    board[selectedBuilderPositionMessage.getBuilderM()[0]][selectedBuilderPositionMessage.getBuilderM()[1]].
                            setBuilder(match.getPlayerByName(selectedBuilderPositionMessage.getUsername()).getBuilderM());
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
                        match.setCurrentPlayerIndex(0);
                        turnManager.handlePhases(match, null);
                    } else {
                        ArrayList<Message> orderMessage = new ArrayList<>();
                        for (int i = 0; i < match.getNumberOfPlayers(); ++i) {
                            orderMessage.add(new Message(playingPlayers[i].getNickname()));
                            orderMessage.get(i).buildTurnPlayerMessage(new TurnPlayerMessage(match.getCurrentPlayer(), match.getBoard().getBoard()));
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

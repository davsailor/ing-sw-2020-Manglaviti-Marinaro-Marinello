package it.polimi.ingsw2020.santorini.model;

import it.polimi.ingsw2020.santorini.controller.GameLogic;
import it.polimi.ingsw2020.santorini.utils.PhaseType;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("deprecation")

public class Match extends Observable {
    private Player[] players;
    private int currentPlayerIndex;
    private int numberOfPlayers;
    private int eliminatedPlayer;
    private Board board;
    private int numberOfCompletedTowers;
    private int turnNumber;
    private PhaseType turnPhase;
    private ArrayList<Observer> observers;

    public Match (Board board, int numberOfPlayers, GameLogic controller) {
        this.players = new Player[numberOfPlayers];
        this.numberOfPlayers = numberOfPlayers;
        this.currentPlayerIndex = 0;
        this.eliminatedPlayer = -1;
        this.board = board;
        this.numberOfCompletedTowers = 0;
        this.turnNumber = 0;
        this.turnPhase = null;
        this.observers = new ArrayList<>();
        this.observers.add(controller);
    }

    /**
     * it returns the current player
     * @return value of the attribute currentPlayer
     */
    public Player getCurrentPlayer(){
        return (this.players[this.currentPlayerIndex]);
    }

    /**
     * it sets the current player to the following player
     */
    public void nextCurrentPlayer(){
        this.currentPlayerIndex = this.currentPlayerIndex +1;
    }

    /**
     * setter of the attribute currentPlayerIndex
     * @param currentPlayerIndex is an int value that will be the value of this attribute
     */
    public void setCurrentPlayerIndex(int currentPlayerIndex){
        this.currentPlayerIndex = currentPlayerIndex;
    }

    /**
     * it adds the new players to the list of players
     * @param player is the variable that represents a player
     */
    public void addPlayer(Player player){
        int k = 0;//flag
        for( int i = 0; i < this.numberOfPlayers; i++ ){
            if (this.players[i] == null){
                this.players[i] = player;
                k = 1;
            }
        }
    }

    /**
     * getter of the attribute numberOfCompletedTowers
     * @return the int value of that attribute
     */
    public int getNumberOfCompletedTowers(){
        return(this.numberOfCompletedTowers);
    }
    /**
     * It increases the number of completed towers by one unit
     */
    public void addNumberOfCompletedTowers(){
        this.numberOfCompletedTowers = this.numberOfCompletedTowers +1;
    }

    /**
     * It decreases the number of completed towers by one unit
     */
    public void decreaseNumberOfCompletedTowers(){
        this.numberOfCompletedTowers = this.numberOfCompletedTowers - 1;
    }

    /**
     * setter of the attribute turnPhase
     * @param turnPhase is the variable that represents a phase
     */
    public void setTurnPhase (PhaseType turnPhase ){
        this.turnPhase = turnPhase;
    }

    /**
     * getter of the attribute turnPhase
     * @return the value of turnPhase
     */
    public PhaseType getTurnPhase(){
        return(this.turnPhase);
    }

    /**
     * It increases the number of turn by one unit
     */
    public void nextTurnNumber(){
        this.turnNumber = this.turnNumber +1;
    }

    /**
     * setter of the attribute turnNumber
     * @param turnNumber will be the int value of this attribute
     */
    public void setTurnNumber(int turnNumber){
        this.turnNumber = turnNumber;
    }

    /**
     * getter of the attribute turnNumber
     * @return the int value of this attribute
     */
    public int getTurnNumber(){
        return(this.turnNumber);
    }

    /**
     * setter of the attribute eliminatedPlayer
     * @param eliminatedPlayer will be the int value of this attribute
     */
    public void setEliminatedPlayer(int eliminatedPlayer){
        this.eliminatedPlayer = eliminatedPlayer;
    }

    /**
     * getter of the attribute eliminatedPlayer
     * @return the int value that represent the index of the player array
     */
    public int getEliminatedPlayer (){
        return(this.eliminatedPlayer);
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public ArrayList<Observer> getObservers() {
        return observers;
    }

    public void setObservers(ArrayList<Observer> observers) {
        this.observers = observers;
    }
}

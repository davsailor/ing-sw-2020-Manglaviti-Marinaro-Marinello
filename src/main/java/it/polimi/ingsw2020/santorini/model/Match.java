package it.polimi.ingsw2020.santorini.model;

import it.polimi.ingsw2020.santorini.network.server.VirtualView;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;
import it.polimi.ingsw2020.santorini.utils.Color;
import it.polimi.ingsw2020.santorini.utils.messages.MatchSetupMessage;

import java.util.ArrayList;
import java.util.Observable;

@SuppressWarnings("deprecation")

public class Match extends Observable {
    private final int matchID;
    private Player[] players;
    private int currentPlayerIndex;
    private int numberOfPlayers;
    private int eliminatedPlayer;
    private Board board;
    private int numberOfCompletedTowers;
    private int turnNumber;
    private PhaseType turnPhase;

    public Match (Board board, int numberOfPlayers, VirtualView view) {
        this.matchID = view.getServer().generateMatchID();
        this.players = new Player[numberOfPlayers];
        this.numberOfPlayers = numberOfPlayers;
        this.currentPlayerIndex = 0;
        this.eliminatedPlayer = -1;
        this.board = board;
        this.numberOfCompletedTowers = 0;
        this.turnNumber = 0;
        this.turnPhase = null;
        addObserver(view);
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

    public void setNumberOfCompletedTowers(int numberOfCompletedTowers) {
        this.numberOfCompletedTowers = numberOfCompletedTowers;
    }

    public Player[] getPlayers() {
        Player[] playerCpy = new Player[players.length];
        for(int i = 0; i < players.length; ++i)
            playerCpy[i] = players[i];
        return playerCpy;
    }

    public void initialize(Player[] players) {
        ArrayList<Message> listOfMessages = new ArrayList<>();
        this.getBoard().getGodCards().shuffleDeck();
        for(int i = 0; i < this.getNumberOfPlayers(); ++i){
            this.players[i] = players[i];
            this.players[i].setGod(this.getBoard().getGodCards().giveCard());
            this.players[i].setColor(Color.getColor(i));
            listOfMessages.add((new Message(players[i].getNickname())));
        }
        for(int i = 0; i < this.getNumberOfPlayers(); ++i){
            listOfMessages.get(i).buildMatchSetupMessage(new MatchSetupMessage(getPlayers(), getBoard().getBoard()));
        }
        setChanged();
        notifyObservers(listOfMessages);
    }

    public int getMatchID() {
        return matchID;
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

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    @Override
    public boolean equals(Object obj){
        Match match = (Match) obj;
        return this.matchID == match.getMatchID();
    }

}

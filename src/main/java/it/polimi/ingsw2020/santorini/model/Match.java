package it.polimi.ingsw2020.santorini.model;

public class Match {
    private Player[] players;
    private int currentPlayerIndex;
    private int numberOfPlayers;
    private int eliminatedPlayer;
    private Board board;
    private int numberOfCompletedTowers;
    private int turnNumber;
    private TimingType turnPhase;

    public Match (Board board, int numberOfPlayers) {
        this.players = new Player[numberOfPlayers];
        this.numberOfPlayers = numberOfPlayers;
        this.currentPlayerIndex = 0;
        this.eliminatedPlayer = -1;
        this.board = board;
        this.numberOfCompletedTowers = 0;
        this.turnNumber = 0;
        this.turnPhase = null;
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
    public void setTurnPhase (TimingType turnPhase ){
        this.turnPhase = turnPhase;
    }

    /**
     * getter of the attribute turnPhase
     * @return the value of turnPhase
     */
    public TimingType getTurnPhase(){
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



}

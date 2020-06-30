package it.polimi.ingsw2020.santorini.model;

import it.polimi.ingsw2020.santorini.exceptions.EndMatchException;
import it.polimi.ingsw2020.santorini.model.gods.*;
import it.polimi.ingsw2020.santorini.network.server.Server;
import it.polimi.ingsw2020.santorini.network.server.VirtualView;
import it.polimi.ingsw2020.santorini.utils.AccessType;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.Color;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.*;

import java.util.ArrayList;
import java.util.Observable;

@SuppressWarnings("deprecation")

public class Match extends Observable {
    private final int matchID;
    private ArrayList<Player> players;
    private int currentPlayerIndex;
    private final int numberOfPlayers;
    private ArrayList<Player> eliminatedPlayers;
    private ArrayList<Integer> remainingGods;
    private Board board;
    private int numberOfCompletedTowers;
    private int turnNumber;

    public Match (Board board, int numberOfPlayers, VirtualView view) {
        this.matchID = view.getServer().generateMatchID();
        this.players = new ArrayList<>();
        this.numberOfPlayers = numberOfPlayers;
        this.currentPlayerIndex = 0;
        this.eliminatedPlayers = new ArrayList<>();
        this.board = board;
        this.numberOfCompletedTowers = 0;
        this.turnNumber = 0;
        remainingGods = null;
        addObserver(view);
        view.setMatch(this);
    }

    public ArrayList<Integer> getRemainingGods() {
        return remainingGods;
    }

    public void setRemainingGods(ArrayList<Integer> remainingGods) {
        this.remainingGods = remainingGods;
    }

    /**
     * it returns the current player
     * @return value of the attribute currentPlayer
     */
    public Player getCurrentPlayer(){
        return (this.players.get(currentPlayerIndex));
    }

    /**
     * setter of the attribute currentPlayerIndex
     * @param currentPlayerIndex is an int value that will be the value of this attribute
     */
    public void setCurrentPlayerIndex(int currentPlayerIndex){
        this.currentPlayerIndex = currentPlayerIndex;
    }

    /**
     * method that finds a player in the match using the corresponding username
     * @param username the username of the player we are looking for
     * @return the player we are looking for
     */
    public Player getPlayerByName(String username){
        for(int i = 0; i < numberOfPlayers; ++i){
            if(players.get(i).getNickname().equals(username)) return players.get(i);
        }
        return null;
    }

    /**
     * getter of eliminated player
     * @return an arraylist containing all eliminated players
     */
    public ArrayList<Player> getEliminatedPlayers() {
        return eliminatedPlayers;
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
     * It increases the number of turn by one unit
     */
    public void nextTurnNumber(){
        this.turnNumber = this.turnNumber +1;
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
    public void setEliminatedPlayer(int eliminatedPlayer) throws EndMatchException{
        if(currentPlayerIndex > eliminatedPlayer)
            --currentPlayerIndex;
        else if(currentPlayerIndex == eliminatedPlayer) {
            setNextPlayer();
            if(currentPlayerIndex > eliminatedPlayer)
                --currentPlayerIndex;
        }
        eliminatedPlayers.add(players.get(eliminatedPlayer));
        if(eliminatedPlayers.size() == numberOfPlayers - 1) {
            players.remove(players.get(eliminatedPlayer));
            throw new EndMatchException(this);
        }
        else
            for (int i = 1; i < 6; ++i)
                for (int j = 1; j < 6; ++j)
                    if (board.getBoard()[i][j].getBuilder() != null && board.getBoard()[i][j].getBuilder().getColor() == getPlayers()[eliminatedPlayer].getColor()) {
                        board.getBoard()[i][j].setBuilder(null);
                        board.getBoard()[i][j].setStatus(AccessType.FREE);
                    }
        players.remove(players.get(eliminatedPlayer));
    }

    /**
     * setter of number of completed towers
     * @param numberOfCompletedTowers the number to set
     */
    public void setNumberOfCompletedTowers(int numberOfCompletedTowers) {
        this.numberOfCompletedTowers = numberOfCompletedTowers;
    }

    /**
     * getter of players
     * @return a shadow copy of player using an array
     */
    public Player[] getPlayers() {
        Player[] playerCpy = new Player[players.size()];
        for(int i = 0; i < players.size(); ++i)
            playerCpy[i] = players.get(i);
        return playerCpy;
    }

    public ArrayList<Player> getPlayersAsList(){
        return players;
    }

    /**
     * method that creates the match
     * @param players players of the match
     */
    public void initialize(Player[] players) {
        ArrayList<Message> listOfMessages = new ArrayList<>();
        for(int i = 0; i < this.getNumberOfPlayers(); ++i){
            this.players.add(players[i]);
            this.players.get(i).setColor(Color.getColor(i));
            listOfMessages.add((new Message(players[i].getNickname())));
        }
        for(int i = 0; i < this.getPlayers().length; ++i)
            listOfMessages.get(i).buildMatchSetupMessage(new MatchSetupMessage(this, null));
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

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     * method that tells us if two matches are equals, using their matchID
     * @param obj the object to test
     * @return the result of the comparison
     */
    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof Match)) return false;
        Match match = (Match) obj;
        return this.matchID == match.getMatchID();
    }

    /**
     * method used to notify clients, using the virtual view
     * @param list the list of messages to send
     */
    public void notifyView(ArrayList<Message> list){
        setChanged();
        notifyObservers(list);
    }

    /**
     * method that calculates the next player
     */
    public void setNextPlayer() {
        if (getCurrentPlayerIndex() == players.size() - 1) setCurrentPlayerIndex(0);
        else setCurrentPlayerIndex(getCurrentPlayerIndex() + 1);
    }

    /**
     * method that complete all the operations required for an instant win
     * @throws EndMatchException exception that will tell us the match is ended
     */
    public void currentWins() throws EndMatchException{
        for(int i = 0; i < getPlayers().length; ++i)
            if(!getPlayers()[i].getNickname().equals(getCurrentPlayer().getNickname())) {
                setEliminatedPlayer(i);
                i = -1;
            }
    }

    /**
     * method that complete all the operations required to end a match
     * @param server the server where the match is hosted
     */
    public void notifyEndMatch(Server server) {
        ArrayList<Message> endMatchMessages = new ArrayList<>();
        Message winner = new Message(getPlayers()[0].getNickname());
        winner.buildEndMatchMessage(new EndMatchMessage(getPlayers()[0].getNickname()));
        endMatchMessages.add(winner);
        server.getPlayerInMatch().remove(getPlayers()[0].getNickname());
        for(Player p: getEliminatedPlayers()){
            if(server.getVirtualClients().containsKey(p.getNickname())) {
                Message loser = new Message(p.getNickname());
                loser.buildEndMatchMessage(new EndMatchMessage(getPlayers()[0].getNickname()));
                endMatchMessages.add(loser);
            }
            server.getPlayerInMatch().remove(p.getNickname());
        }
        server.getVirtualViews().remove(getMatchID());
        server.getControllers().remove(getMatchID());
        notifyView(endMatchMessages);
    }
}

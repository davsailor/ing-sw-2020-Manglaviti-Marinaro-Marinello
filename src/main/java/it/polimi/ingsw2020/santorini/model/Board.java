package it.polimi.ingsw2020.santorini.model;

import java.util.List;

public class Board {
    private Cell[][] board;
    private List<Player> players;
    private GodDeck godCards;
    private Player currentPlayer;
    private int numberOfCompletedTowers;

    /**
     * it is the constructor of the class
     */
    public Board() {
        this.board = new Cell[7][7];
        //this.godCards = new GodDeck;
        this.numberOfCompletedTowers = 0;
        this.currentPlayer = null;
        //initialization of the cells
        for(int i = 1; i< 6; i++){ //i riga
            for(int j= 1 ; j < 6; j++){
                board[i][j] = new Cell(AccessType.Free);
            }
        }
        //end
        //delimitation of borders
        for( int j = 0; j < 7; j++ ){
            board[0][j] = new Cell(AccessType.Forbidden);
            board[6][j] = new Cell(AccessType.Forbidden);
            board[j][0] = new Cell(AccessType.Forbidden);
            board[j][6] = new Cell(AccessType.Forbidden);
        }
    }

    /**
     * it returns the current player
     * @return value of the attribute currentPlayer
     */
    public Player getCurrentPlayer(){
        return (this.currentPlayer);
    }

    /**
     * it sets the current player
     * @param currentPlayer is the currentPlayer that will be set
     */
    public void setCurrentPlayer(Player currentPlayer){
        this.currentPlayer = currentPlayer;
    }

    /**
     * it creates the stream of the board to be used by the view
     */
    public void showBoard(){}

    /**
     * it adds the new players to the list of players
     * @param player is the variable that represents a player
     */
    public void addPlayer(Player player){
        this.players.add(this.players.size(),player);
    }

    /**
     * it return the references of the cells neighboring the main cell
     * @param cell is the main cell
     * @return the reference of the cells
     */
    public Cell[][] neighboringCell(Cell cell){
        return null;
    }
}

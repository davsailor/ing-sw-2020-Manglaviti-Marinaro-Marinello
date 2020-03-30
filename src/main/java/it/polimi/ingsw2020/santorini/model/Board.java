package it.polimi.ingsw2020.santorini.model;

import org.graalvm.compiler.nodes.memory.Access;

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
                board[i][j] = new Cell(AccessType.FREE);
            }
        }
        //end
        //delimitation of borders
        for( int j = 0; j < 7; j++ ){
            board[0][j] = new Cell(AccessType.FORBIDDEN);
            board[6][j] = new Cell(AccessType.FORBIDDEN);
            board[j][0] = new Cell(AccessType.FORBIDDEN);
            board[j][6] = new Cell(AccessType.FORBIDDEN);
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
     * it build a matrix of 9 elements that shows the cells with status equals to target
     * in particular te cell value will be 0 if the corresponding status's cell doesn't match target
     * 1 if they matches and the cell(intX, intY).getLevel is equal to the one's neighboring cells have
     * 2 if they matches and the cell(intX, intY).getLevel.getHeight is inferior by 1 to the one's neighboring cells have
     * 3 if they matches and the cell(intX, intY).getLevel.getHeight is bigger by 2 to the one's neighboring cells have
     * @param intX is the coordinate of row
     * @param intY is the coordinate of column
     * @param target represent which kind of cell it is searching for
     * @return the references to a matrix of int
     */
    public int [][] neighboringStatusCell(int intX, int intY, AccessType target){
        int[][] neighborMatrix;
        neighborMatrix = new int[3][3];
        int k;
        for(int i = 0; i < 3; i++){//For of the row
            for (int j = 0; j < 3 ; j++){//For of the column
                if ((i == 1) && (j==1)){//if that checks the coordinates correspond to the center of the matrix
                    neighborMatrix[i][j] = 0;
                }
                else{//Analise the other cells of the matrix board
                    if(board[intX-1+i][intY-1+j].getStatus().equals(target)){//checks if the cells'status correspond to target
                        k = board[intX][intY].calculateJump(board[intX-1+i][intY-1+j]);
                        if((k == 0) || (k == -1)){//case with same height or drop from one block
                            neighborMatrix[i][j] = 1;
                        }
                        else if (k == 1){//case that implies a rise(1 block)
                            neighborMatrix[i][j] = 2;
                        }
                        else if (k < -1){//case that implies a drop from two blocks or more
                            neighborMatrix[i][j] = 3;
                        }
                        else{//case that implies a rise of two blocks or more
                            neighborMatrix[i][j] = 0;
                        }
                    }
                    else{//status and target are different
                        neighborMatrix[i][j] = 0;
                    }
                }
            }
        }
        return neighborMatrix;
    }

    public int[][] neighborLevelCell(int posX, int posY){
        int[][] neighborMatrix;
        neighborMatrix = new int[3][3];
        for(int i = 0; i < 3; i++){//For of the row
            for (int j = 0; j < 3 ; j++){//For of the column
                neighborMatrix[i][j] = board[posX-1+i][posY-1+j].getLevel().getHeight();
            }
        }
        return neighborMatrix;
    }

    public void buildBlock (int buildX, int buildY, LevelType block){
        board[buildX][buildY].setLevel(block);
    }
}

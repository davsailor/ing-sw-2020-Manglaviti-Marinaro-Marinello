package it.polimi.ingsw2020.santorini.model;

import it.polimi.ingsw2020.santorini.utils.AccessType;
import it.polimi.ingsw2020.santorini.utils.Color;
import it.polimi.ingsw2020.santorini.utils.LevelType;

import static it.polimi.ingsw2020.santorini.utils.AccessType.OCCUPIED;

public class Board {
    private Cell[][] board;
    private transient GodDeck godCards;

    /**
     * it is the constructor of the class
     */
    public Board(GodDeck godCards) {
        this.board = new Cell[7][7];
        this.godCards = godCards;
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

    // da modificare perchè così è terribile
    public Cell[][] getBoard() {
        return board;
        /*
        Cell[][] boardCpy = new Cell[7][7];
        for(int i = 0; i < 7; ++i)
            System.arraycopy(board[i], 0, boardCpy[i], 0, 7);
        return boardCpy;
         */
    }


    public GodDeck getGodCards() {
        return godCards;
    }

    /**
     * it build a matrix of 9 elements that shows the cells with status equals to target
     * in particular te cell value will be
     * 0 if the corresponding status's cell doesn't match target
     * 1 if they matches and the cell(intX, intY).getLevel is equal to the one's neighboring cells have
     * 2 if they matches and the cell(intX, intY).getLevel.getHeight is inferior by 1 to the one's neighboring cells have
     * 3 if they matches and the cell(intX, intY).getLevel.getHeight is bigger by 2 to the one's neighboring cells have
     * @param currentBuilder is the Builder that is in the center of the neighboringStatusCell
     * @param target represent which kind of cell it is searching for
     * @return the references to a matrix of int
     */
    public static int [][] neighboringStatusCell(Builder currentBuilder,  AccessType target){
        int[][] neighborMatrix;
        neighborMatrix = new int[3][3];
        int k;
        for(int i = 0; i < 3; i++){//For of the row
            for (int j = 0; j < 3 ; j++){//For of the column
                if ((i == 1) && (j==1)){//if that checks the coordinates correspond to the center of the matrix
                    neighborMatrix[i][j] = 0;
                }
                else{//Analise the other cells of the matrix board
                    if(currentBuilder.getBoard().getBoard()[currentBuilder.getPosX()-1+i][currentBuilder.getPosY()-1+j].getStatus().equals(target)){//checks if the cells'status correspond to target
                        k = currentBuilder.getBoard().getBoard()[currentBuilder.getPosX()][currentBuilder.getPosY()].calculateJump(currentBuilder.getBoard().getBoard()[currentBuilder.getPosX()-1+i][currentBuilder.getPosY()-1+j]);
                        if((k == 0) || (k == -1)){//case with same height or drop from one block
                            if(currentBuilder.getPlayer().getMoveActions()) {neighborMatrix[i][j] = 1;}
                            else{neighborMatrix[i][j] = 0;}
                        }
                        else if (k == 1){//case that implies a rise(1 block)
                            if(currentBuilder.getPlayer().getRiseActions()) {neighborMatrix[i][j] = 2;}
                            else{neighborMatrix[i][j] = 0;}//ACCURA
                        }
                        else if (k < -1){//case that implies a drop from two blocks or more
                            if(currentBuilder.getPlayer().getMoveActions()) {neighborMatrix[i][j] = 3;}
                            else{neighborMatrix[i][j] = 0;}
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

    /**
     * it builds and return a matrix of nine int, with each representing the height of the buildings neighboring the
     * with coordinates posX and posY
     * @param currentBuilder is the Builder that is in the center of the neighboringStatusCell
     * @return the matrix build within the function
     */
    public static int[][] neighboringLevelCell(Builder currentBuilder) throws IllegalArgumentException{
        if (((currentBuilder.getPosX() == 0) || (currentBuilder.getPosX() == 6)) || (currentBuilder.getPosY() == 0) || currentBuilder.getPosY() == 6){
            throw new IllegalArgumentException();
        }
        else{
            int[][] neighborMatrix;
            neighborMatrix = new int[3][3];
            for(int i = 0; i < 3; i++){//For of the row
                for (int j = 0; j < 3 ; j++){//For of the column
                    neighborMatrix[i][j] = currentBuilder.getBoard().getBoard()[currentBuilder.getPosX()-1+i][currentBuilder.getPosY()-1+j].getLevel().getHeight();
                }
            }
            return neighborMatrix;
        }
    }

    public static int[][] neighboringColorCell(Builder currentBuilder){
        int[][] neighborMatrix;
        neighborMatrix = new int[3][3];
        for (int i= 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (currentBuilder.getBoard().getBoard()[currentBuilder.getPosX()-1+i][currentBuilder.getPosY()-1+j].getStatus() == OCCUPIED){
                    if(currentBuilder.getBoard().getBoard()[currentBuilder.getPosX()-1+i][currentBuilder.getPosX()-1+j].getBuilder().getColor() != currentBuilder.getColor()){
                        neighborMatrix[i][j] = 1;
                    }
                    else{
                        neighborMatrix[i][j] = 0;
                    }
                }
                else{
                    neighborMatrix[i][j] = 0;
                }
            }
        }
        return  neighborMatrix;
    }

    /**
     * it builds a block of building on the top of the cell
     * @param buildX is row coordinate of ethe cell where the block will be placed
     * @param buildY is column coordinate of ethe cell where the block will be placed
     * @param block is the type/eight of the block that will be be build on the top of the cell
     */
    public void buildBlock (int buildX, int buildY, LevelType block){
        board[buildX][buildY].setLevel(block);
    }

    public static int[][] neighboringSwappingCell(Builder builder, AccessType target){
        int[][] neighborMatrix = new int[3][3];
        int[][] neighborMatrix1 = new int[3][3];
        int[][] neighborMatrix2 = new int[3][3];
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                neighborMatrix1[i][j] = neighboringColorCell(builder)[i][j];
                neighborMatrix2[i][j] = neighboringColorCell(builder)[i][j];
                neighborMatrix[i][j]  = neighborMatrix1[i][j] * neighborMatrix2[i][j];
            }
        }
        return neighborMatrix;
    }
}


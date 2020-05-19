package it.polimi.ingsw2020.santorini.model;

import it.polimi.ingsw2020.santorini.exceptions.EndMatchException;
import it.polimi.ingsw2020.santorini.exceptions.IllegalConstructionException;
import it.polimi.ingsw2020.santorini.exceptions.IllegalMovementException;
import it.polimi.ingsw2020.santorini.utils.AccessType;
import it.polimi.ingsw2020.santorini.utils.Color;
import it.polimi.ingsw2020.santorini.utils.Direction;
import it.polimi.ingsw2020.santorini.utils.LevelType;


public class Builder {
    private Color color;
    private char gender;
    private transient Player player;
    private transient Board board;
    private int posX;
    private int posY;
    private int buildPosX;
    private int buildPosY;
    private int[][] possibleMoves;
    private int[][] possibleBuildings;
    private boolean movedThisTurn;
    private boolean risedThisTurn;
    private boolean builtThisTurn;


    /*
     * getter and setters
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    public Board getBoard() {
        return board;
    }

    public Color getColor() {
        return color;
    }

    public char getGender() {
        return gender;
    }

    public Player getPlayer() {
        return player;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getBuildPosX() {
        return buildPosX;
    }

    public int getBuildPosY() {
        return buildPosY;
    }

    public void setRisedThisTurn(boolean risedThisTurn) {
        this.risedThisTurn = risedThisTurn;
    }

    public void setPossibleMoves() {
        this.possibleMoves = Board.neighboringStatusCell( this , AccessType.FREE);
    }

    public int[][] getPossibleMoves() {
        return possibleMoves;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setPossibleBuildings() {
        possibleBuildings = Board.neighboringLevelCell(this);
    }

    public int[][] getPossibleBuildings() {
        return possibleBuildings;
    }

    public boolean isMovedThisTurn() {
        return movedThisTurn;
    }

    public boolean isRisedThisTurn() {
        return risedThisTurn;
    }

    public boolean isBuiltThisTurn() {
        return builtThisTurn;
    }

    /**
     * constructor of the class
     * @param player is the player of the builder
     * @param gender is the gender of the builder
     */
    public Builder(Player player, char gender, Board board, int[] pos) {
        // if for test purpose
        if(player != null)
            this.color = player.getColor();
        else
            this.color = null;
        this.gender = gender;
        this.player = player;

        this.board= board;
        // if for test purpose
        if(pos != null){
            this.posX=pos[0];
            this.posY=pos[1];
            this.buildPosX=pos[0];
            this.buildPosY=pos[1];
            board.getBoard()[pos[0]][pos[1]].setStatus(AccessType.OCCUPIED);
        } else {
            this.posX = 3;
            this.posY = 3;
            this.buildPosX = 3;
            this.buildPosY = 3;
        }

        this.possibleMoves =new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.possibleMoves[i][j] = 1;
            }
        }
        this.possibleBuildings= new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.possibleBuildings[i][j] = 0;
            }
        }
        movedThisTurn = false;
        risedThisTurn = false;
        builtThisTurn = false;
    }

    /**
     * method used to swap two builders
     * @param b1 is the builder that i have to swap
     */
    public void swapBuilders(Builder b1){
        int tempX;
        int tempY;
        tempX=this.getPosX();
        tempY=this.getPosY();
        this.setPosX(b1.getPosX());
        this.setPosY(b1.getPosY());
        b1.setPosX(tempX);
        b1.setPosY(tempY);
        board.getBoard()[posX][posY].setBuilder(this);
        board.getBoard()[b1.getPosX()][b1.getPosY()].setBuilder(b1);
    }

    /**
     * method that evaluates the possibility of moving of the builder
     * @return true if the builder can perform a move action, false otherwise
     */
    public boolean canMove(){
        setPossibleMoves();
        for(int i = 0; i < 3; ++i)
            for(int j = 0; j < 3; ++j)
                if(possibleMoves[i][j] == 1 && player.getMoveActions()) return true;
                else if(possibleMoves[i][j] == 2 && player.getRiseActions()) return true;
                else if(possibleMoves[i][j] == 3 && player.getMoveActions() && !(!player.getMoveActions() && player.getRiseActions())) return true;
        return false;
    }

    /**
     * method that evaluates the possibility of building of the builder
     * @return true if the builder can perform a build action, false otherwise
     */
    public boolean canBuild(){
        setPossibleBuildings();
        for(int i = 0; i < 3; ++i)
            for(int j = 0; j < 3; ++j)
                if(possibleBuildings[i][j] < 4 && possibleBuildings[i][j] != -1) return true;
        return false;
    }

    /**
     * method that performs the move action
     * @param direction is the direction in which the player wants to move
     */
    public void move(Direction direction) throws IllegalMovementException, EndMatchException {
        if(direction == null) throw new IllegalMovementException("Something went wrong with your choice, please select another movement");
        board.getBoard()[posX][posY].setBuilder(null);
        board.getBoard()[posX][posY].setStatus(AccessType.FREE);
        int oldPosX = posX;
        int oldPosY = posY;
        switch (direction) {
            case NORTH_WEST:
                this.posX=posX-1;
                this.posY=posY-1;
                setPossibleMoves();
                possibleMoves[2][2] = 4;
                break;
            case NORTH:
                this.posX=posX-1;
                setPossibleMoves();
                possibleMoves[2][1] = 4;
                break;
            case NORTH_EAST:
                this.posX=posX-1;
                this.posY=posY+1;
                setPossibleMoves();
                possibleMoves[2][0] = 4;
                break;
            case WEST:
                this.posY=posY-1;
                setPossibleMoves();
                possibleMoves[1][2] = 4;
                break;
            case EAST:
                this.posY=posY+1;
                setPossibleMoves();
                possibleMoves[1][0]=4;
                break;
            case SOUTH_WEST:
                this.posX=posX+1;
                this.posY=posY-1;
                setPossibleMoves();
                possibleMoves[0][2]=4;
                break;
            case SOUTH:
                this.posX=posX+1;
                setPossibleMoves();
                possibleMoves[0][1]=4;
                break;
            case SOUTH_EAST:
                this.posX=posX+1;
                this.posY=posY+1;
                setPossibleMoves();
                possibleMoves[0][0]=4;
                break;
        }
        if(board.getBoard()[oldPosX][oldPosY].calculateJump(board.getBoard()[posX][posY]) > 0)
            risedThisTurn = true;

        board.getBoard()[posX][posY].setBuilder(this);
        board.getBoard()[posX][posY].setStatus(AccessType.OCCUPIED);
        if(board.getBoard()[oldPosX][oldPosY].getLevel() != LevelType.TOP && board.getBoard()[posX][posY].getLevel() == LevelType.TOP)
            throw new EndMatchException(null);
    }

    /**
     * method that performs the build action
     * @param direction is the direction in which the player wants to build
     * @param match the match in which the builder is playing
     */
    public void build(Direction direction, Match match) throws IllegalConstructionException{
        //LevelType level;
        setPossibleBuildings();
        int buildPosX, buildPosY;
        switch (direction) {
            case NORTH_WEST:
                buildPosX = this.posX - 1;
                buildPosY = this.posY - 1;
                break;
            case NORTH:
                buildPosX = this.posX - 1;
                buildPosY = this.posY;
                break;
            case NORTH_EAST:
                buildPosX = this.posX - 1;
                buildPosY = this.posY + 1;
                break;
            case WEST:
                buildPosX = this.posX;
                buildPosY = this.posY - 1;
                break;
            case EAST:
                buildPosX = this.posX;
                buildPosY = this.posY + 1;
                break;
            case SOUTH_WEST:
                buildPosX = this.posX + 1;
                buildPosY = this.posY - 1;
                break;
            case SOUTH:
                buildPosX = this.posX + 1;
                buildPosY = this.posY;
                break;
            case SOUTH_EAST:
                buildPosX = this.posX + 1;
                buildPosY = this.posY + 1;
                break;
            default:
                throw new IllegalConstructionException("Your direction does not exist!");
        }
        switch (possibleBuildings[buildPosX - this.posX + 1][buildPosY - this.posY + 1]) {
            case 0:
                board.buildBlock(buildPosX, buildPosY, LevelType.BASE);
                break;
            case 1:
                board.buildBlock(buildPosX, buildPosY, LevelType.MID);
                break;
            case 2:
                board.buildBlock(buildPosX, buildPosY, LevelType.TOP);
                break;
            case 3:
                board.buildBlock(buildPosX, buildPosY, LevelType.DOME);
                match.setNumberOfCompletedTowers(match.getNumberOfCompletedTowers() + 1);
                break;
            case -2:
                break;
            default:
                throw new IllegalConstructionException("Something went wrong while building, please select another one");
        }
        this.buildPosX = buildPosX;
        this.buildPosY = buildPosY;
        possibleBuildings[buildPosX - this.posX + 1][buildPosY - this.posY + 1] = -2;
    }
}
package it.polimi.ingsw2020.santorini.model;

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


    //METODI

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

    /**
     *
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

        // if for test purpose
        if(pos != null){
            this.posX=pos[0];
            this.posY=pos[1];
            this.buildPosX=pos[0];
            this.buildPosY=pos[1];
        } else {
            this.posX = 3;
            this.posY = 3;
            this.buildPosX = 3;
            this.buildPosY = 3;
        }

        this.board= board;
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
     *
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
    }

    public void setPossibleMoves() {
        this.possibleMoves = Board.neighboringStatusCell(board.getBoard(), posX, posY, AccessType.FREE);
    }


    public int[][] getPossibleMoves() {
        return possibleMoves;
    }

    /**
     *
     * @param direction is the direction in which the player wants to move
     */

    public void move(Direction direction) throws IllegalMovementException {
        if(direction == null) throw new IllegalMovementException();
        switch (direction) {
            case NORTH_WEST:
                this.posX=posX-1;
                this.posY=posY-1;
                setPossibleMoves();
                possibleMoves[2][2]=4;
                break;
            case NORTH:
                this.posX=posX-1;
                setPossibleMoves();
                possibleMoves[2][1]=4;
                break;
            case NORTH_EAST:
                this.posX=posX-1;
                this.posY=posY+1;
                setPossibleMoves();
                possibleMoves[2][0]=4;
                break;
            case WEST:
                this.posY=posY-1;
                setPossibleMoves();
                possibleMoves[1][2]=4;
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
            default:
                throw new IllegalMovementException();
        }
    }

    public void setPossibleBuildings() {
        possibleBuildings = Board.neighborLevelCell(board.getBoard(), posX, posY);
    }

    public int[][] getPossibleBuildings() {
        return possibleBuildings;
    }

    /**
     *
     * @param direction is the direction in which the player wants to build
     */

    public void build (Direction direction) throws IllegalConstructionException{
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
                throw new IllegalConstructionException();
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
                break;
            case -2:
                break;
            default:
                throw new IllegalConstructionException();
        }
        this.buildPosX = buildPosX;
        this.buildPosY = buildPosY;
        possibleBuildings[buildPosX - this.posX + 1][buildPosY - this.posY + 1] = -2;
    }
}
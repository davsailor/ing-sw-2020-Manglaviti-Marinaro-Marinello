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
    public int i=0,j=0;      // RICORDA DI VERIFICARE SE VANNO BENE PUBLIC
    private int[][] possibleMoves;
    private int[][] possibleBuildings;
    boolean movedThisTurn;
    boolean risedThisTurn;
    boolean builtThisTurn;


    //METODI

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
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

    public void setBuildPosX(int buildPosX) {
        this.buildPosX = buildPosX;
    }

    public int getBuildPosY() {
        return buildPosY;
    }

    public void setBuildPosY(int buildPosY) {
        this.buildPosY = buildPosY;
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
        int[][] possibleMoves =new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                possibleMoves[i][j] = 1;
            }
        }
        int[][] possibleBuildings= new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                possibleBuildings[i][j] = 0;
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

    /**
     *
     * @param posX is the row where the builder is
     * @param posY is the column where the builder is
     */

    public void setPossibleMoves(int posX, int posY) {
        this.possibleMoves = board.neighboringStatusCell(posX,posY, AccessType.FREE);
    }


    public int[][] getPossibleMoves() {
        return possibleMoves;
    }

    /**
     *
     * @param direction is the direction in which the player wants to move
     * @param posX is the row where the builder is before the movement
     * @param posY is the column where the builder is before the movement
     */

    public void move(Direction direction, int posX , int posY) throws IllegalMovementException {
        if(direction == null) throw new IllegalMovementException();
        switch (direction) {
            case NORTH_WEST:
                this.posX=posX-1;
                this.posY=posY-1;
                setPossibleMoves(this.posX, this.posY);
                possibleMoves[2][2]=4;
                break;
            case NORTH:
                this.posX=posX-1;
                this.posY=posY;
                setPossibleMoves(this.posX, this.posY);
                possibleMoves[2][1]=4;
                break;
            case NORTH_EAST:
                this.posX=posX-1;
                this.posY=posY+1;
                setPossibleMoves(this.posX, this.posY);
                possibleMoves[2][0]=4;
                break;
            case WEST:
                this.posX=posX;
                this.posY=posY-1;
                setPossibleMoves(this.posX, this.posY);
                possibleMoves[1][2]=4;
                break;
            case EAST:
                this.posX=posX;
                this.posY=posY+1;
                setPossibleMoves(this.posX, this.posY);
                possibleMoves[1][0]=4;
                break;
            case SOUTH_WEST:
                this.posX=posX+1;
                this.posY=posY-1;
                setPossibleMoves(this.posX, this.posY);
                possibleMoves[0][2]=4;
                break;
            case SOUTH:
                this.posX=posX+1;
                this.posY=posY;
                setPossibleMoves(this.posX, this.posY);
                possibleMoves[0][1]=4;
                break;
            case SOUTH_EAST:
                this.posX=posX+1;
                this.posY=posY+1;
                setPossibleMoves(this.posX, this.posY);
                possibleMoves[0][0]=4;
                break;
            default:
                throw new IllegalMovementException();
        }
    }
    /**
     *
     * @param posX is the row where the builder is after the movement
     * @param posY is the column where the builder is after the movement
     */

    public void setPossibleBuildings(int posX, int posY) {
        possibleBuildings = board.neighborLevelCell(posX, posY);
    }

    public int[][] getPossibleBuildings() {
        return possibleBuildings;
    }

    /**
     *
     * @param posX is the row where the builder is
     * @param posY is the column where the builder is
     * @param direction is the direction in which the player wants to build
     */

    public void build (int posX, int posY, Direction direction) throws IllegalConstructionException{
        //LevelType level;
        setPossibleBuildings( posX, posY);
        switch (direction) {
            case NORTH_WEST:
                this.buildPosX = this.posX - 1;
                this.buildPosY = this.posY - 1;
                i = 0;
                j = 0;
                break;
            case NORTH:
                this.buildPosX = this.posX - 1;
                this.buildPosY = this.posY;
                i = 0;
                j = 1;
                break;
            case NORTH_EAST:
                this.buildPosX = this.posX - 1;
                this.buildPosY = this.posY + 1;
                i = 0;
                j = 2;
                break;
            case WEST:
                this.buildPosX = this.posX;
                this.buildPosY = this.posY - 1;
                i = 1;
                j = 0;
                break;
            case EAST:
                this.buildPosX = this.posX;
                this.buildPosY = this.posY + 1;
                i = 1;
                j = 2;
                break;
            case SOUTH_WEST:
                this.buildPosX = this.posX + 1;
                this.buildPosY = this.posY - 1;
                i = 2;
                j = 0;
                break;
            case SOUTH:
                this.buildPosX = this.posX + 1;
                this.buildPosY = this.posY;
                i = 2;
                j = 1;
                break;
            case SOUTH_EAST:
                this.buildPosX = this.posX + 1;
                this.buildPosY = this.posY + 1;
                i = 2;
                j = 2;
                break;
        }
        switch (getPossibleBuildings()[i][j]) {
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
            default:
                throw new IllegalConstructionException();
        }
        possibleBuildings[i][j]=-2;
    }
}
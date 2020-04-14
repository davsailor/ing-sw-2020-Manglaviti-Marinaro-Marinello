package it.polimi.ingsw2020.santorini.model;

import it.polimi.ingsw2020.santorini.utils.AccessType;
import it.polimi.ingsw2020.santorini.utils.Color;
import it.polimi.ingsw2020.santorini.utils.Direction;
import it.polimi.ingsw2020.santorini.utils.LevelType;

public class Builder {
    private Color color;
    private char gender;
    private Player player;
    private Board board;
    private int posX;
    private int posY;
    private int buildPosX;
    private int buildPosY;
    private int[][] possibleMoves;          // 0=NO, 1 = SI noRise, 2 = SI conRise, 3=SI -2level, 4=MOVEFFETT
    private int[][] possibleBuildings ;
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
        posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        posY = posY;
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
     * @param player
     * @param gender
     */
    public Builder(Player player, char gender,Board board) {
        color = player.getColor();
        this.gender = gender;
        this.player = player;
        this.posX=0;
        this.posY=0;
        this.buildPosX=0;
        this.buildPosY=0;
        this.board= board;
        possibleMoves =new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                possibleMoves[i][j] = 1;
            }
        }
        possibleBuildings= new int[3][3];
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
     * @param b1
     */
    public void swapBuilders(Builder b1){
        int tempX;
        int tempY;
        tempX=this.posX;
        tempY=this.posY;
        this.posX=b1.posX;
        this.posY=b1.posY;
        b1.posX=tempX;
        b1.posY=tempY;
    }

    public void setPossibleMoves(int posX, int posY) {
        this.possibleMoves = board.neighboringStatusCell(posX,posY, AccessType.FREE);
    }

    public int[][] getPossibleMoves() {
        return possibleMoves;
    }

    /**
     *
     * @param direction
     * @param posX
     * @param posY
     */
    public void move(Direction direction, int posX , int posY){
        switch (direction) {
            case NORTH_WEST:
                this.posX=posX-1;
                this.posY=posY-1;
                setPossibleMoves(this.posX, this.posY);
                possibleMoves[2][2]=4;
            case NORTH:
                this.posX=posX-1;
                this.posY=posY;
                setPossibleMoves(this.posX, this.posY);
                possibleMoves[2][1]=4;
            case NORTH_EAST:
                this.posX=posX-1;
                this.posY=posY+1;
                setPossibleMoves(this.posX, this.posY);
                possibleMoves[2][0]=4;
            case WEST:
                this.posX=posX;
                this.posY=posY-1;
                setPossibleMoves(this.posX, this.posY);
                possibleMoves[1][2]=4;
            case EAST:
                this.posX=posX;
                this.posY=posY+1;
                setPossibleMoves(this.posX, this.posY);
                possibleMoves[1][0]=4;
            case SOUTH_WEST:
                this.posX=posX+1;
                this.posY=posY-1;
                setPossibleMoves(this.posX, this.posY);
                possibleMoves[0][2]=4;
            case SOUTH:
                this.posX=posX+1;
                this.posY=posY;
                setPossibleMoves(this.posX, this.posY);
                possibleMoves[0][1]=4;
            case SOUTH_EAST:
                this.posX=posX+1;
                this.posY=posY+1;
                setPossibleMoves(this.posX, this.posY);
                possibleMoves[0][0]=4;
        }
    }
    /**
     *
     * @param posX
     * @param posY
     */
    public void setPossibleBuildings(int posX, int posY) {
        possibleBuildings = board.neighborLevelCell(posX, posY);
    }

    /**
     *
     * @param buildPosX
     * @param buildPosY
     */
    public void build (int buildPosX, int buildPosY, Direction direction){          //UML passa posX e posY, sbagliato?
         LevelType level;
         int i=0,j=0;
         this.buildPosX=buildPosX;
         this.buildPosY=buildPosY;

        switch (direction) {
            case NORTH_WEST:
                this.buildPosX = this.posX - 1;
                this.buildPosY = this.posY - 1;
                i = 0;
                j = 0;
            case NORTH:
                this.buildPosX = this.posX - 1;
                this.buildPosY = this.posY;
                i = 0;
                j = 1;
            case NORTH_EAST:
                this.buildPosX = this.posX - 1;
                this.buildPosY = this.posY + 1;
                i = 0;
                j = 2;
            case WEST:
                this.buildPosX = posX;
                this.buildPosY = posY - 1;
                i = 1;
                j = 0;
            case EAST:
                this.buildPosX = posX;
                this.buildPosY = posY + 1;
                i = 1;
                j = 2;
            case SOUTH_WEST:
                this.buildPosX = posX + 1;
                this.buildPosY = posY - 1;
                i = 2;
                j = 0;
            case SOUTH:
                this.buildPosX = posX + 1;
                this.buildPosY = posY;
                i = 2;
                j = 1;
            case SOUTH_EAST:
                this.buildPosX = posX + 1;
                this.buildPosY = posY + 1;
        }

        switch (possibleBuildings[i][j]) {
            case 0:
                board.buildBlock(buildPosX, buildPosY, LevelType.BASE);
            case 1:
                board.buildBlock(buildPosX, buildPosY, LevelType.MID);
            case 2:
                board.buildBlock(buildPosX, buildPosY, LevelType.TOP);
            case 3:
                board.buildBlock(buildPosX, buildPosY, LevelType.DOME);
        }
        setPossibleBuildings(this.buildPosX, this.buildPosY);
        possibleBuildings[i][j]=-2;
    }
}

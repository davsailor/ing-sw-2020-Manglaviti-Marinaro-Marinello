package it.polimi.ingsw2020.santorini.model;

import it.polimi.ingsw2020.santorini.utils.AccessType;
import it.polimi.ingsw2020.santorini.utils.LevelType;

import java.util.EnumSet;


public class Cell {
    private LevelType[] building;
    private AccessType status;
    private Builder builder;

    /**
     * constructor of the class
     * @param status is the status
     */
    public Cell (AccessType status){
        this.building = new LevelType[5];
        if(status == AccessType.FORBIDDEN) this.building[0] = LevelType.COAST;
        else this.building[0] = (LevelType.GROUND);
        this.status = status;
        this.builder = null;
    }


    /*
     * getter and setter of the class
     */
    public LevelType getLevel(){
        int i = 0;
        while((i < 4)&&(this.building[i + 1] != null)){
            i++;
        }
        return (this.building[i]);//understand if toString is necessary
    }

    public void setLevel(LevelType building){
        int i = 0;
        while((i < 5)&&(this.building[i] != null)){i++;}
        this.building[i] = building;
    }

    public AccessType getStatus(){
        return status;
    }

    public void setStatus(AccessType status){
        this.status = status;
    }

    public Builder getBuilder() {
        return builder;
    }

    public void setBuilder(Builder builder) {
        this.builder = builder;
    }

    /**
     *
     */
    public void demolish(){
        int i = 0;
        while((i < 4) && (this.building[i] != null))
            ++i;
        this.building[--i] = null;
    }

    /**
     * it calculates the difference between the heights of two cell
     * @param cell is the cell where the builder may want to go
     * @return the integer value of the difference of the two heights
     */
    public int calculateJump (Cell cell){
        return cell.getLevel().getHeight() - this.getLevel().getHeight();
    }
}

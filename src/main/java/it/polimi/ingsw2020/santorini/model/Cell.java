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

    /**
     *  it return the pieces of the highest piece of building on the cell
     * @return the string containing the name of the highest piece on the cell
     */
    public LevelType getLevel(){
        int i = 0;
        while((i < 4)&&(this.building[i + 1] != null)){
            i++;
        }
        return (this.building[i]);//understand if toString is necessary
    }

    /**
     * it sets the piece of the building on the cell
     * @param building is the piece of build that will be placed on the cell
     */
    public void setLevel(LevelType building){
        int i = 0;
        while((i < 5)&&(this.building[i] != null)){i++;}
        this.building[i] = building;
    }

    /**
     * the method removes the block on the top of the cell, that is the last element in the array of buildings
     */
    public void demolish(){
        int i = 0;
        while((i < 4) && (this.building[i] != null))
            ++i;
        this.building[--i] = null;
    }

    /**
     * it return the the status of the cell
     * @return the string containing the name the status of the cell
     */
    public AccessType getStatus(){
        return status;
    }

    /**
     * it sets the status of the cell
     * @param status is the status that the cell will assume
     */
    public void setStatus(AccessType status){
        this.status = status;
    }

    /**
     * the method is the getter of the attribute Builder
     * @return is the reference to the builder in the cell
     */
    public Builder getBuilder() {
        return builder;
    }

    /**
     * the method is the setter of the attribute Builder
     * @param builder is the reference of the builder that has been placed in the cell
     */
    public void setBuilder(Builder builder) {
        this.builder = builder;
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

package it.polimi.ingsw2020.santorini.model;


public class Cell {
    private LevelType building;
    private AccessType status;
    private Builder builder;

    /**
     * constructor of the class
     * @param status is the status
     */
    public Cell (AccessType status){
        this.building = LevelType.Ground;
        this.status = status;
        this.builder = null;
    }

    /**
     *  it return the pieces of the building on the cell
     * @return the string containing the name of the highest piece on the cell
     */
    public String getLevel(){
       return(building.toString());//understand if toString is necessary
    }

    /**
     * it sets the piece of the building on the cell
     * @param building is the piece of build that will be placed on the cell
     */
    public void setLevel(LevelType building){
        this.building = building;
    }

    /**
     * it return the the status of the cell
     * @return the string containing the name the status of the cell
     */
    public String getStatus(){
        return (status.toString());
    }

    /**
     * it sets the status of the cell
     * @param status is the status that the cell will assume
     */
    public void setStatus(AccessType status){
        this.status = status;
    }

    /**
     * it calculates the difference between the heights of two cell
     * @param cell is the cell where the builder may want to go
     * @return the integer value of the difference of the two heights
     */
    public int calculateJump (Cell cell){
        return cell.building.getHeight() - this.building.getHeight();
    }
}

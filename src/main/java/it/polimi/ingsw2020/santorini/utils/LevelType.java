package it.polimi.ingsw2020.santorini.utils;

/**
 * Enum that describes the pieces used to build the building itself
 */
public enum LevelType {
    GROUND (0),
    BASE (1),
    MID (2),
    TOP (3),
    DOME (4);

    private final int height;

    /**
     * it is the enum's constructor
     * @param height is the parameter that needs to be associated with the values of LevelType
     */
    LevelType (int height){
        this.height = height;
    }

    /**
     * it return the values of height associated with each level
     * @return the integer value of height of the level
     */
    public int getHeight(){
        return this.height;
    }

}

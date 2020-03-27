package it.polimi.ingsw2020.santorini.model;

public enum LevelType {
    Ground (0),
    Base (1),
    Mid (2),
    Top (3),
    Dome (4);

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
    int getHeight(){
        return this.height;
    }

}

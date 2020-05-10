package it.polimi.ingsw2020.santorini.utils.messages.godsParam;

import it.polimi.ingsw2020.santorini.utils.Direction;

import java.util.ArrayList;

public class PoseidonParamMessage {

    private ArrayList<Direction> direction;
    private int numberOfBuild;

    public PoseidonParamMessage() {
    }

    public ArrayList<Direction> getDirection() {
        return direction;
    }
    public void setDirection(ArrayList<Direction> direction) {
        this.direction = direction;
    }

    public int getNumberOfBuild() {
        return numberOfBuild;
    }

    public void setNumberOfBuild(int numberOfBuild) {
        this.numberOfBuild = numberOfBuild;
    }
}

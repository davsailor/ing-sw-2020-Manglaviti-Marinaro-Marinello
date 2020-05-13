package it.polimi.ingsw2020.santorini.utils.messages.godsParam;

import it.polimi.ingsw2020.santorini.utils.Direction;

import java.util.ArrayList;

public class PoseidonParamMessage {

    private ArrayList<Direction> direction;
    private int numberOfBuild;
    private char constructionGender;

    public PoseidonParamMessage() {
    }

    public char getConstructionGender() {
        return constructionGender;
    }

    public void setConstructionGender(char constructionGender) {
        this.constructionGender = constructionGender;
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

package it.polimi.ingsw2020.santorini.utils.messages.godsParam;

import it.polimi.ingsw2020.santorini.utils.Direction;

public class PoseidonParamMessage {
    private Direction direction1;
    private Direction direction2;
    private Direction direction3;
    private int numberOfBuild;

    public PoseidonParamMessage() {
    }

    public Direction getDirection1() {
        return direction1;
    }

    public void setDirection1(Direction direction1) {
        this.direction1 = direction1;
    }

    public Direction getDirection2() {
        return direction2;
    }

    public void setDirection2(Direction direction2) {
        this.direction2 = direction2;
    }

    public Direction getDirection3() {
        return direction3;
    }

    public void setDirection3(Direction direction3) {
        this.direction3 = direction3;
    }

    public int getNumberOfBuild() {
        return numberOfBuild;
    }

    public void setNumberOfBuild(int numberOfBuild) {
        this.numberOfBuild = numberOfBuild;
    }
}

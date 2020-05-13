package it.polimi.ingsw2020.santorini.utils.messages.godsParam;

import it.polimi.ingsw2020.santorini.utils.Direction;

public class PrometheusParamMessage {
    private char builderSex;
    private Direction direction;

    public PrometheusParamMessage() {
    }

    public char getBuilderSex() {
        return builderSex;
    }

    public void setBuilderSex(char builderSex) {
        this.builderSex = builderSex;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}

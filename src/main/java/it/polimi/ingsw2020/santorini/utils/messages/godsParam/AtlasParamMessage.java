package it.polimi.ingsw2020.santorini.utils.messages.godsParam;

import it.polimi.ingsw2020.santorini.utils.Direction;

public class AtlasParamMessage {
    private Direction direction;
    private char playingBuilderSex;

    public AtlasParamMessage() {
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public char getPlayingBuilderSex() {
        return playingBuilderSex;
    }

    public void setPlayingBuilderSex(char playingBuilderSex) {
        this.playingBuilderSex = playingBuilderSex;
    }
}

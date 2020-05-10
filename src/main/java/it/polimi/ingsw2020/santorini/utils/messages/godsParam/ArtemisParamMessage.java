package it.polimi.ingsw2020.santorini.utils.messages.godsParam;

import it.polimi.ingsw2020.santorini.utils.Direction;

public class ArtemisParamMessage {
    private Direction direction;
    private char playingBuilderGender;

    public ArtemisParamMessage() {
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public char getPlayingBuilderGender() {
        return playingBuilderGender;
    }

    public void setPlayingBuilderGender(char playingBuilderGender) {
        this.playingBuilderGender = playingBuilderGender;
    }
}

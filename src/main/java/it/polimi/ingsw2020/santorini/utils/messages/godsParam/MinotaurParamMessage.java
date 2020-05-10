package it.polimi.ingsw2020.santorini.utils.messages.godsParam;

import it.polimi.ingsw2020.santorini.utils.Direction;

public class MinotaurParamMessage {
    private Direction opponentBuilderDirection;
    private char playingBuilderSex;

    public MinotaurParamMessage() {
    }

    public Direction getOpponentBuilderDirection() {
        return opponentBuilderDirection;
    }

    public void setOpponentBuilderDirection(Direction opponentBuilderDirection) {
        this.opponentBuilderDirection = opponentBuilderDirection;
    }

    public char getPlayingBuilderSex() {
        return playingBuilderSex;
    }

    public void setPlayingBuilderSex(char playingBuilderSex) {
        this.playingBuilderSex = playingBuilderSex;
    }
}

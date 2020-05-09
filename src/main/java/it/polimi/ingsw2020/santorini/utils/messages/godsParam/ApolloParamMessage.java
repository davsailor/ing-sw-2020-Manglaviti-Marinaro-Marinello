package it.polimi.ingsw2020.santorini.utils.messages.godsParam;

import it.polimi.ingsw2020.santorini.utils.Direction;

public class ApolloParamMessage {
    private char yourBuilderGender;
    private Direction opponentBuilderDirection;

    public ApolloParamMessage() {
    }

    public char getYourBuilderGender() {
        return yourBuilderGender;
    }

    public void setYourBuilderGender(char yourBuilderGender) {
        this.yourBuilderGender = yourBuilderGender;
    }

    public Direction getOpponentBuilderDirection() {
        return opponentBuilderDirection;
    }

    public void setOpponentBuilderDirection(Direction opponentBuilderDirection) {
        this.opponentBuilderDirection = opponentBuilderDirection;
    }
}

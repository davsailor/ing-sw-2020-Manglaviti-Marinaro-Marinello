package it.polimi.ingsw2020.santorini.utils.messages.godsParam;

public class ApolloParamMessage {
    private int[] yourBuilder;
    private int[] opponentBuilder;

    public ApolloParamMessage() {
    }

    public int[] getYourBuilder() {
        return yourBuilder;
    }

    public void setYourBuilder(int[] yourBuilder) {
        this.yourBuilder = yourBuilder;
    }

    public int[] getOpponentBuilder() {
        return opponentBuilder;
    }

    public void setOpponentBuilder(int[] opponentBuilder) {
        this.opponentBuilder = opponentBuilder;
    }
}

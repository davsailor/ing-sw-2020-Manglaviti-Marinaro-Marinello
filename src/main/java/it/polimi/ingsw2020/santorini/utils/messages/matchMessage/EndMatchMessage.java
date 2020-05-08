package it.polimi.ingsw2020.santorini.utils.messages.matchMessage;

public class EndMatchMessage {
    private String winner;

    public EndMatchMessage(String nickname) {
        winner = nickname;
    }

    public String getWinner() {
        return winner;
    }
}

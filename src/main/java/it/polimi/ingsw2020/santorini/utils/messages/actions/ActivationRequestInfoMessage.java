package it.polimi.ingsw2020.santorini.utils.messages.actions;

public class ActivationRequestInfoMessage {
    private String currentPlayer;
    private String god;

    public ActivationRequestInfoMessage(String currentPlayer, String god) {
        this.currentPlayer = currentPlayer;
        this.god = god;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public String getGod() {
        return god;
    }
}

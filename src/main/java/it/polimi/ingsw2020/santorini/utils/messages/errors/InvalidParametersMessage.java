package it.polimi.ingsw2020.santorini.utils.messages.errors;

public class InvalidParametersMessage {
    private String god;

    public InvalidParametersMessage(String god) {
        this.god = god;
    }

    public String getGod() {
        return god;
    }
}

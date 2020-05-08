package it.polimi.ingsw2020.santorini.utils.messages.errors;

public class InvalidParametersMessage {
    private String god;
    private String error;

    public InvalidParametersMessage(String god, String error) {
        this.error = error;
        this.god = god;
    }

    public String getGod() {
        return god;
    }

    public String getError() {
        return error;
    }
}

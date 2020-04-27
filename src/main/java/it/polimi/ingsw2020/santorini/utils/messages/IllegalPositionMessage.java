package it.polimi.ingsw2020.santorini.utils.messages;

public class IllegalPositionMessage {
    private String username;
    private boolean builderFToChange;
    private boolean builderMToChange;

    public IllegalPositionMessage(String username, boolean builderFToChange, boolean builderMToChange) {
        this.username = username;
        this.builderFToChange = builderFToChange;
        this.builderMToChange = builderMToChange;
    }

    public String getUsername() {
        return username;
    }

    public boolean isBuilderFToChange() {
        return builderFToChange;
    }

    public boolean isBuilderMToChange() {
        return builderMToChange;
    }
}

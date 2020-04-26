package it.polimi.ingsw2020.santorini.utils.messages;

public class IllegalPositionMessage {
    String username;
    boolean builderF;
    boolean builderM;

    public IllegalPositionMessage(String username) {
        this.username = username;
        this.builderF = false;
        this.builderM = false;
    }

    public String getUsername() {
        return username;
    }

    public boolean isBuilderF() {
        return builderF;
    }

    public void setBuilderF(boolean builderF) {
        this.builderF = builderF;
    }

    public boolean isBuilderM() {
        return builderM;
    }

    public void setBuilderM(boolean builderM) {
        this.builderM = builderM;
    }
}

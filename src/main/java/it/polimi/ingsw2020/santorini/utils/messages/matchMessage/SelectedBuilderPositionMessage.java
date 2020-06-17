package it.polimi.ingsw2020.santorini.utils.messages.matchMessage;

public class SelectedBuilderPositionMessage {
    private String username;
    private int[] builderF;
    private int[] builderM;

    public SelectedBuilderPositionMessage(String username, int[] builderF, int[] builderM) {
        this.username = username;
        this.builderF = builderF;
        this.builderM = builderM;
    }

    public String getUsername(){
        return username;
    }
    public int[] getBuilderF() {
        return builderF;
    }

    public int[] getBuilderM() {
        return builderM;
    }
}

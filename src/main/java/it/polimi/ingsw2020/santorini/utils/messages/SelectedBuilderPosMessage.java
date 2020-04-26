package it.polimi.ingsw2020.santorini.utils.messages;

public class SelectedBuilderPosMessage {
    String username;
    int[] builderF;
    int[] builderM;

    public SelectedBuilderPosMessage(String username, int[] builderF, int[] builderM) {
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

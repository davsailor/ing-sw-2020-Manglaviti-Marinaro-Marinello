package it.polimi.ingsw2020.santorini.utils.messages.actions;

public class AskBuildSelectionMessage {
    private int[][] possibleBuildings;

    public AskBuildSelectionMessage(int[][] possibleBuildings) {
        this.possibleBuildings = possibleBuildings;
    }

    public int[][] getPossibleBuildings() {
        return possibleBuildings;
    }
}

package it.polimi.ingsw2020.santorini.utils.messages.matchMessage;

public class GameGodsSelectionMessage {
    private int[] selectedGods;

    public GameGodsSelectionMessage(int[] selectedGods) {
        this.selectedGods = selectedGods;
    }

    public int[] getSelectedGods() {
        return selectedGods;
    }
}

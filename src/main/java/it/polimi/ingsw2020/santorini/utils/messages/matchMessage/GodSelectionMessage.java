package it.polimi.ingsw2020.santorini.utils.messages.matchMessage;

import java.util.ArrayList;

public class GodSelectionMessage {
    private int selected;
    private ArrayList<Integer> remaining;

    public GodSelectionMessage(int selected, ArrayList<Integer> remaining) {
        this.selected = selected;
        this.remaining = remaining;
    }

    public int getSelected() {
        return selected;
    }

    public ArrayList<Integer> getRemaining() {
        return remaining;
    }
}

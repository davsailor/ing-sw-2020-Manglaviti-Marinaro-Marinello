package it.polimi.ingsw2020.santorini.utils.messages;

import it.polimi.ingsw2020.santorini.model.Cell;

import java.util.ArrayList;
import java.util.Arrays;

public class TurnPlayerMessage {
    private String currentPlayer;
    private ArrayList<Cell> cells;

    public TurnPlayerMessage(String currentPlayer, Cell[][] cells) {
        this.currentPlayer = currentPlayer;
        this.cells = new ArrayList<>();
        for(int i = 0; i < 7; ++i)
            this.cells.addAll(Arrays.asList(cells[i]).subList(0, 7));
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public ArrayList<Cell> getCells() {
        return cells;
    }
}

package it.polimi.ingsw2020.santorini.utils.messages.matchMessage;

import it.polimi.ingsw2020.santorini.model.Cell;
import it.polimi.ingsw2020.santorini.model.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class MatchStateMessage {
    private Player currentPlayer;
    private Cell[][] cells;

    public MatchStateMessage(Player currentPlayer, Cell[][] cells) {
        this.currentPlayer = currentPlayer;
        this.cells = new Cell[7][7];
        this.cells = cells;

    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public ArrayList<Cell> getCells() {
        ArrayList<Cell> toPrint = new ArrayList<>();
        for(int i = 0; i < 7; ++i)
            toPrint.addAll(Arrays.asList(cells[i]).subList(0, 7));
        return toPrint;
    }

    public Cell[][] getBoard(){
        return this.cells;
    }
}

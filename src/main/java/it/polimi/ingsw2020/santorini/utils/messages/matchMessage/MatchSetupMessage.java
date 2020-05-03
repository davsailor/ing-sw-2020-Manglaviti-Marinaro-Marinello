package it.polimi.ingsw2020.santorini.utils.messages.matchMessage;

import it.polimi.ingsw2020.santorini.model.Cell;
import it.polimi.ingsw2020.santorini.model.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class MatchSetupMessage {
    private ArrayList<Player> players;
    private ArrayList<Cell> cells;

    public MatchSetupMessage(Player[] players, Cell[][] board){
        this.players = new ArrayList<>();
        this.cells = new ArrayList<>();

        this.players.addAll(Arrays.asList(players));
        for(int i = 0; i < 7; ++i)
            this.cells.addAll(Arrays.asList(board[i]).subList(0, 7));
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Cell> getCells() {
        return cells;
    }
}

package it.polimi.ingsw2020.santorini.utils.messages;

import it.polimi.ingsw2020.santorini.model.Cell;
import it.polimi.ingsw2020.santorini.model.Match;
import it.polimi.ingsw2020.santorini.model.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class MatchStartMessage {
    private ArrayList<Cell> board;
    private ArrayList<Player> players;
    private int turnNumber;
    private int currentPlayerIndex;

    public MatchStartMessage(Match match) {
        this.board = new ArrayList<>();
        Cell[][] cells = match.getBoard().getBoard();
        for(int i = 0; i < 7; ++i)
            this.board.addAll(Arrays.asList(cells[i]).subList(0, 7));
        this.players = new ArrayList<>();
        this.players.addAll(Arrays.asList(match.getPlayers()));
        this.turnNumber = match.getTurnNumber();
        this.currentPlayerIndex = match.getCurrentPlayerIndex();
    }

    public ArrayList<Cell> getBoard() {
        return board;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
}

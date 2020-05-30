package it.polimi.ingsw2020.santorini.utils.messages.matchMessage;

import it.polimi.ingsw2020.santorini.model.Cell;
import it.polimi.ingsw2020.santorini.model.Match;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.utils.PhaseType;

import java.util.ArrayList;
import java.util.Arrays;

public class UpdateMessage {
    private ArrayList<Cell> board;
    private ArrayList<Player> players;
    private PhaseType phase;
    private int turnNumber;
    private Player currentPlayer;
    private boolean canActivateGod;

    public UpdateMessage(Match match, PhaseType phase) {
        this.board = new ArrayList<>();
        Cell[][] cells = match.getBoard().getBoard();
        for(int i = 0; i < 7; ++i)
            this.board.addAll(Arrays.asList(cells[i]).subList(0, 7));
        this.players = new ArrayList<>();
        this.players.addAll(Arrays.asList(match.getPlayers()));
        this.phase = phase;
        this.turnNumber = match.getTurnNumber();
        this.currentPlayer = match.getCurrentPlayer();
    }

    public ArrayList<Cell> getBoard() {
        return board;
    }

    public Cell[][] getCells(){
        Cell[][] cells = new Cell[7][7];
        int j = 0;
        int k = 0;
        for(int i = 0; i < board.size(); ++i){
            cells[j][k] = board.get(i);
            if(k < 7) ++k;
            if(k == 7){
                ++j;
                k = 0;
            }
        }
        return cells;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public PhaseType getPhase() { return phase; }

    public boolean isCanActivateGod() { return canActivateGod; }

    public void setCanActivateGod(boolean canActivateGod) { this.canActivateGod = canActivateGod; }

    public int getTurnNumber() {
        return turnNumber;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}

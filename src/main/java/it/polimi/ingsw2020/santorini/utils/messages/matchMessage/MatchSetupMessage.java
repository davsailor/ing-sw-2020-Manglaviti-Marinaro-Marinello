package it.polimi.ingsw2020.santorini.utils.messages.matchMessage;

import it.polimi.ingsw2020.santorini.model.Cell;
import it.polimi.ingsw2020.santorini.model.GodDeck;
import it.polimi.ingsw2020.santorini.model.Match;
import it.polimi.ingsw2020.santorini.model.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class MatchSetupMessage {
    private ArrayList<Player> players;
    private ArrayList<Cell> cells;
    private GodDeck gods;
    private ArrayList<Integer> selectedGods;
    private int currentPlayerIndex;

    public MatchSetupMessage(Match match, ArrayList<Integer> selectedGods){
        this.players = new ArrayList<>();
        this.cells = new ArrayList<>();
        this.selectedGods = new ArrayList<>();
        if(selectedGods == null) {
            for (int i = match.getBoard().getGodCards().getNextSelection(); i < match.getBoard().getGodCards().getNextCard(); ++i)
                this.selectedGods.add(match.getBoard().getGodCards().getDeck()[i]);
        } else
            this.selectedGods = selectedGods;
        this.gods = match.getBoard().getGodCards();
        this.currentPlayerIndex = match.getCurrentPlayerIndex();
        this.players.addAll(Arrays.asList(match.getPlayers()));
        for(int i = 0; i < 7; ++i)
            this.cells.addAll(Arrays.asList(match.getBoard().getBoard()[i]).subList(0, 7));
    }

    public ArrayList<Integer> getSelectedGods() {
        return selectedGods;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Cell> getCells() {
        return cells;
    }

    public GodDeck getGods() {
        return gods;
    }
}

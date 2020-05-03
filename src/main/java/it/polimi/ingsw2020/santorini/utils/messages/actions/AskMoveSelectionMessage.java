package it.polimi.ingsw2020.santorini.utils.messages.actions;

public class AskMoveSelectionMessage {
    int[][] possibleMoves;

    public AskMoveSelectionMessage(int[][] possibleMoves) {
        this.possibleMoves = possibleMoves;
    }

    public int[][] getPossibleMoves() {
        return possibleMoves;
    }
}

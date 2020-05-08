package it.polimi.ingsw2020.santorini.utils.messages.actions;

public class AskMoveSelectionMessage {
    private int[][] possibleMoves;
    private boolean riseActions;
    private boolean moveActions;

    public AskMoveSelectionMessage(int[][] possibleMoves, boolean riseActions, boolean moveActions) {
        this.possibleMoves = possibleMoves;
        this.riseActions = riseActions;
        this.moveActions = moveActions;
    }

    public boolean isRiseActions() {
        return riseActions;
    }

    public boolean isMoveActions() {
        return moveActions;
    }

    public int[][] getPossibleMoves() {
        return possibleMoves;
    }
}

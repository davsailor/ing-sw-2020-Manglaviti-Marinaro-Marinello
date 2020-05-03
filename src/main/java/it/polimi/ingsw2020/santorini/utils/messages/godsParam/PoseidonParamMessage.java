package it.polimi.ingsw2020.santorini.utils.messages.godsParam;

public class PoseidonParamMessage {
    private int[] targetedCell;

    public PoseidonParamMessage() {
    }

    public int[] getTargetedCell() {
        return targetedCell;
    }

    public void setTargetedCell(int[] targetedCell) {
        this.targetedCell = targetedCell;
    }
}

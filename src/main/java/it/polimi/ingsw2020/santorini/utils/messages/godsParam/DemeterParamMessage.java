package it.polimi.ingsw2020.santorini.utils.messages.godsParam;

public class DemeterParamMessage {
    private int[] targetedCell;

    public DemeterParamMessage() {
    }

    public int[] getTargetedCell() {
        return targetedCell;
    }

    public void setTargetedCell(int[] targetedCell) {
        this.targetedCell = targetedCell;
    }
}

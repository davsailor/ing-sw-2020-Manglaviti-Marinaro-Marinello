package it.polimi.ingsw2020.santorini.utils.messages.godsParam;

public class PrometheusParamMessage {
    private int[] builder;
    private int[] targetedCell;

    public PrometheusParamMessage() {
    }

    public int[] getTargetedCell() {
        return targetedCell;
    }

    public int[] getBuilder() {
        return builder;
    }

    public void setBuilder(int[] builder) {
        this.builder = builder;
    }

    public void setTargetedCell(int[] targetedCell) {
        this.targetedCell = targetedCell;
    }
}

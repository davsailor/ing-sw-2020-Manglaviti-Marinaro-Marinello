package it.polimi.ingsw2020.santorini.utils.messages.godsParam;

import it.polimi.ingsw2020.santorini.utils.Direction;

public class AresParamMessage {
    private Direction targetedBlockDirection;

    public char getDemolitionBuilderSex() {
        return demolitionBuilderSex;
    }

    public void setDemolitionBuilderSex(char demolitionBuilderSex) {
        this.demolitionBuilderSex = demolitionBuilderSex;
    }

    private char demolitionBuilderSex;

    public AresParamMessage() {
    }

    public Direction getTargetedBlock() {
        return targetedBlockDirection;
    }

    public void setTargetedBlock(Direction targetedBlockDirection) {
        this.targetedBlockDirection = targetedBlockDirection;
    }
}

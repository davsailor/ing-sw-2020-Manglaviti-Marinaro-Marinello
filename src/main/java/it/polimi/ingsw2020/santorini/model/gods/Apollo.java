package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.utils.PhaseType;

public class Apollo extends GodCard {
    private String name = getClass().getSimpleName();
    private int maxPlayersNumber = 3;
    private String timingName = "Your Move";
    private PhaseType timing = PhaseType.STANDBY_PHASE_1;
    private boolean mandatory = false;

    public Apollo(){}

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMaxPlayersNumber() {
        return maxPlayersNumber;
    }

    @Override
    public String getTimingName() {
        return timingName;
    }

    @Override
    public PhaseType getTiming() {
        return timing;
    }

    @Override
    public boolean isMandatory() {
        return mandatory;
    }

    @Override
    public void invokeGod(Board field, Player invoker) {

    }

    @Override
    public String toStringEffect() {
        return this.name + "\n" + this.timingName + ": Your Worker may move into an opponent Worker’s space\n" +
                "(using normal movement rules) and force their Worker to the space yours\n" +
                "just vacated (swapping their positions).";
    }
}

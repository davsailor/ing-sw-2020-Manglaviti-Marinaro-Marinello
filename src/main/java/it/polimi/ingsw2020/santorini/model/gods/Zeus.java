package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.model.Board;
import it.polimi.ingsw2020.santorini.model.GodCard;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.utils.PhaseType;

public class Zeus extends GodCard {
    private String name = getClass().getSimpleName();
    private int maxPlayersNumber = 3;
    private String timingName = "Your Build";
    private PhaseType timing = PhaseType.STANDBY_PHASE_2;
    private boolean mandatory = false;

    public Zeus(){}

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

    public void invokeGod(Board field, Player invoker) {

    }

    public String toStringEffect() {
        return this.name + "\n" + this.timingName + ":  Your Worker may build under itself in its current\n" +
                "space, forcing it up one level. You do not win by forcing yourself up to the third level.";
    }
}

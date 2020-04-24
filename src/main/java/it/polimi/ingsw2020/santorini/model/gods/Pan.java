package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.model.Board;
import it.polimi.ingsw2020.santorini.model.GodCard;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.utils.PhaseType;

public class Pan extends GodCard {
    private String name = getClass().getSimpleName();
    private int maxPlayersNumber = 3;
    private String timingName = "Win Condition";
    private PhaseType timing = PhaseType.STANDBY_PHASE_2;
    private boolean mandatory = true;

    public Pan(){}

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
        return this.name + "\n" + this.timingName + ": You also win if your Worker moves down two or more levels.";
    }
}

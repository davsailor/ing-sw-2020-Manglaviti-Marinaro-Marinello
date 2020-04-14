package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.utils.TimingType;

public class Apollo extends GodCard {
    private String name = getClass().getSimpleName();
    private int maxPlayersNumber = 3;
    private String timingName = "Your Move";
    private TimingType timing = TimingType.STANDBY_PHASE_1;

    public Apollo(){}

    public void invokeGod(Board field, Player invoker) {

    }

    public String toStringEffect() {
        return this.name + "\n" + this.timingName + ": Your Worker may move into an opponent Worker’s space\n" +
                "(using normal movement rules) and force their Worker to the space yours\n" +
                "just vacated (swapping their positions).";
    }
}

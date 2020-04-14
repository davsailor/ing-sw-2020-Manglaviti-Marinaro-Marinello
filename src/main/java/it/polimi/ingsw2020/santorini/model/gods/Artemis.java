package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.model.Board;
import it.polimi.ingsw2020.santorini.model.GodCard;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.utils.TimingType;

public class Artemis extends GodCard {
    private String name = getClass().getSimpleName();
    private int maxPlayersNumber = 3;
    private String timingName = "Your Move";
    private TimingType timing = TimingType.STANDBY_PHASE_2;

    public Artemis(){}

    public void invokeGod(Board field, Player invoker) {

    }

    public String toStringEffect() {
        return this.name + "\n" + this.timingName + ": Your Worker may move one additional time,\n" +
                "but not back to the space it started on.";
    }
}

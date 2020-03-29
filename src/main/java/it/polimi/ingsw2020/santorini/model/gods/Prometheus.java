package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.model.GodCard;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.model.TimingType;

public class Prometheus extends GodCard {
    private String name = getClass().getSimpleName();
    private int maxPlayersNumber = 3;
    private String timingName = "Your Turn";
    private TimingType timing = TimingType.STANDBY_PHASE_1;

    public Prometheus(){}

    public void invokeGod(Board field, Player invoker) {

    }

    public String toStringEffect() {
        return this.name + "\n" + this.timingName + ": If your Worker does not move up,\n" +
                "it may build both before and after moving.";
    }
}

package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.model.Board;
import it.polimi.ingsw2020.santorini.model.GodCard;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.model.TimingType;

public class Ares extends GodCard {
    private String name = getClass().getSimpleName();
    private int maxPlayersNumber = 3;
    private String timingName = "End of Your Turn";
    private TimingType timing = TimingType.STANDBY_PHASE_3;

    public Ares(){}

    public void invokeGod(Board field, Player invoker) {

    }

    public String toStringEffect() {
        return this.name + "\n" + this.timingName + ": You may remove an unoccupied block\n" +
                "(not dome) neighboring your unmoved Worker";
    }
}

package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.model.Board;
import it.polimi.ingsw2020.santorini.model.GodCard;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.utils.TimingType;

public class Hephaestus extends GodCard {
    private String name = getClass().getSimpleName();
    private int maxPlayersNumber = 3;
    private String timingName = "Your Build";
    private TimingType timing = TimingType.STANDBY_PHASE_3;

    public Hephaestus(){}

    public void invokeGod(Board field, Player invoker) {

    }

    public String toStringEffect() {
        return this.name + "\n" + this.timingName + ": Your Worker may build one additional block\n" +
                "(not dome) on top of your first block.";
    }
}

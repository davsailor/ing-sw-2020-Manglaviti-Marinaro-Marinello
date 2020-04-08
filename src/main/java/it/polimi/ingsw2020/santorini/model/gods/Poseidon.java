package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.model.Board;
import it.polimi.ingsw2020.santorini.model.GodCard;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.model.TimingType;

public class Poseidon extends GodCard {
    private String name = getClass().getSimpleName();
    private int maxPlayersNumber = 3;
    private String timingName = "End of Your Turn";
    private TimingType timing = TimingType.STANDBY_PHASE_3;

    public Poseidon(){}

    public void invokeGod(Board field, Player invoker) {

    }

    public String toStringEffect() {
        return this.name + "\n" + this.timingName + ": If your unmoved Worker is on the ground level,\n" +
                "it may build up to three times in neighboring spaces.";
    }
}

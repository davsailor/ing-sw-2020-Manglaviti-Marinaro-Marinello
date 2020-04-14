package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.model.Board;
import it.polimi.ingsw2020.santorini.model.GodCard;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.utils.TimingType;

public class Athena extends GodCard {
    private String name = getClass().getSimpleName();
    private int maxPlayersNumber = 3;
    private String timingName = "Opponents' Turn";
    private TimingType timing = TimingType.STANDBY_PHASE_3;

    public Athena(){}

    public void invokeGod(Board field, Player invoker) {

    }

    public String toStringEffect() {
        return this.name + "\n" + this.timingName + ": If one of your Workers moved up on your last turn, \n" +
                "opponent Workers cannot move up this turn.";
    }
}

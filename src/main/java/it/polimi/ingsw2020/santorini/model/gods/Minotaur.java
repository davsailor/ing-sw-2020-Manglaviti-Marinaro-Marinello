package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.model.Board;
import it.polimi.ingsw2020.santorini.model.GodCard;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.utils.TimingType;

public class Minotaur extends GodCard {
    private String name = getClass().getSimpleName();
    private int maxPlayersNumber = 3;
    private String timingName = "Your Move";
    private TimingType timing = TimingType.STANDBY_PHASE_1;

    public Minotaur(){}

    public void invokeGod(Board field, Player invoker) {

    }

    public String toStringEffect() {
        return this.name + "\n" + this.timingName + ": Your Worker may move into an opponent Worker’s\n" +
                "space (using normal movement rules), if the next space in the same direction is\n" +
                "unoccupied. Their Worker is forced into that space (regardless of its level).";
    }
}

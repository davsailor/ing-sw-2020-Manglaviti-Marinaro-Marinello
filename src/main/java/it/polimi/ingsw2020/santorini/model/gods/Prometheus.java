package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.model.Board;
import it.polimi.ingsw2020.santorini.model.GodCard;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.utils.PhaseType;

public class Prometheus extends GodCard {

    public Prometheus(){
        super();
        name = getClass().getSimpleName();
        maxPlayersNumber = 3;
        timingName = "Your Turn";
        timing = PhaseType.STANDBY_PHASE_1;
        mandatory = false;
    }

    @Override
    public void invokeGod(Board field, Player invoker) {

    }

    public static String toStringEffect(GodCard card) {
        return card.getName() + "\n" + card.getTimingName() + ": If your Worker does not move up,\n" +
                "it may build both before and after moving.";
    }
}

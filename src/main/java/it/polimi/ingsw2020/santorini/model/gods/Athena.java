package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.model.Board;
import it.polimi.ingsw2020.santorini.model.GodCard;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.utils.PhaseType;

public class Athena extends GodCard {

    public Athena(){
        super();
        name = getClass().getSimpleName();
        maxPlayersNumber = 3;
        timingName = "Opponents' Turn";
        timing = PhaseType.STANDBY_PHASE_3;
        mandatory = true;
    }

    @Override
    public void invokeGod(Board field, Player invoker) {

    }

    public static String toStringEffect(GodCard card) {
        return card.getName() + "\n" + card.getTimingName() + ": If one of your Workers moved up on your last turn, \n" +
                "opponent Workers cannot move up this turn.";
    }
}

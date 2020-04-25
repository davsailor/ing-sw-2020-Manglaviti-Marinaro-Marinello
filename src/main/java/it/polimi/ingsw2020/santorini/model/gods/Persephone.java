package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.model.Board;
import it.polimi.ingsw2020.santorini.model.GodCard;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.utils.PhaseType;

public class Persephone extends GodCard {

    public Persephone(){
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
        return card.getName() + "\n" + card.getTimingName() + ": If possible, at least one Worker must move up this turn.";
    }
}

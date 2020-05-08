package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.controller.TurnLogic;
import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;

public class Persephone extends GodCard {

    public Persephone(){
        super();
        name = getClass().getSimpleName();
        maxPlayersNumber = 3;
        timingName = "Opponents' Turn";
        timing = PhaseType.STANDBY_PHASE_3;
        mandatory = true;
        willEnded = false;
        needParameters = false;
    }

    @Override
    public void invokeGod(Match match, Player invoker, Message message, TurnLogic turnManager) {
        System.out.println("potere di " + name + " attivato");
    }

    public static String toStringEffect(GodCard card) {
        return card.getName() + "\n" + card.getTimingName() + ": If possible, at least one Worker must move up this turn.";
    }
}

package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.controller.TurnLogic;
import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;

public class Athena extends GodCard {

    public Athena(){
        super();
        name = getClass().getSimpleName();
        maxPlayersNumber = 3;
        timingName = "Opponents' Turn";
        timing = PhaseType.STANDBY_PHASE_3;
        mandatory = true;
        needParameters = false;
    }

    @Override
    public void invokeGod(Match match, Player invoker, Message message, TurnLogic turnManager) {
        System.out.println("potere di " + name + " attivato");
    }

    public static String toStringEffect(GodCard card) {
        return card.getName() + "\n" + card.getTimingName() + ": If one of your Workers moved up on your last turn, \n" +
                "opponent Workers cannot move up this turn.";
    }
}

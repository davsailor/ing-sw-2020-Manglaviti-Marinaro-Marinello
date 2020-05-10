package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.controller.TurnLogic;
import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;

public class Zeus extends GodCard {

    public Zeus(){
        super();
        name = getClass().getSimpleName();
        maxPlayersNumber = 3;
        timingName = "Your Build";
        timing = PhaseType.STANDBY_PHASE_2;
        mandatory = false;
        needParameters = true;
    }

    @Override
    public void invokeGod(Match match, Player invoker, Message message, TurnLogic turnManager) {
        System.out.println("potere di " + name + " attivato");
    }

    public static String toStringEffect(GodCard card) {
        return card.getName() + "\n" + card.getTimingName() + ": Your Worker may build under itself in its current\n" +
                "space, forcing it up one level. You do not win by forcing yourself up to the third level.";
    }
}

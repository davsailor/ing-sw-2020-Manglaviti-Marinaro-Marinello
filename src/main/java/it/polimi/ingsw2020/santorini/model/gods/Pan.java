package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;

public class Pan extends GodCard {

    public Pan(){
        super();
        name = getClass().getSimpleName();
        maxPlayersNumber = 3;
        timingName = "Win Condition";
        timing = PhaseType.STANDBY_PHASE_2;
        mandatory = true;
        willEnded = false;
        needParameters = false;
    }

    @Override
    public void invokeGod(Match match, Player invoker, Message message) {
        System.out.println("potere di " + name + " attivato");
    }

    public static String toStringEffect(GodCard card) {
        return card.getName() + "\n" + card.getTimingName() + ": You also win if your Worker moves down two or more levels.";
    }
}

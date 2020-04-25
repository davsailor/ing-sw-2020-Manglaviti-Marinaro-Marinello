package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.model.Board;
import it.polimi.ingsw2020.santorini.model.GodCard;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.utils.PhaseType;

public class Pan extends GodCard {

    public Pan(){
        super();
        name = getClass().getSimpleName();
        maxPlayersNumber = 3;
        timingName = "Win Condition";
        timing = PhaseType.STANDBY_PHASE_2;
        mandatory = true;
    }

    @Override
    public void invokeGod(Board field, Player invoker) {

    }

    public static String toStringEffect(GodCard card) {
        return card.getName() + "\n" + card.getTimingName() + ": You also win if your Worker moves down two or more levels.";
    }
}

package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.controller.TurnLogic;
import it.polimi.ingsw2020.santorini.exceptions.IllegalConstructionException;
import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.DemeterParamMessage;

public class Demeter extends GodCard {

    public Demeter(){
        name = getClass().getSimpleName();
        maxPlayersNumber = 3;
        timing = PhaseType.STANDBY_PHASE_3;
        mandatory = false;
        needParameters = true;
    }

    @Override
    public boolean canActivate(Match match) {
        for(int i = 0; i < 3; ++i)
            for(int j = 0; j < 3; ++j)
                if(match.getCurrentPlayer().getPlayingBuilder().getPossibleBuildings()[i][j] < 3 && match.getCurrentPlayer().getPlayingBuilder().getPossibleBuildings()[i][j] >= 0)
                    return true;
        return false;
    }

    @Override
    public void invokeGod(Match match, Message message, TurnLogic turnManager) {
        DemeterParamMessage param = message.deserializeDemeterParamMessage();
        try {
            match.getCurrentPlayer().getPlayingBuilder().build(param.getDirection(), match);
        } catch (IllegalConstructionException e) {
            e.printStackTrace();
        }
        System.out.println("potere di " + name + " attivato");
    }

    public static String toStringEffect(GodCard card) {
        return "Your Build" + ": Your Worker may build one additional time,\n" +
                "but not on the same space.";
    }
}

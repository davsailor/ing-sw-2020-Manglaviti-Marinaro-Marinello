package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.controller.TurnLogic;
import it.polimi.ingsw2020.santorini.exceptions.InvalidParametersException;
import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.AtlasParamMessage;

public class Atlas extends GodCard {

    public Atlas(){
        super();
        name = getClass().getSimpleName();
        maxPlayersNumber = 3;
        timingName = "Your Build";
        timing = PhaseType.STANDBY_PHASE_2;
        mandatory = false;
        willEnded = false;
        needParameters = true;
    }

    @Override
    public void invokeGod(Match match, Player invoker, Message message, TurnLogic turnManager)  throws InvalidParametersException {
        AtlasParamMessage param = message.deserializeAtlasParamMessage();
        checkParam(param);
        System.out.println("potere di " + name + " attivato");
    }

    private void checkParam(AtlasParamMessage param) throws InvalidParametersException {
        String error = null;
        // controlli: se i controlli non vanno a buon fine:
        throw new InvalidParametersException(error);
    }

    public static String toStringEffect(GodCard card) {
        return card.getName() + "\n" + card.getTimingName() + ": Your Worker may build a dome\n" +
                "at any level including the ground.";
    }
}

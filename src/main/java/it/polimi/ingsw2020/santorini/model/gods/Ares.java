package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.controller.TurnLogic;
import it.polimi.ingsw2020.santorini.exceptions.InvalidParametersException;
import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.AresParamMessage;

public class Ares extends GodCard {

    public Ares(){
        super();
        name = getClass().getSimpleName();
        maxPlayersNumber = 3;
        timingName = "End of Your Turn";
        timing = PhaseType.STANDBY_PHASE_3;
        mandatory = false;
        willEnded = false;
        needParameters = true;
    }

    @Override
    public void invokeGod(Match match, Player invoker, Message message, TurnLogic turnManager) throws InvalidParametersException {
        AresParamMessage param = message.deserializeAresParamMessage();
        checkParam(param);
        System.out.println("potere di " + name + " attivato");
    }

    private void checkParam(AresParamMessage param) throws InvalidParametersException {
        String error = null;
        // controlli: se i controlli non vanno a buon fine:
        throw new InvalidParametersException(error);
    }

    public static String toStringEffect(GodCard card) {
        return card.getName() + "\n" + card.getTimingName() + ": You may remove an unoccupied block\n" +
                "(not dome) neighboring your unmoved Worker";
    }
}

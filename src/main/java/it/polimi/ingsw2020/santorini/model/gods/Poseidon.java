package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.controller.TurnLogic;
import it.polimi.ingsw2020.santorini.exceptions.InvalidParametersException;
import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.PoseidonParamMessage;

public class Poseidon extends GodCard {

    public Poseidon(){
        super();
        name = getClass().getSimpleName();
        maxPlayersNumber = 3;
        timingName = "End of Your Turn";
        timing = PhaseType.STANDBY_PHASE_3;
        mandatory = false;
        needParameters = true;
    }

    @Override
    public void invokeGod(Match match, Player invoker, Message message, TurnLogic turnManager) throws InvalidParametersException {
        PoseidonParamMessage param = message.deserializePoseidonParamMessage();
        checkParam(param);
        System.out.println("potere di " + name + " attivato");
    }

    private void checkParam(PoseidonParamMessage param) throws InvalidParametersException {
        String error = null;
        // controlli: se i controlli non vanno a buon fine:
        throw new InvalidParametersException(error);
    }

    public static String toStringEffect(GodCard card) {
        return card.getName() + "\n" + card.getTimingName() + ": If your unmoved Worker is on the ground level,\n" +
                "it may build up to three times in neighboring spaces.";
    }
}

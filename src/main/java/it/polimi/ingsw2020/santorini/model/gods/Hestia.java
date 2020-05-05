package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.exceptions.InvalidParametersException;
import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.HestiaParamMessage;

public class Hestia extends GodCard {

    public Hestia(){
        super();
        name = getClass().getSimpleName();
        maxPlayersNumber = 3;
        timingName = "Your Build";
        timing = PhaseType.STANDBY_PHASE_3;
        mandatory = false;
        willEnded = false;
        needParameters = true;
    }

    @Override
    public void invokeGod(Match match, Player invoker, Message message) throws InvalidParametersException {
        HestiaParamMessage param = message.deserializeHestiaParamMessage();
        checkParam(param);
        System.out.println("potere di " + name + " attivato");
    }

    private void checkParam(HestiaParamMessage param) throws InvalidParametersException {
        // controlli: se i controlli non vanno a buon fine:
        throw new InvalidParametersException();
    }

    public static String toStringEffect(GodCard card) {
        return card.getName() + "\n" + card.getTimingName() + ": Your Worker may build one additional time.\n" +
                "The additional build cannot be on a perimeter space.";
    }
}

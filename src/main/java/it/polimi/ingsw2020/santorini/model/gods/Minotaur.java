package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.exceptions.InvalidParametersException;
import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.MinotaurParamMessage;

public class Minotaur extends GodCard {

    public Minotaur(){
        super();
        name = getClass().getSimpleName();
        maxPlayersNumber = 3;
        timingName = "Your Move";
        timing = PhaseType.STANDBY_PHASE_1;
        mandatory = false;
        willEnded = false;
        needParameters = true;
    }

    @Override
    public void invokeGod(Match match, Player invoker, Message message) throws InvalidParametersException {
        MinotaurParamMessage param = message.deserializeMinotaurParamMessage();
        checkParam(param);
        System.out.println("potere di " + name + " attivato");
    }

    private void checkParam(MinotaurParamMessage param) throws InvalidParametersException {
        // controlli: se i controlli non vanno a buon fine:
        throw new InvalidParametersException();
    }

    public static String toStringEffect(GodCard card) {
        return card.getName() + "\n" + card.getTimingName() + ": Your Worker may move into an opponent Worker’s\n" +
                "space (using normal movement rules), if the next space in the same direction is\n" +
                "unoccupied. Their Worker is forced into that space (regardless of its level).";
    }
}

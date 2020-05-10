package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.controller.TurnLogic;
import it.polimi.ingsw2020.santorini.exceptions.InvalidParametersException;
import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.ApolloParamMessage;

public class Apollo extends GodCard {

    public Apollo(){
        super();
        name = getClass().getSimpleName();
        maxPlayersNumber = 3;
        timingName = "Your Move";
        timing = PhaseType.STANDBY_PHASE_1;
        mandatory = false;
        needParameters = true;
    }


    @Override
    public void invokeGod(Match match, Player invoker, Message message, TurnLogic turnManager) throws InvalidParametersException {
        ApolloParamMessage param = message.deserializeApolloParamMessage(message.getSerializedPayload());
        //checkParam(param, match);
        System.out.println("potere di " + name + " attivato");
    }

    private void checkParam(ApolloParamMessage param, Match match) throws InvalidParametersException {
        StringBuilder errorBuilder = new StringBuilder();
        String error = null;
        boolean toThrow = false;
        // controlli: se i controlli non vanno a buon fine:
        if (toThrow) throw new InvalidParametersException(error);
    }

    public static String toStringEffect(GodCard card) {
        return card.getName() + "\n" + card.getTimingName() + ": Your Worker may move into an opponent Worker’s space\n" +
                "(using normal movement rules) and force their Worker to the space yours\n" +
                "just vacated (swapping their positions).";
    }
}

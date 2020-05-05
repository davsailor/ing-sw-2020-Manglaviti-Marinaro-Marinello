package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.exceptions.InvalidParametersException;
import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.PrometheusParamMessage;

public class Prometheus extends GodCard {

    public Prometheus(){
        super();
        name = getClass().getSimpleName();
        maxPlayersNumber = 3;
        timingName = "Your Turn";
        timing = PhaseType.STANDBY_PHASE_1;
        mandatory = false;
        willEnded = false;
        needParameters = true;
    }

    @Override
    public void invokeGod(Match match, Player invoker, Message message) throws InvalidParametersException {
        PrometheusParamMessage param = message.deserializePrometheusParamMessage();
        checkParam(param);
        System.out.println("potere di " + name + " attivato");
    }

    private void checkParam(PrometheusParamMessage param) throws InvalidParametersException {
        // controlli: se i controlli non vanno a buon fine:
        throw new InvalidParametersException();
    }

    public static String toStringEffect(GodCard card) {
        return card.getName() + "\n" + card.getTimingName() + ": If your Worker does not move up,\n" +
                "it may build both before and after moving.";
    }
}

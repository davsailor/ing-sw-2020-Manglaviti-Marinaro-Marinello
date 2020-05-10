package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.controller.TurnLogic;
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
        needParameters = true;
    }

    @Override
    public void invokeGod(Match match, Player invoker, Message message, TurnLogic turnManager) {
        PrometheusParamMessage param = message.deserializePrometheusParamMessage();
        System.out.println("potere di " + name + " attivato");
    }

    public static String toStringEffect(GodCard card) {
        return card.getName() + "\n" + card.getTimingName() + ": If your Worker does not move up,\n" +
                "it may build both before and after moving.";
    }
}

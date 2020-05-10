package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.controller.TurnLogic;
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
        needParameters = true;
    }

    @Override
    public void invokeGod(Match match, Player invoker, Message message, TurnLogic turnManager) {
        AresParamMessage param = message.deserializeAresParamMessage();
        System.out.println("potere di " + name + " attivato");
    }

    public static String toStringEffect(GodCard card) {
        return card.getName() + "\n" + card.getTimingName() + ": You may remove an unoccupied block\n" +
                "(not dome) neighboring your unmoved Worker";
    }
}

package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.controller.TurnLogic;
import it.polimi.ingsw2020.santorini.exceptions.IllegalConstructionException;
import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.PrometheusParamMessage;

public class Prometheus extends GodCard {

    public Prometheus(){
        name = getClass().getSimpleName();
        maxPlayersNumber = 3;
        timingName = "Your Turn";
        timing = PhaseType.STANDBY_PHASE_1;
        mandatory = false;
        needParameters = true;
    }

    @Override
    public boolean canActivate(Match match){
        return(match.getCurrentPlayer().getBuilderM().canBuild() || match.getCurrentPlayer().getBuilderF().canBuild());
    }

    @Override
    public void invokeGod(Match match, Message message, TurnLogic turnManager) {
        PrometheusParamMessage param = message.deserializePrometheusParamMessage();
        if(param.getBuilderSex() == 'M')
            match.getCurrentPlayer().setPlayingBuilder(match.getCurrentPlayer().getBuilderM());
        else
            match.getCurrentPlayer().setPlayingBuilder(match.getCurrentPlayer().getBuilderF());

        try {
            match.getCurrentPlayer().getPlayingBuilder().build(param.getDirection());
        } catch (IllegalConstructionException ignored){}

        match.getCurrentPlayer().setRiseActions(false);
        match.getCurrentPlayer().setMoveActions(true);

        System.out.println("potere di " + name + " attivato");
    }

    public static String toStringEffect(GodCard card) {
        return card.getName() + "\n" + card.getTimingName() + ": If your Worker does not move up,\n" +
                "it may build both before and after moving.";
    }
}

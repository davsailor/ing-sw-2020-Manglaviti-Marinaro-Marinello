package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.controller.TurnLogic;
import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;

public class Athena extends GodCard {

    public Athena(){
        name = getClass().getSimpleName();
        maxPlayersNumber = 3;
        timing = PhaseType.STANDBY_PHASE_3;
        mandatory = true;
        needParameters = false;
    }

    @Override
    public boolean canActivate(Match match) {
        return match.getCurrentPlayer().getPlayingBuilder().isRisedThisTurn();
    }

    @Override
    public void invokeGod(Match match, Message message, TurnLogic turnManager) {
        for(Player p : match.getPlayers()){
            if(!p.getNickname().equals(match.getCurrentPlayer().getNickname())){
                p.setRiseActions(false);
            }
        }
        System.out.println("potere di " + name + " attivato");
    }

    public static String toStringEffect(GodCard card) {
        return "Opponents' Turn" + ": If one of your Workers moved up on your last turn, \n" +
                "opponent Workers cannot move up this turn.";
    }
}

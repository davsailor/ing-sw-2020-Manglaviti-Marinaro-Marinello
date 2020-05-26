package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.controller.TurnLogic;
import it.polimi.ingsw2020.santorini.exceptions.EndMatchException;
import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;

public class Pan extends GodCard {

    public Pan(){
        name = getClass().getSimpleName();
        maxPlayersNumber = 3;
        timingName = "Win Condition";
        timing = PhaseType.STANDBY_PHASE_2;
        mandatory = true;
        needParameters = false;
    }

    @Override
    public boolean canActivate(Match match){
        return true;
    }

    @Override
    public void invokeGod(Match match, Message message, TurnLogic turnManager) throws EndMatchException {
        System.out.println("potere di " + name + " attivato");
        int[][] moveMatrix = match.getCurrentPlayer().getPlayingBuilder().getPossibleMoves();
        for(int k = 0; k < 3; ++k)
            for(int j = 0; j < 3; ++j)
                if(moveMatrix[k][j] == 4) {
                    if (match.getBoard().getBoard()[match.getCurrentPlayer().getPlayingBuilder().getPosX() - 1 + k][match.getCurrentPlayer().getPlayingBuilder().getPosY() - 1 + j]
                            .calculateJump(match.getBoard().getBoard()[match.getCurrentPlayer().getPlayingBuilder().getPosX()][match.getCurrentPlayer().getPlayingBuilder().getPosY()]) <= -2)
                        match.currentWins();
                    return;
                }
    }

    public static String toStringEffect(GodCard card) {
        return card.getTimingName() + ": You also win if your Worker moves down two or more levels.";
    }
}

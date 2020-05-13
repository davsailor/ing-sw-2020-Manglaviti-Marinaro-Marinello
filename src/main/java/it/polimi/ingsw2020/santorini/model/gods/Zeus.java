package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.controller.TurnLogic;
import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.utils.ActionType;
import it.polimi.ingsw2020.santorini.utils.LevelType;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;

public class Zeus extends GodCard {

    public Zeus(){
        name = getClass().getSimpleName();
        maxPlayersNumber = 3;
        timingName = "Your Build";
        timing = PhaseType.STANDBY_PHASE_2;
        mandatory = false;
        needParameters = true;
    }

    @Override
    public boolean canActivate(Match match){
        return match.getBoard().getBoard()[match.getCurrentPlayer().getPlayingBuilder().getPosX()][match.getCurrentPlayer().getPlayingBuilder().getPosY()].getLevel().getHeight() < 3;
    }

    @Override
    public void invokeGod(Match match, Message message, TurnLogic turnManager) {
        System.out.println("potere di " + name + " attivato");
        switch(match.getBoard().getBoard()[match.getCurrentPlayer().getPlayingBuilder().getPosX()][match.getCurrentPlayer().getPlayingBuilder().getPosY()].getLevel()){
            case GROUND:
                match.getBoard().getBoard()[match.getCurrentPlayer().getPlayingBuilder().getPosX()][match.getCurrentPlayer().getPlayingBuilder().getPosY()].setLevel(LevelType.BASE);
                break;
            case BASE:
                match.getBoard().getBoard()[match.getCurrentPlayer().getPlayingBuilder().getPosX()][match.getCurrentPlayer().getPlayingBuilder().getPosY()].setLevel(LevelType.MID);
                break;
            case MID:
                match.getBoard().getBoard()[match.getCurrentPlayer().getPlayingBuilder().getPosX()][match.getCurrentPlayer().getPlayingBuilder().getPosY()].setLevel(LevelType.TOP);
                break;
        }
        turnManager.getRemainingActions().remove(ActionType.SELECT_CELL_BUILD);
        turnManager.getRemainingActions().remove(ActionType.BUILD);
    }

    public static String toStringEffect(GodCard card) {
        return card.getName() + "\n" + card.getTimingName() + ": Your Worker may build under itself in its current\n" +
                "space, forcing it up one level. You do not win by forcing yourself up to the third level.";
    }
}

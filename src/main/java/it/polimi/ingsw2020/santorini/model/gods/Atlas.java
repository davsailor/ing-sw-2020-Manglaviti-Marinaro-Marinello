package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.controller.TurnLogic;
import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.utils.*;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.AtlasParamMessage;

public class Atlas extends GodCard {

    public Atlas(){
        name = getClass().getSimpleName();
        maxPlayersNumber = 3;
        timing = PhaseType.STANDBY_PHASE_2;
        mandatory = false;
        needParameters = true;
    }

    @Override
    public boolean canActivate(Match match) {
        return match.getCurrentPlayer().getPlayingBuilder().canBuild();
    }


    @Override
    public void invokeGod(Match match, Message message, TurnLogic turnManager) {
        AtlasParamMessage param = message.deserializeAtlasParamMessage();

        Cell target = match.getBoard().getBoard()[match.getCurrentPlayer().getPlayingBuilder().getPosX()][match.getCurrentPlayer().getPlayingBuilder().getPosY()-1];

        switch(param.getDirection()){
            case NORTH:
                target = match.getBoard().getBoard()[match.getCurrentPlayer().getPlayingBuilder().getPosX()-1][match.getCurrentPlayer().getPlayingBuilder().getPosY()];
                break;
            case NORTH_WEST:
                target = match.getBoard().getBoard()[match.getCurrentPlayer().getPlayingBuilder().getPosX()-1][match.getCurrentPlayer().getPlayingBuilder().getPosY()-1];
                break;
            case NORTH_EAST:
                target = match.getBoard().getBoard()[match.getCurrentPlayer().getPlayingBuilder().getPosX()-1][match.getCurrentPlayer().getPlayingBuilder().getPosY()+1];
                break;
            case WEST:
                target = match.getBoard().getBoard()[match.getCurrentPlayer().getPlayingBuilder().getPosX()][match.getCurrentPlayer().getPlayingBuilder().getPosY()-1];
                break;
            case EAST:
                target = match.getBoard().getBoard()[match.getCurrentPlayer().getPlayingBuilder().getPosX()][match.getCurrentPlayer().getPlayingBuilder().getPosY()+1];
                break;
            case SOUTH:
                target = match.getBoard().getBoard()[match.getCurrentPlayer().getPlayingBuilder().getPosX()+1][match.getCurrentPlayer().getPlayingBuilder().getPosY()];
                break;
            case SOUTH_EAST:
                target = match.getBoard().getBoard()[match.getCurrentPlayer().getPlayingBuilder().getPosX()+1][match.getCurrentPlayer().getPlayingBuilder().getPosY()+1];
                break;
            case SOUTH_WEST:
                target = match.getBoard().getBoard()[match.getCurrentPlayer().getPlayingBuilder().getPosX()+1][match.getCurrentPlayer().getPlayingBuilder().getPosY()-1];
                break;
        }

        if(target.getLevel() == LevelType.TOP) match.setNumberOfCompletedTowers(match.getNumberOfCompletedTowers() + 1);
        target.setLevel(LevelType.DOME);
        target.setStatus(AccessType.DOME);

        turnManager.getRemainingActions().remove(ActionType.SELECT_CELL_BUILD);
        turnManager.getRemainingActions().remove(ActionType.BUILD);

        System.out.println("potere di " + name + " attivato");
    }

    public static String toStringEffect(GodCard card) {
        return "Your Build" + ": Your Worker may build a dome\n" +
                "at any level including the ground.";
    }
}

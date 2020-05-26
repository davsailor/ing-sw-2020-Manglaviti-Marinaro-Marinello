package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.controller.TurnLogic;
import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.AresParamMessage;

public class Ares extends GodCard {

    public Ares(){
        name = getClass().getSimpleName();
        maxPlayersNumber = 3;
        timingName = "End of Your Turn";
        timing = PhaseType.STANDBY_PHASE_3;
        mandatory = false;
        needParameters = true;
    }

    @Override
    public boolean canActivate(Match match) {
        Builder demolitionBuilder;

        if(match.getCurrentPlayer().getPlayingBuilder().getGender() == '\u2642')
            demolitionBuilder = match.getCurrentPlayer().getBuilderF();
        else
            demolitionBuilder = match.getCurrentPlayer().getBuilderM();

        int[][] neighboringLevelMatrix = Board.neighboringLevelCell(demolitionBuilder);

        for(int i = 0; i < 3; ++i)
            for(int j = 0; j < 3; ++j)
                if(neighboringLevelMatrix[i][j] > 0 && neighboringLevelMatrix[i][j] < 4) return true;

        return false;
    }

    @Override
    public void invokeGod(Match match, Message message, TurnLogic turnManager) {
        AresParamMessage param = message.deserializeAresParamMessage();
        Builder demolitionBuilder;

        if(param.getDemolitionBuilderSex() == 'M') demolitionBuilder = match.getCurrentPlayer().getBuilderM();
        else demolitionBuilder = match.getCurrentPlayer().getBuilderF();

        Cell target = match.getBoard().getBoard()[demolitionBuilder.getPosX()][demolitionBuilder.getBuildPosY()-1];

        switch(param.getTargetedBlock()){
            case NORTH:
                target = match.getBoard().getBoard()[demolitionBuilder.getPosX()-1][demolitionBuilder.getPosY()];
                break;
            case NORTH_WEST:
                target = match.getBoard().getBoard()[demolitionBuilder.getPosX()-1][demolitionBuilder.getPosY()-1];
                break;
            case NORTH_EAST:
                target = match.getBoard().getBoard()[demolitionBuilder.getPosX()-1][demolitionBuilder.getPosY()+1];
                break;
            case WEST:
                target = match.getBoard().getBoard()[demolitionBuilder.getPosX()][demolitionBuilder.getPosY()-1];
                break;
            case EAST:
                target = match.getBoard().getBoard()[demolitionBuilder.getPosX()][demolitionBuilder.getPosY()+1];
                break;
            case SOUTH:
                target = match.getBoard().getBoard()[demolitionBuilder.getPosX()+1][demolitionBuilder.getPosY()];
                break;
            case SOUTH_EAST:
                target = match.getBoard().getBoard()[demolitionBuilder.getPosX()+1][demolitionBuilder.getPosY()+1];
                break;
            case SOUTH_WEST:
                target = match.getBoard().getBoard()[demolitionBuilder.getPosX()+1][demolitionBuilder.getPosY()-1];
                break;
        }

        target.demolish();

        System.out.println("potere di " + name + " attivato");
    }

    public static String toStringEffect(GodCard card) {
        return card.getTimingName() + ": You may remove an unoccupied block\n" +
                "(not dome) neighboring your unmoved Worker";
    }
}

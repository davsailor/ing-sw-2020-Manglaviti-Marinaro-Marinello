package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.controller.TurnLogic;
import it.polimi.ingsw2020.santorini.exceptions.IllegalConstructionException;
import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.utils.ActionType;
import it.polimi.ingsw2020.santorini.utils.LevelType;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.PoseidonParamMessage;

public class Poseidon extends GodCard {

    public Poseidon(){
        name = getClass().getSimpleName();
        maxPlayersNumber = 3;
        timingName = "End of Your Turn";
        timing = PhaseType.STANDBY_PHASE_3;
        mandatory = false;
        needParameters = true;
    }

    @Override
    public boolean canActivate(Match match){
        Builder unmoved;
        if(match.getCurrentPlayer().getPlayingBuilder().getGender() == 'M')
            unmoved = match.getCurrentPlayer().getBuilderF();
        else
            unmoved = match.getCurrentPlayer().getBuilderM();
        if(match.getBoard().getBoard()[unmoved.getPosX()][unmoved.getPosY()].getLevel() == LevelType.GROUND){
            int[][] matrix = Board.neighboringLevelCell(unmoved);
            for(int i = 0; i < 3; ++i)
                for(int j = 0; j < 3; ++j)
                    if(matrix[i][j] >= 0 && matrix[i][j] < 4) return true;
        }
        return false;
    }

    @Override
    public void invokeGod(Match match, Message message, TurnLogic turnManager){
        PoseidonParamMessage param = message.deserializePoseidonParamMessage();
        System.out.println("potere di " + name + " attivato");
        Builder constructionBuilder;
        if(param.getConstructionGender() == 'M')
            constructionBuilder = match.getCurrentPlayer().getBuilderM();
        else
            constructionBuilder = match.getCurrentPlayer().getBuilderF();
        for(int i = 0; i < param.getNumberOfBuild(); ++i) {
            try {
                constructionBuilder.build(param.getDirection().get(i));
            } catch (IllegalConstructionException e) {
                e.printStackTrace();
            }
        }
        turnManager.getRemainingActions().remove(ActionType.SELECT_CELL_BUILD);
        turnManager.getRemainingActions().remove(ActionType.BUILD);
    }

    public static String toStringEffect(GodCard card) {
        return card.getName() + "\n" + card.getTimingName() + ": If your unmoved Worker is on the ground level,\n" +
                "it may build up to three times in neighboring spaces.";
    }
}

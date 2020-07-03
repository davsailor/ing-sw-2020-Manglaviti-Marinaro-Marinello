package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.controller.TurnLogic;
import it.polimi.ingsw2020.santorini.exceptions.EndMatchException;
import it.polimi.ingsw2020.santorini.exceptions.IllegalMovementException;
import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.utils.*;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.MinotaurParamMessage;

public class Minotaur extends GodCard {

    public Minotaur(){
        name = getClass().getSimpleName();
        maxPlayersNumber = 3;
        timing = PhaseType.STANDBY_PHASE_1;
        mandatory = false;
        needParameters = true;
    }

    @Override
    public boolean canActivate(Match match){
        int[][] swappingMatrix;

        swappingMatrix = Board.neighboringSwappingCell(match.getCurrentPlayer().getBuilderM(), AccessType.OCCUPIED);
        for(int i = 0; i < 3; ++i)
            for(int j = 0; j < 3; ++j)
                if(swappingMatrix[i][j] != 0)
                    try {
                        if (match.getBoard().getBoard()[match.getCurrentPlayer().getBuilderM().getPosX() + (i - 1) * 2][match.getCurrentPlayer().getBuilderM().getPosY() + (j - 1) * 2].getStatus() == AccessType.FREE &&
                                match.getBoard().getBoard()[match.getCurrentPlayer().getBuilderM().getPosX() + (i - 1) * 2][match.getCurrentPlayer().getBuilderM().getPosY() + (j - 1) * 2].getLevel() != LevelType.DOME)
                            return true;
                    } catch (IndexOutOfBoundsException ignored){}

        swappingMatrix = Board.neighboringSwappingCell(match.getCurrentPlayer().getBuilderF(), AccessType.OCCUPIED);
        for(int i = 0; i < 3; ++i)
            for(int j = 0; j < 3; ++j)
                if(swappingMatrix[i][j] != 0)
                    try {
                        if (match.getBoard().getBoard()[match.getCurrentPlayer().getBuilderF().getPosX() + (i - 1) * 2][match.getCurrentPlayer().getBuilderF().getPosY() + (j - 1) * 2].getStatus() == AccessType.FREE &&
                                match.getBoard().getBoard()[match.getCurrentPlayer().getBuilderF().getPosX() + (i - 1) * 2][match.getCurrentPlayer().getBuilderF().getPosY() + (j - 1) * 2].getLevel() != LevelType.DOME)
                            return true;
                    } catch (IndexOutOfBoundsException ignored){}

        return false;
    }

    @Override
    public void invokeGod(Match match, Message message, TurnLogic turnManager) throws EndMatchException {
        MinotaurParamMessage param = message.deserializeMinotaurParamMessage();

        if(param.getPlayingBuilderSex() == 'F')
            match.getCurrentPlayer().setPlayingBuilder(match.getCurrentPlayer().getBuilderF());
        else
            match.getCurrentPlayer().setPlayingBuilder(match.getCurrentPlayer().getBuilderM());

        Builder opponent = match.getBoard().getBoard()[match.getCurrentPlayer().getPlayingBuilder().getPosX()][match.getCurrentPlayer().getPlayingBuilder().getPosY() - 1].getBuilder();
        switch(param.getOpponentBuilderDirection()){
            case NORTH:
                opponent = match.getBoard().getBoard()[match.getCurrentPlayer().getPlayingBuilder().getPosX() -1][match.getCurrentPlayer().getPlayingBuilder().getPosY()].getBuilder();
                break;
            case NORTH_EAST:
                opponent = match.getBoard().getBoard()[match.getCurrentPlayer().getPlayingBuilder().getPosX() - 1][match.getCurrentPlayer().getPlayingBuilder().getPosY() + 1].getBuilder();
                break;
            case EAST:
                opponent = match.getBoard().getBoard()[match.getCurrentPlayer().getPlayingBuilder().getPosX() ][match.getCurrentPlayer().getPlayingBuilder().getPosY() +1].getBuilder();
                break;
            case SOUTH_EAST:
                opponent = match.getBoard().getBoard()[match.getCurrentPlayer().getPlayingBuilder().getPosX() + 1][match.getCurrentPlayer().getPlayingBuilder().getPosY() + 1].getBuilder();
                break;
            case SOUTH:
                opponent = match.getBoard().getBoard()[match.getCurrentPlayer().getPlayingBuilder().getPosX() +1][match.getCurrentPlayer().getPlayingBuilder().getPosY() ].getBuilder();
                break;
            case SOUTH_WEST:
                opponent = match.getBoard().getBoard()[match.getCurrentPlayer().getPlayingBuilder().getPosX() + 1][match.getCurrentPlayer().getPlayingBuilder().getPosY() - 1].getBuilder();
                break;
            case WEST:
                opponent = match.getBoard().getBoard()[match.getCurrentPlayer().getPlayingBuilder().getPosX() ][match.getCurrentPlayer().getPlayingBuilder().getPosY() -1].getBuilder();
                break;
            case NORTH_WEST:
                opponent = match.getBoard().getBoard()[match.getCurrentPlayer().getPlayingBuilder().getPosX() - 1][match.getCurrentPlayer().getPlayingBuilder().getPosY() - 1].getBuilder();
                break;
        }
        try {
            opponent.move(param.getOpponentBuilderDirection());
            opponent.setRisedThisTurn(false);
        } catch (EndMatchException | IllegalMovementException ignored){}

        try {
            match.getCurrentPlayer().getPlayingBuilder().move(param.getOpponentBuilderDirection());
        } catch (IllegalMovementException e) {
            e.printStackTrace();
        } catch (EndMatchException e) {
            match.currentWins();
        }

        turnManager.getRemainingActions().remove(ActionType.SELECT_BUILDER);
        turnManager.getRemainingActions().remove(ActionType.SELECT_CELL_MOVE);
        turnManager.getRemainingActions().remove(ActionType.MOVE);

        System.out.println("potere di " + name + " attivato");
    }

    public static String toStringEffect(GodCard card) {
        return "Your Move" + ": Your Worker may move into an opponent Worker’s\n" +
                "space (using normal movement rules), if the next space in the same direction is\n" +
                "unoccupied. Their Worker is forced into that space (regardless of its level).";
    }
}

package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.controller.TurnLogic;
import it.polimi.ingsw2020.santorini.exceptions.EndMatchException;
import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.utils.AccessType;
import it.polimi.ingsw2020.santorini.utils.ActionType;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.ApolloParamMessage;

public class Apollo extends GodCard {

    public Apollo(){
        name = getClass().getSimpleName();
        maxPlayersNumber = 3;
        timingName = "Your Move";
        timing = PhaseType.STANDBY_PHASE_1;
        mandatory = false;
        needParameters = true;
    }

    /**
     * this function evaluate the match state and tells if the god's power can be used. The method in this case
     * the method checks if there is any opponent's builder near current player's builders, because the god swap two near builder of
     * two opponents
     * @param match is the current match that has to be evaluate
     * @return true if the god can be activated, false otherwise
     */
    @Override
    public boolean canActivate(Match match){
        int[][] swappingMatrix;
        swappingMatrix = Board.neighboringSwappingCell(match.getCurrentPlayer().getBuilderM(), AccessType.OCCUPIED);
        for(int i = 0; i < 3; ++i)
            for(int j = 0; j < 3; ++j)
                if (swappingMatrix[i][j] != 0) return true;
        swappingMatrix = Board.neighboringSwappingCell(match.getCurrentPlayer().getBuilderF(), AccessType.OCCUPIED);
        for(int i = 0; i < 3; ++i)
            for(int j = 0; j < 3; ++j)
                if (swappingMatrix[i][j] != 0) return true;
        return false;
    }

    /**
     * the function that will modify the match and its normal phases following the instructions of god
     * In Apollo case it will
     * @param match the match playing
     * @param message this message refers to possible parameters requested to players. Each overridden method can
     * deserialize correctly this message. Apollo will understand its own param message (ApolloParamMessage)
     * but will not understand other gods parameters messages
     * @param turnManager since some gods modify phases of the match, we need a reference to turnManager to correctly
     *                    modify the turn
     * @throws EndMatchException some gods (such as Pan) can make you win the game
     */
    @Override
    public void invokeGod(Match match, Message message, TurnLogic turnManager) {
        ApolloParamMessage param = message.deserializeApolloParamMessage(message.getSerializedPayload());

        if(param.getYourBuilderGender() == 'F')
            match.getCurrentPlayer().setPlayingBuilder(match.getCurrentPlayer().getBuilderF());
        else
            match.getCurrentPlayer().setPlayingBuilder(match.getCurrentPlayer().getBuilderM());

        Builder yours = match.getCurrentPlayer().getPlayingBuilder();
        Builder opponent = match.getBoard().getBoard()[yours.getPosX()][yours.getPosY()-1].getBuilder();

        switch(param.getOpponentBuilderDirection()){
            case NORTH:
                opponent = match.getBoard().getBoard()[yours.getPosX()-1][yours.getPosY()].getBuilder();
                break;
            case NORTH_WEST:
                opponent = match.getBoard().getBoard()[yours.getPosX()-1][yours.getPosY()-1].getBuilder();
                break;
            case NORTH_EAST:
                opponent = match.getBoard().getBoard()[yours.getPosX()-1][yours.getPosY()+1].getBuilder();
                break;
            case WEST:
                opponent = match.getBoard().getBoard()[yours.getPosX()][yours.getPosY()-1].getBuilder();
                break;
            case EAST:
                opponent = match.getBoard().getBoard()[yours.getPosX()][yours.getPosY()+1].getBuilder();
                break;
            case SOUTH:
                opponent = match.getBoard().getBoard()[yours.getPosX()+1][yours.getPosY()].getBuilder();
                break;
            case SOUTH_EAST:
                opponent = match.getBoard().getBoard()[yours.getPosX()+1][yours.getPosY()+1].getBuilder();
                break;
            case SOUTH_WEST:
                opponent = match.getBoard().getBoard()[yours.getPosX()+1][yours.getPosY()-1].getBuilder();
                break;
        }

        match.getCurrentPlayer().getPlayingBuilder().swapBuilders(opponent);
        turnManager.getRemainingActions().remove(ActionType.SELECT_BUILDER);
        turnManager.getRemainingActions().remove(ActionType.SELECT_CELL_MOVE);
        turnManager.getRemainingActions().remove(ActionType.MOVE);

        System.out.println("potere di " + name + " attivato");
    }

    public static String toStringEffect(GodCard card) {
        return card.getTimingName() + ": Your Worker may move into an opponent Worker’s space\n" +
                "(using normal movement rules) and force their Worker to the space yours\n" +
                "just vacated (swapping their positions).";
    }
}

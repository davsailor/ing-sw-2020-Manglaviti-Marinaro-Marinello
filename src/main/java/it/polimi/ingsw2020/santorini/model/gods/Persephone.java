package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.controller.TurnLogic;
import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.utils.AccessType;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;

public class Persephone extends GodCard {

    private boolean activated = false;
    private String invoker = null;
    private boolean set = false;

    public Persephone(){
        name = getClass().getSimpleName();
        maxPlayersNumber = 3;
        timingName = "Opponents' Turn";
        timing = PhaseType.STANDBY_PHASE_1;
        mandatory = true;
        needParameters = false;
    }

    public String getInvoker() {
        return invoker;
    }

    @Override
    public boolean canActivate(Match match){
        if(!activated) {
            activated = true;
            invoker = match.getCurrentPlayer().getNickname();
            return true;
        }
        return false;
    }

    @Override
    public void invokeGod(Match match, Message message, TurnLogic turnManager) {
        System.out.println("potere di " + name + " attivato");
        if(!set) {
            try {
                turnManager.setPersephone(this);
                turnManager.setPersephoneEffect(Persephone.class.getMethod("invokeGod", Match.class, Message.class, TurnLogic.class));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            set = true;
        }
        int[][] matrix;
        for(Player p : match.getPlayers()){
            if(!p.getNickname().equals(invoker)){
                matrix = Board.neighboringStatusCell(p.getBuilderF(), AccessType.FREE);
                for(int i = 0; i < 3; ++i)
                    for(int j = 0; j < 3; ++j)
                        if(matrix[i][j] == 2){
                            p.setMoveActions(false);
                            p.setRiseActions(true);
                            break;
                        }

                matrix = Board.neighboringStatusCell(p.getBuilderM(), AccessType.FREE);
                for(int i = 0; i < 3; ++i)
                    for(int j = 0; j < 3; ++j)
                        if(matrix[i][j] == 2){
                            p.setMoveActions(false);
                            p.setRiseActions(true);
                            break;
                        }
            }
        }
    }

    public static String toStringEffect(GodCard card) {
        return card.getName() + "\n" + card.getTimingName() + ": If possible, at least one Worker must move up this turn.";
    }
}

package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.controller.TurnLogic;
import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.utils.LevelType;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;

public class Hephaestus extends GodCard {

    public Hephaestus(){
        name = getClass().getSimpleName();
        maxPlayersNumber = 3;
        timingName = "Your Build";
        timing = PhaseType.STANDBY_PHASE_3;
        mandatory = false;
        needParameters = false;
    }

    @Override
    public boolean canActivate(Match match) {
        return match.getBoard().getBoard()[match.getCurrentPlayer().getPlayingBuilder().getBuildPosX()][match.getCurrentPlayer().getPlayingBuilder().getBuildPosY()].getLevel() != LevelType.DOME;
    }

    @Override
    public void invokeGod(Match match, Message message, TurnLogic turnManager) {
        Cell target = match.getBoard().getBoard()[match.getCurrentPlayer().getPlayingBuilder().getBuildPosX()][match.getCurrentPlayer().getPlayingBuilder().getBuildPosY()];
        switch(target.getLevel()){
            case BASE:
                target.setLevel(LevelType.MID);
                break;
            case MID:
                target.setLevel(LevelType.TOP);
                break;
            case TOP:
                target.setLevel(LevelType.DOME);
                break;
        }
        System.out.println("potere di " + name + " attivato");
    }

    public static String toStringEffect(GodCard card) {
        return card.getName() + "\n" + card.getTimingName() + ": Your Worker may build one additional block\n" +
                "(not dome) on top of your first block.";
    }
}

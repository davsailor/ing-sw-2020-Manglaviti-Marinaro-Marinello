package it.polimi.ingsw2020.santorini.controller;

import it.polimi.ingsw2020.santorini.exceptions.InvalidParametersException;
import it.polimi.ingsw2020.santorini.model.GodCard;
import it.polimi.ingsw2020.santorini.model.Match;
import it.polimi.ingsw2020.santorini.utils.ActionType;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.errors.InvalidParametersMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.UpdateMessage;

import java.util.ArrayList;

public class ActionLogic {
    /**
     * filtra e gestisce i messaggi che richiedono un movimento o una costruzione o una invocazione divina
     */
    private TurnLogic turnManager;

    public ActionLogic(TurnLogic turnManager) {
        this.turnManager = turnManager;
    }

    public TurnLogic getTurnManager() {
        return turnManager;
    }

    public ArrayList<Message> invocation(Match match, String caller, Message message) {
        ArrayList<Message> listToSend = new ArrayList<>();
        GodCard god = match.getPlayerByName(caller).getDivinePower();
        try{
            god.invokeGod(match, match.getPlayerByName(caller), message);
            for(int i = 0; i < match.getNumberOfPlayers(); ++i){
                Message sendMessage = new Message(match.getPlayers()[i].getNickname());
                message.buildUpdateMessage(new UpdateMessage(match, turnManager.getPhase()));
                listToSend.add(message);
            }
            if(god.isWillEnded()) {
                turnManager.getRemainingActions().remove(ActionType.ACTIVATE_GOD);
                turnManager.nextPhase();
            }
        } catch (InvalidParametersException e) {
            Message error = new Message(caller);
            error.buildInvalidParametersMessage(new InvalidParametersMessage(god.getName()));
            listToSend.add(message);
        }
        return listToSend;
    }
}

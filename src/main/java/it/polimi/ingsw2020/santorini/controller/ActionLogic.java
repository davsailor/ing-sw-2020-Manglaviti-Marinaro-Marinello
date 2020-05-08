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

    /**
     * Getter of the attribute turnManager
     * @return the attribute turnManager
     */
    public TurnLogic getTurnManager() {
        return turnManager;
    }

    /**
     * The method invokes the power of the player’s god
     * @param match is the match that the controller is controlling
     * @param caller is the username of the player invoking the god
     * @param message is the match which asks to invoke the god
     * @return an Array List of Message which contains messages to notify the use of the god
     */
    public ArrayList<Message> invocation(Match match, String caller, Message message) {
        ArrayList<Message> listToSend = new ArrayList<>();
        GodCard god = match.getPlayerByName(caller).getDivinePower();
        try{
            god.invokeGod(match, match.getPlayerByName(caller), message);
            for(int i = 0; i < match.getNumberOfPlayers(); ++i){
                Message sendMessage = new Message(match.getPlayers()[i].getNickname());
                sendMessage.buildUpdateMessage(new UpdateMessage(match, turnManager.getPhase()));
                listToSend.add(sendMessage);
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

    /**
     * The method builds an ArrayList of Messages
     * @param match is the reference of the match controlled by the controller
     * @param caller is the username of the player that required the move
     * @param message is the ?
     * @return the ArrayList created
     */
    public ArrayList<Message> move(Match match, String caller, Message message) {
        ArrayList<Message> listToSend = new ArrayList<>();
        return listToSend;
    }
}

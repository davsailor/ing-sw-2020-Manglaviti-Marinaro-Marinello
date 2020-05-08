package it.polimi.ingsw2020.santorini.controller;

import it.polimi.ingsw2020.santorini.exceptions.IllegalConstructionException;
import it.polimi.ingsw2020.santorini.exceptions.IllegalMovementException;
import it.polimi.ingsw2020.santorini.exceptions.InvalidParametersException;
import it.polimi.ingsw2020.santorini.model.GodCard;
import it.polimi.ingsw2020.santorini.model.Match;
import it.polimi.ingsw2020.santorini.utils.ActionType;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.actions.SelectedBuildingMessage;
import it.polimi.ingsw2020.santorini.utils.messages.actions.SelectedMoveMessage;
import it.polimi.ingsw2020.santorini.utils.messages.errors.GenericErrorMessage;
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
            god.invokeGod(match, match.getPlayerByName(caller), message, turnManager);
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
            error.buildInvalidParametersMessage(new InvalidParametersMessage(god.getName(), e.getError()));
            listToSend.add(error);
        }
        return listToSend;
    }

    /**
     * The method handles the movement of the builder
     * @param match is the reference of the match controlled by the controller
     * @param caller is the username of the player that required the move
     * @param message contains the direction of the movement
     * @return the ArrayList created contains the message for the next phases of the turn
     */
    public ArrayList<Message> move(Match match, String caller, SelectedMoveMessage message) {
        ArrayList<Message> listToSend = new ArrayList<>();
        try {
            match.getPlayerByName(caller).getPlayingBuilder().move(message.getDirection());
            for(int i = 0; i < match.getNumberOfPlayers(); ++i){
                Message sendMessage = new Message(match.getPlayers()[i].getNickname());
                sendMessage.buildUpdateMessage(new UpdateMessage(match, turnManager.getPhase()));
                listToSend.add(sendMessage);
            }
        } catch (IllegalMovementException e) {
            Message error = new Message(caller);
            error.buildInvalidMoveMessage(new GenericErrorMessage(e.getError()));
            listToSend.add(error);
        }
        return listToSend;
    }

    /**
     * The method handles the construction of the blocks
     * @param match is the reference of the match controlled by the controller
     * @param caller is the username of the player that required the move
     * @param message contains the direction of construction
     * @return the ArrayList created contains the message for the next phases of the turn
     */
    public ArrayList<Message> build(Match match, String caller, SelectedBuildingMessage message) {
        ArrayList<Message> listToSend = new ArrayList<>();
        try {
            match.getPlayerByName(caller).getPlayingBuilder().build(message.getDirection());
            for(int i = 0; i < match.getNumberOfPlayers(); ++i){
                Message sendMessage = new Message(match.getPlayers()[i].getNickname());
                sendMessage.buildUpdateMessage(new UpdateMessage(match, turnManager.getPhase()));
                listToSend.add(sendMessage);
            }
        } catch (IllegalConstructionException e) {
            Message error = new Message(caller);
            error.buildInvalidBuildingMessage(new GenericErrorMessage(e.getError()));
            listToSend.add(error);
        }
        return listToSend;
    }
}

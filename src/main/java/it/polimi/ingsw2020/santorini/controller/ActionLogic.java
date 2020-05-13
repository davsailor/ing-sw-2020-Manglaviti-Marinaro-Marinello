package it.polimi.ingsw2020.santorini.controller;

import it.polimi.ingsw2020.santorini.exceptions.EndMatchException;
import it.polimi.ingsw2020.santorini.exceptions.IllegalConstructionException;
import it.polimi.ingsw2020.santorini.exceptions.IllegalMovementException;
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
     *
     * @return the attribute turnManager
     */
    public TurnLogic getTurnManager() {
        return turnManager;
    }

    /**
     * The method invokes the power of the player’s god
     * @param match is the match that the controller is controlling
     * @param message is the match which asks to invoke the god
     * @return an Array List of Message which contains messages to notify the use of the god
     */
    public ArrayList<Message> invocation(Match match, Message message) throws EndMatchException {
        ArrayList<Message> listToSend = new ArrayList<>();
        GodCard god = match.getCurrentPlayer().getDivinePower();
        god.invokeGod(match, message, turnManager);
        for(int i = 0; i < match.getNumberOfPlayers(); ++i){
            Message sendMessage = new Message(match.getPlayers()[i].getNickname());
            sendMessage.buildUpdateMessage(new UpdateMessage(match, turnManager.getPhase()));
            listToSend.add(sendMessage);
        }
        return listToSend;
    }

    /**
     * The method handles the movement of the builder
     * @param match is the reference of the match controlled by the controller
     * @param message contains the direction of the movement
     * @return the ArrayList created contains the message for the next phases of the turn
     */
    public ArrayList<Message> move(Match match, SelectedMoveMessage message) throws EndMatchException {
        ArrayList<Message> listToSend = new ArrayList<>();
        try {
            match.getCurrentPlayer().getPlayingBuilder().move(message.getDirection());
            for(int i = 0; i < match.getNumberOfPlayers(); ++i){
                Message sendMessage = new Message(match.getPlayers()[i].getNickname());
                sendMessage.buildUpdateMessage(new UpdateMessage(match, turnManager.getPhase()));
                listToSend.add(sendMessage);
            }
        } catch (IllegalMovementException e) {
            Message error = new Message(match.getCurrentPlayer().getNickname());
            error.buildInvalidMoveMessage(new GenericErrorMessage(e.getError()));
            listToSend.add(error);
        } catch (EndMatchException e){
            match.currentWins();
        }
        return listToSend;
    }

    /**
     * The method handles the construction of the blocks
     * @param match is the reference of the match controlled by the controller
     * @param message contains the direction of construction
     * @return the ArrayList created contains the message for the next phases of the turn
     */
    public ArrayList<Message> build(Match match, SelectedBuildingMessage message) {
        ArrayList<Message> listToSend = new ArrayList<>();
        try {
            match.getCurrentPlayer().getPlayingBuilder().build(message.getDirection());
            for(int i = 0; i < match.getNumberOfPlayers(); ++i){
                Message sendMessage = new Message(match.getPlayers()[i].getNickname());
                sendMessage.buildUpdateMessage(new UpdateMessage(match, turnManager.getPhase()));
                listToSend.add(sendMessage);
            }
        } catch (IllegalConstructionException e) {
            Message error = new Message(match.getCurrentPlayer().getNickname());
            error.buildInvalidBuildingMessage(new GenericErrorMessage(e.getError()));
            listToSend.add(error);
        }
        return listToSend;
    }
}
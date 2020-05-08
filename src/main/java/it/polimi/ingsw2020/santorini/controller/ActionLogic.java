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

    public TurnLogic getTurnManager() {
        return turnManager;
    }

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

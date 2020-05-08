package it.polimi.ingsw2020.santorini.controller;

import it.polimi.ingsw2020.santorini.exceptions.EndMatchException;
import it.polimi.ingsw2020.santorini.model.GodCard;
import it.polimi.ingsw2020.santorini.model.Match;
import it.polimi.ingsw2020.santorini.utils.ActionType;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;
import it.polimi.ingsw2020.santorini.utils.messages.actions.ActivateGodMessage;
import it.polimi.ingsw2020.santorini.utils.messages.actions.AskBuildSelectionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.actions.AskMoveSelectionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.TurnPlayerMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.UpdateMessage;
import it.polimi.ingsw2020.santorini.utils.messages.actions.ActivationRequestInfoMessage;

import java.util.ArrayList;
import java.util.EnumSet;

import static it.polimi.ingsw2020.santorini.utils.ActionType.*;

public class TurnLogic {
    /**
     * quello che gestisce le fasi del turno e le transizioni di esso
     * uno dei suoi attributi sarà ActionLogic, da cui si andrà a gestire le azioni
     *
     * 1) controllo sull'attivabilità del potere
     * 2) seleziono il builder -> risposta è show possible moves
     * 3) seleziono la cella -> risposta è move
     * 3b) controllo sull'attivabilità del potere
     * 4) show possible builds
     * 5) seleziono la cella -> risposta è build
     * 5b) controllo sull'attivabilità del potere
     * 6) end turn
     */
    private PhaseType phase;
    private EnumSet<ActionType> remainingActions;
    private ActionLogic actionManager;


    public TurnLogic() {
        phase = PhaseType.START_TURN;
        remainingActions = EnumSet.allOf(ActionType.class);
        actionManager = new ActionLogic(this);
        this.reset();
    }

    /**
     * The method resets the remainingActions, refilling it
     */
    public void reset(){
        phase = PhaseType.START_TURN;
        remainingActions.clear();
        remainingActions = EnumSet.complementOf(remainingActions);
    }

    /**
     * Getter of the attribute remainingActions
     * @return the attribute remainingActions
     */
    public EnumSet<ActionType> getRemainingActions() {
        return remainingActions;
    }

    /**
     * Getter of the attribute phase
     * @return the attribute phase
     */
    public PhaseType getPhase() {
        return phase;
    }

    /**
     * The method manages the various Phases of the match calling the respective Manager for each PhaseType
     * @param match is the reference to the match controlled by the controller
     * @param caller is the username of the players which is playing this turn
     */
    public void handlePhases(Match match, String caller) throws EndMatchException {
        switch (phase){
            case START_TURN:
                startTurnManager(match);
                break;
            case STANDBY_PHASE_1:
                if(match.getCurrentPlayer().canMove()) standByPhaseManager(match, caller, PhaseType.STANDBY_PHASE_1);
                else match.setEliminatedPlayer(match.getCurrentPlayerIndex());
                break;
            case MOVE_PHASE:
                moveManager(match, caller);
                break;
            case STANDBY_PHASE_2:
                if(match.getCurrentPlayer().canBuild()) standByPhaseManager(match, caller, PhaseType.STANDBY_PHASE_2);
                else match.setEliminatedPlayer(match.getCurrentPlayerIndex());
                break;
            case BUILD_PHASE:
                buildManager(match, caller);
                break;
            case STANDBY_PHASE_3:
                standByPhaseManager(match, caller, PhaseType.STANDBY_PHASE_3);
                break;
            case END_TURN:
                endTurnManager(match);
                break;
            default:
                break;
        }
    }

    /**
     * THe method changes the currentPhase to the next one
     */
    public void nextPhase() {
        switch (this.phase){
            case START_TURN:
                this.phase = PhaseType.STANDBY_PHASE_1;
                break;
            case STANDBY_PHASE_1:
                this.phase = PhaseType.MOVE_PHASE;
                break;
            case MOVE_PHASE:
                this.phase = PhaseType.STANDBY_PHASE_2;
                break;
            case STANDBY_PHASE_2:
                this.phase = PhaseType.BUILD_PHASE;
                break;
            case BUILD_PHASE:
                this.phase = PhaseType.STANDBY_PHASE_3;
                break;
            case STANDBY_PHASE_3:
                this.phase = PhaseType.END_TURN;
                break;
            case END_TURN:
                this.phase = PhaseType.START_TURN;
                break;
        }
    }

    /**
     * The method filters requests from actions that will be managed by ActionLogic
     * @param action is the type of action that the method will handle
     * @param match is the reference to the match controlled by the controller
     * @param caller is the username of the players which is playing this turn
     * @param message is the message that will be deserialized by the method to extrapolate information needed to handle the actions
     */
    // filtro tra le richieste e le azioni vere e proprie che eseguirà action logic
    public void requestManager(ActionType action, Match match, String caller, Message message) throws EndMatchException{
        switch(action) {
            case ACTIVATE_GOD:
                ActivateGodMessage activateGod = message.deserializeActivateGodMessage(message.getSerializedPayload());
                remainingActions.remove(ACTIVATE_GOD);
                if(activateGod.isWantToActivate()) handlePhases(match, caller);
                else{
                    remainingActions.remove(SELECT_PARAMETERS);
                    remainingActions.remove(USE_POWER);
                    nextPhase();
                }
            case SELECT_PARAMETERS:
                requestManager(ActionType.USE_POWER, match, caller, message);
                remainingActions.remove(SELECT_PARAMETERS);
                break;
            case USE_POWER:
                ArrayList<Message> afterActivation = actionManager.invocation(match, caller, message);
                remainingActions.remove(USE_POWER);
                match.notifyView(afterActivation);
                break;
            case SELECT_CELL_MOVE:
                requestManager(ActionType.MOVE, match, caller, message);
                break;
            case MOVE:
                ArrayList<Message> afterMove = actionManager.move(match, caller, message.deserializeSelectedMoveMessage());
                remainingActions.remove(SELECT_CELL_MOVE);
                remainingActions.remove(MOVE);
                nextPhase();
                match.notifyView(afterMove);
                break;
            case SELECT_CELL_BUILD:
                requestManager(ActionType.BUILD, match, caller, message);
                break;
            case BUILD:
                ArrayList<Message> afterBuild = actionManager.build(match, caller, message.deserializeSelectedBuildingMessage());
                remainingActions.remove(SELECT_CELL_BUILD);
                remainingActions.remove(BUILD);
                nextPhase();
                match.notifyView(afterBuild);
                break;
            default:
                break;
        }
    }

    /**
     * The method handles the start of the turn
     * @param match is the reference to the match controlled by the controller
     */
    private void startTurnManager(Match match) {
        ArrayList<Message> listOfUpdateMessages = new ArrayList<>();
        match.getCurrentPlayer().setPlayingBuilder(null);
        for (int i = 0; i < match.getNumberOfPlayers(); ++i) {
            listOfUpdateMessages.add(new Message(match.getPlayers()[i].getNickname()));
            listOfUpdateMessages.get(i).buildUpdateMessage(new UpdateMessage(match, this.phase));
        }
        nextPhase();
        match.notifyView(listOfUpdateMessages);
    }

    /**
     * the method manages the operations that the controller has do in this phase. Especially it checks if the god’s power
     * can be activated by the player if it is not mandatory, in the other case it will be activated automatically if his
     * activation requirements are fulfilled
     * @param match is the reference to the match controlled by the controller
     * @param caller is the username of the players which is playing this turn
     * @param phase is the Phase that would be handled.
     */
    private void standByPhaseManager(Match match, String caller, PhaseType phase) throws EndMatchException{
        // controllo se il potere divino è attivabile
        GodCard god = match.getPlayerByName(caller).getDivinePower();
        if(god.getTiming() == phase){ // && (god.canActivate() || !remainingActions.contains(ACTIVATE_GOD))
            if (remainingActions.contains(ACTIVATE_GOD)) {
                if (god.isMandatory()) {
                    remainingActions.remove(ACTIVATE_GOD);
                    remainingActions.remove(SELECT_PARAMETERS);
                    remainingActions.remove(USE_POWER);
                    requestManager(USE_POWER, match, caller, null);
                    nextPhase();
                } else {
                    ArrayList<Message> listOfMessages = new ArrayList<>();
                    Message message = new Message(caller);
                    message.buildWouldActivateGodMessage(new ActivationRequestInfoMessage(caller, god.getName()));
                    listOfMessages.add(message);
                    match.notifyView(listOfMessages);
                }
            } else if(remainingActions.contains(SELECT_PARAMETERS)) {
                if(god.isNeedParameters()) {
                    ArrayList<Message> listOfMessages = new ArrayList<>();
                    Message message = new Message(caller);
                    message.buildSelectParametersMessage(new ActivationRequestInfoMessage(caller, god.getName()));
                    listOfMessages.add(message);
                    match.notifyView(listOfMessages);
                } else {
                    remainingActions.remove(SELECT_PARAMETERS);
                    requestManager(USE_POWER, match, caller, null);
                }
            } else if(remainingActions.contains(USE_POWER)){
            } else {
                if(god.isWillEnded()) { // controllo fatto per le divinità che possono attivare il potere più volte, vedi POSEIDONE
                    nextPhase();
                    handlePhases(match, caller);
                } else {
                    remainingActions.add(ACTIVATE_GOD);
                    remainingActions.add(SELECT_PARAMETERS);
                    remainingActions.add(USE_POWER);
                }
            }
        } else {
            nextPhase();
            handlePhases(match, caller);
        }
    }

    /**
     * the method handles the entire move phase from choosing the builder to the choice of the destination of the move
     * and the effective modification of the board
     * @param match is the reference to the match controlled by the controller
     * @param caller is the username of the players which is playing this turn
     */
    private void moveManager(Match match, String caller) throws EndMatchException{
        System.out.println("move manager");
            if(remainingActions.contains(SELECT_BUILDER)){
                if(match.getCurrentPlayer().getPlayingBuilder() == null){
                    ArrayList<Message> listOfMessages = new ArrayList<>();
                    Message requestBuilder = new Message(caller);
                    requestBuilder.buildSelectBuilderMessage(new TurnPlayerMessage(match.getCurrentPlayer(), match.getBoard().getBoard()));
                    listOfMessages.add(requestBuilder);
                    match.notifyView(listOfMessages);
                } else {
                    remainingActions.remove(SELECT_BUILDER);
                    handlePhases(match, caller);
                }
            } else if(remainingActions.contains(SELECT_CELL_MOVE)){
                match.getPlayerByName(caller).getPlayingBuilder().setPossibleMoves();
                int[][] possibleMoves = match.getPlayerByName(caller).getPlayingBuilder().getPossibleMoves();
                ArrayList<Message> listOfMessages = new ArrayList<>();
                Message requestMove = new Message(caller);
                requestMove.buildAskMoveSelectionMessage(new AskMoveSelectionMessage(possibleMoves, match.getCurrentPlayer().getRiseActions(), match.getCurrentPlayer().getMoveActions()));
                listOfMessages.add(requestMove);
                match.notifyView(listOfMessages);
            } else if(remainingActions.contains(MOVE)){
                // richiediamo al client le informazioni necessarie
            } else {
                nextPhase();
        }
    }

    /**
     * The method handles the entire build phase from choosing the cell where to build to the effective modification of the board.
     * @param match is the reference to the match controlled by the controller
     * @param caller is the username of the players which is playing this turn
     */
    private void buildManager(Match match, String caller) {
        System.out.println("build manager");
        if(remainingActions.contains(SELECT_CELL_BUILD)){
            match.getPlayerByName(caller).getPlayingBuilder().setPossibleBuildings();
            int[][] possibleBuilding = match.getPlayerByName(caller).getPlayingBuilder().getPossibleBuildings();
            ArrayList<Message> listOfMessages = new ArrayList<>();
            Message requestBuild = new Message(caller);
            requestBuild.buildAskBuildSelectionMessage(new AskBuildSelectionMessage(possibleBuilding));
            listOfMessages.add(requestBuild);
            match.notifyView(listOfMessages);
        } else if(remainingActions.contains(BUILD)){
            // richiediamo al client le informazioni necessarie
        } else {
            nextPhase();
        }
    }

    /**
     * The method finish the current TUrn starting a new one
     */
    private void endTurnManager(Match match) throws EndMatchException {
        match.setNextPlayer();
        reset();
        handlePhases(match, match.getCurrentPlayer().getNickname());
    }
}

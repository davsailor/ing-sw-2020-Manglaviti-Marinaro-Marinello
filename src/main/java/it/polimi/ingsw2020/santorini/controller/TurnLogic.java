package it.polimi.ingsw2020.santorini.controller;

import it.polimi.ingsw2020.santorini.model.GodCard;
import it.polimi.ingsw2020.santorini.model.Match;
import it.polimi.ingsw2020.santorini.utils.ActionType;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;
import it.polimi.ingsw2020.santorini.utils.messages.actions.ActivateGodMessage;
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

    public void reset(){
        phase = PhaseType.START_TURN;
        remainingActions.clear();
        remainingActions = EnumSet.complementOf(remainingActions);
    }

    public EnumSet<ActionType> getRemainingActions() {
        return remainingActions;
    }

    public PhaseType getPhase() {
        return phase;
    }

    public void handlePhases(Match match, String caller) {
        switch (phase){
            case START_TURN:
                startTurnManager(match);
                break;
            case STANDBY_PHASE_1:
                standByPhaseManager(match, caller, PhaseType.STANDBY_PHASE_1);
                break;
            case MOVE_PHASE:
                moveManager(match, caller);
                break;
            case STANDBY_PHASE_2:
                standByPhaseManager(match, caller, PhaseType.STANDBY_PHASE_2);
                break;
            case BUILD_PHASE:
                buildManager(match, caller);
                break;
            case STANDBY_PHASE_3:
                standByPhaseManager(match, caller, PhaseType.STANDBY_PHASE_3);
                break;
            case END_TURN:
                endTurnManager();
                break;
            default:
                break;
        }
    }

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

    // filtro tra le richieste e le azioni vere e proprie che eseguirà action logic
    public void requestManager(ActionType action, Match match, String caller, Message message) {
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
                // controllo sulla validità della richiesta
                // se è valida facciamo questo qui di seguito
                remainingActions.remove(SELECT_PARAMETERS);
                requestManager(ActionType.USE_POWER, match, caller, message);
                // se non è valida facciamo quello qui sotto
                // inviamo un messaggio di ERROR - INVALID_PARAMETERS
                break;
            case USE_POWER:
                remainingActions.remove(USE_POWER);
                match.notifyView(actionManager.invocation(match, caller, message));
            default:
                break;
        }
    }

    private void startTurnManager(Match match) {
        ArrayList<Message> listOfUpdateMessages = new ArrayList<>();
        match.getCurrentPlayer().setPlayingBuilder(null);
        for (int i = 0; i < match.getNumberOfPlayers(); ++i) {
            listOfUpdateMessages.add(new Message(match.getPlayers()[i].getNickname()));
            listOfUpdateMessages.get(i).buildUpdateMessage(new UpdateMessage(match, this.phase));
            match.notifyView(listOfUpdateMessages);
        }
        nextPhase();
    }

    private void standByPhaseManager(Match match, String caller, PhaseType phase) {
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
                ArrayList<Message> listOfMessages = new ArrayList<>();
                Message message = new Message(caller);
                message.buildSelectParametersMessage(new ActivationRequestInfoMessage(caller, god.getName()));
                listOfMessages.add(message);
                match.notifyView(listOfMessages);
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

    private void moveManager(Match match, String caller) {
        System.out.println("move manager");
            if(remainingActions.contains(SELECT_BUILDER)){
                // richiediamo al client le informazioni necessarie
            } else if(remainingActions.contains(SELECT_CELL_MOVE)){
                // richiediamo al client le informazioni necessarie
            } else if(remainingActions.contains(MOVE)){
                // richiediamo al client le informazioni necessarie
            } else {
                nextPhase();
        }
    }

    private void buildManager(Match match, String caller) {
        System.out.println("build manager");
        if(remainingActions.contains(SELECT_CELL_BUILD)){
            // richiediamo al client le informazioni necessarie
        } else if(remainingActions.contains(BUILD)){
            // richiediamo al client le informazioni necessarie
        } else {
            nextPhase();
        }
    }

    private void endTurnManager() {
        // selezionare il giocatore successivo
        reset();
    }
}

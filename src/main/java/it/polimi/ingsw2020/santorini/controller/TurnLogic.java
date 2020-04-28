package it.polimi.ingsw2020.santorini.controller;

import it.polimi.ingsw2020.santorini.exceptions.IllegalPhaseException;
import it.polimi.ingsw2020.santorini.utils.ActionType;
import it.polimi.ingsw2020.santorini.utils.PhaseType;

import java.util.EnumSet;

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

    public TurnLogic() {
        phase = PhaseType.START_TURN;
        remainingActions = EnumSet.allOf(ActionType.class);
        this.reset();
    }

    public void reset(){
        phase = PhaseType.START_TURN;
        remainingActions.clear();
        remainingActions = EnumSet.complementOf(remainingActions);
    }

    public void handlePhases(PhaseType phase) throws IllegalPhaseException {
        switch (phase){
            case START_TURN:
                startTurnManager();
                break;
            case STANDBY_PHASE_1:
                standByPhaseOneManager();
                break;
            case MOVE_PHASE:
                moveManager();
                break;
            case STANDBY_PHASE_2:
                standByPhaseTwoManager();
                break;
            case BUILD_PHASE:
                buildManager();
                break;
            case STANDBY_PHASE_3:
                standByPhaseThreeManager();
                break;
            case END_TURN:
                endTurnManager();
                break;
            default:
                throw new IllegalPhaseException();
        }
    }

    private void startTurnManager() {
        phase = PhaseType.STANDBY_PHASE_1;
    }

    private void standByPhaseOneManager() {
        phase = PhaseType.MOVE_PHASE;
    }

    private void moveManager() {
        phase = PhaseType.STANDBY_PHASE_2;
    }

    private void standByPhaseTwoManager() {
        phase = PhaseType.BUILD_PHASE;
    }

    private void buildManager() {
        phase = PhaseType.STANDBY_PHASE_3;
    }

    private void standByPhaseThreeManager() {
        phase = PhaseType.END_TURN;
    }

    private void endTurnManager() {
        // selezionare il giocatore successivo
        reset();
    }
}

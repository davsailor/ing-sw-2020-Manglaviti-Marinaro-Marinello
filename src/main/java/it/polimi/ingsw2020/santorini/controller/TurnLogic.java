package it.polimi.ingsw2020.santorini.controller;

import it.polimi.ingsw2020.santorini.exceptions.EndMatchException;
import it.polimi.ingsw2020.santorini.model.Board;
import it.polimi.ingsw2020.santorini.model.GodCard;
import it.polimi.ingsw2020.santorini.model.Match;
import it.polimi.ingsw2020.santorini.model.gods.Chronus;
import it.polimi.ingsw2020.santorini.utils.AccessType;
import it.polimi.ingsw2020.santorini.utils.ActionType;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;
import it.polimi.ingsw2020.santorini.utils.messages.actions.*;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumSet;

public class TurnLogic {
    /*
     * quello che gestisce le fasi del turno e le transizioni di esso
     * uno dei suoi attributi sarà ActionLogic, da cui si andrà a gestire le azioni
     * <p>
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
    private Method chronusEffect;
    private Chronus chronus;

    /*
     * constructor of the class
     */
    public TurnLogic() {
        remainingActions = EnumSet.allOf(ActionType.class);
        actionManager = new ActionLogic(this);
        chronusEffect = null;
        this.reset();
        phase = null;
    }

    /*
     * getters and setters
     */
    public void setStartTurn(){
        phase = PhaseType.START_TURN;
    }

    public void setChronus(Chronus chronus) {
        this.chronus = chronus;
    }

    public void setChronusEffect(Method persephoneEffect) {
        this.chronusEffect = persephoneEffect;
    }

    public EnumSet<ActionType> getRemainingActions() {
        return remainingActions;
    }

    public PhaseType getPhase() {
        return phase;
    }

    /**
     * method that sets the effect of chronus if it is activated.
     * necessary since chronus is an ALWAYS_ACTIVE like effect, unique in its genre
     * @param match the match where Chronus is activated
     * @throws EndMatchException when the condition of Instant Win of Chronus is met
     */
    private void checkChronusEffect(Match match) throws EndMatchException{
        if(chronusEffect != null) {
            try {
                chronusEffect.invoke(chronus,match, null, this);
            } catch (InvocationTargetException e) {
                throw new EndMatchException(match);
            } catch (IllegalAccessException ignored){}
        }
    }

    /**
     * The method resets the remainingActions, refilling it
     */
    public void reset() {
        phase = PhaseType.START_TURN;
        remainingActions.clear();
        remainingActions = EnumSet.complementOf(remainingActions);
    }

    /**
     * The method changes the currentPhase to the next one
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
     * The method manages the various Phases of the match calling the respective Manager for each PhaseType
     * @param match is the reference to the match controlled by the controller
     */
    public void handlePhases(Match match) throws EndMatchException {
        ArrayList<Method> opponentEffects;
        switch (phase){
            case START_TURN:
                startTurnManager(match);
                break;
            case STANDBY_PHASE_1:
                if(match.getCurrentPlayer().canMove()) standByPhaseManager(match, PhaseType.STANDBY_PHASE_1);
                else match.setEliminatedPlayer(match.getCurrentPlayerIndex());
                checkChronusEffect(match);
                break;
            case MOVE_PHASE:
                if(match.getCurrentPlayer().getPlayingBuilder() != null)
                    if(match.getCurrentPlayer().getPlayingBuilder().canMove()) moveManager(match);
                    else match.setEliminatedPlayer(match.getCurrentPlayerIndex());
                else moveManager(match);
                break;
            case STANDBY_PHASE_2:
                if(match.getCurrentPlayer().getPlayingBuilder().canBuild()) standByPhaseManager(match, PhaseType.STANDBY_PHASE_2);
                else match.setEliminatedPlayer(match.getCurrentPlayerIndex());
                break;
            case BUILD_PHASE:
                buildManager(match);
                checkChronusEffect(match);
                break;
            case STANDBY_PHASE_3:
                standByPhaseManager(match, PhaseType.STANDBY_PHASE_3);
                checkChronusEffect(match);
                break;
            case END_TURN:
                endTurnManager(match);
                break;
            default:
                break;
        }
    }

    /**
     * method that satisfies specific requested actions, sending messages or calling action of action logic
     * @param action the requested action
     * @param match the associated match
     * @param message the message that gives some more information
     * @throws EndMatchException when s player wins the match
     */
    public void requestManager(ActionType action, Match match, Message message) throws EndMatchException{
        System.out.println("REQUEST MANAGER: " + action);
        switch(action) {
            case ACTIVATE_GOD:
                ActivateGodMessage activateGod = message.deserializeActivateGodMessage(message.getSerializedPayload());
                remainingActions.remove(ActionType.ACTIVATE_GOD);
                if(activateGod.isWantToActivate()) handlePhases(match);
                else{
                    remainingActions.remove(ActionType.SELECT_PARAMETERS);
                    remainingActions.remove(ActionType.USE_POWER);
                    nextPhase();
                    handlePhases(match);
                }
                break;
            case USE_POWER:
                ArrayList<Message> afterActivation = actionManager.invocation(match, message);
                remainingActions.remove(ActionType.SELECT_PARAMETERS);
                remainingActions.remove(ActionType.USE_POWER);
                nextPhase(); // da rimuovere quando verranno implementati i poteri delle divinità -> ci penserà standbyphase manager a controllare se il potere non ha ulteriori effetti e quindi passare alla fase successiva
                match.notifyView(afterActivation);
                break;
            case MOVE:
                ArrayList<Message> afterMove = actionManager.move(match, message.deserializeSelectedMoveMessage());
                remainingActions.remove(ActionType.SELECT_CELL_MOVE);
                remainingActions.remove(ActionType.MOVE);
                nextPhase();
                match.notifyView(afterMove);
                break;
            case BUILD:
                ArrayList<Message> afterBuild = actionManager.build(match, message.deserializeSelectedBuildingMessage());
                remainingActions.remove(ActionType.SELECT_CELL_BUILD);
                remainingActions.remove(ActionType.BUILD);
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
        System.out.println("START TURN MANAGER");
        ArrayList<Message> listOfUpdateMessages = new ArrayList<>();
        match.getCurrentPlayer().setPlayingBuilder(null);
        for (int i = 0; i < match.getPlayers().length; ++i) {
            listOfUpdateMessages.add(new Message(match.getPlayers()[i].getNickname()));
            listOfUpdateMessages.get(i).buildUpdateMessage(new UpdateMessage(match, this.phase));
        }
        match.notifyView(listOfUpdateMessages);
        nextPhase();
    }

    /**
     * the method manages the operations that the controller has do in this phase. Especially it checks if the god’s power
     * can be activated by the player if it is not mandatory, in the other case it will be activated automatically if his
     * activation requirements are fulfilled
     * @param match is the reference to the match controlled by the controller
     * @param phase is the Phase that would be handled.
     */
    private void standByPhaseManager(Match match, PhaseType phase) throws EndMatchException{
        // controllo se il potere divino è attivabile
        System.out.printf("STANDBY PHASE MANAGER: ");
        GodCard god = match.getCurrentPlayer().getDivinePower();
        if(god.getTiming() == phase && god.canActivate(match)){ // || !remainingActions.contains(ACTIVATE_GOD)
            if (remainingActions.contains(ActionType.ACTIVATE_GOD)) {
                System.out.printf("ACTIVATE GOD\n");
                if (god.isMandatory()) {
                    remainingActions.remove(ActionType.ACTIVATE_GOD);
                    remainingActions.remove(ActionType.SELECT_PARAMETERS);
                    remainingActions.remove(ActionType.USE_POWER);
                    requestManager(ActionType.USE_POWER, match, null);
                } else {
                    ArrayList<Message> listOfMessages = new ArrayList<>();
                    Message message = new Message(match.getCurrentPlayer().getNickname());
                    message.buildWouldActivateGodMessage(new MatchStateMessage(match.getCurrentPlayer(), match.getBoard().getBoard()));
                    listOfMessages.add(message);
                    match.notifyView(listOfMessages);
                }
            } else if(remainingActions.contains(ActionType.SELECT_PARAMETERS)) {
                System.out.printf("SELECT PARAMETERS\n");
                if(god.isNeedParameters()) {
                    ArrayList<Message> listOfMessages = new ArrayList<>();
                    Message message = new Message(match.getCurrentPlayer().getNickname());
                    message.buildSelectParametersMessage(new MatchStateMessage(match.getCurrentPlayer(), match.getBoard().getBoard()));
                    listOfMessages.add(message);
                    match.notifyView(listOfMessages);
                } else {
                    remainingActions.remove(ActionType.SELECT_PARAMETERS);
                    requestManager(ActionType.USE_POWER, match, null);
                }
            } else if(remainingActions.contains(ActionType.USE_POWER)){
            } else {
                nextPhase();
            }
        } else {
            nextPhase();
            handlePhases(match);
        }
    }

    /**
     * the method handles the entire move phase from choosing the builder to the choice of the destination of the move
     * and the effective modification of the board
     * @param match is the reference to the match controlled by the controller
     *
     */
    private void moveManager(Match match) throws EndMatchException{
        System.out.printf("MOVE MANAGER: ");
        if(remainingActions.contains(ActionType.SELECT_BUILDER)){
            if(match.getCurrentPlayer().getPlayingBuilder() == null){
                System.out.printf("SELECT BUILDER\n");
                ArrayList<Message> listOfMessages = new ArrayList<>();
                Message requestBuilder = new Message(match.getCurrentPlayer().getNickname());
                requestBuilder.buildSelectBuilderMessage(new MatchStateMessage(match.getCurrentPlayer(), match.getBoard().getBoard()));
                listOfMessages.add(requestBuilder);
                match.notifyView(listOfMessages);
            } else {
                remainingActions.remove(ActionType.SELECT_BUILDER);
                handlePhases(match);
            }
        } else if(remainingActions.contains(ActionType.SELECT_CELL_MOVE)){
            System.out.printf("SELECT CELL MOVE\n");
            int[][] possibleMoves = Board.neighboringStatusCell(match.getCurrentPlayer().getPlayingBuilder(), AccessType.FREE);
            ArrayList<Message> listOfMessages = new ArrayList<>();
            Message requestMove = new Message(match.getCurrentPlayer().getNickname());
            // se ci sono problemi di client che ricevono il messaggio, bisogna modificare askmove aggiungendo il current player name
            requestMove.buildAskMoveSelectionMessage(new AskMoveSelectionMessage(possibleMoves, match.getCurrentPlayer().getRiseActions(), match.getCurrentPlayer().getMoveActions()));
            listOfMessages.add(requestMove);
            match.notifyView(listOfMessages);
        } else if(remainingActions.contains(ActionType.MOVE)){
            // richiediamo al client le informazioni necessarie
        } else {
            nextPhase();
            handlePhases(match);
        }
    }

    /**
     * The method handles the entire build phase from choosing the cell where to build to the effective modification of the board.
     * @param match is the reference to the match controlled by the controller
     *
     */
    private void buildManager(Match match) {
        System.out.printf("BUILD MANAGER: ");
        if(remainingActions.contains(ActionType.SELECT_CELL_BUILD)){
            System.out.println("SELECT CELL BUILD");
            int[][] possibleBuilding = Board.neighboringLevelCell(match.getCurrentPlayer().getPlayingBuilder());
            ArrayList<Message> listOfMessages = new ArrayList<>();
            Message requestBuild = new Message(match.getCurrentPlayer().getNickname());
            requestBuild.buildAskBuildSelectionMessage(new AskBuildSelectionMessage(possibleBuilding));
            listOfMessages.add(requestBuild);
            match.notifyView(listOfMessages);
        } else if(remainingActions.contains(ActionType.BUILD)){
            // richiediamo al client le informazioni necessarie
        } else {
            nextPhase();
            try {
                handlePhases(match);
            } catch (EndMatchException ignored) {}
        }
    }
    
    /**
     * The method finish the current Turn starting a new one
     * @param match is the reference to the match controller by the controller
     */
    private void endTurnManager(Match match) {
        System.out.println("END TURN MANAGER");
        ArrayList<Message> listOfUpdateMessages = new ArrayList<>();
        for (int i = 0; i < match.getNumberOfPlayers(); ++i) {
            listOfUpdateMessages.add(new Message(match.getPlayers()[i].getNickname()));
            listOfUpdateMessages.get(i).buildUpdateMessage(new UpdateMessage(match, this.phase));
        }
        match.getCurrentPlayer().setMoveActions(true);
        match.getCurrentPlayer().setRiseActions(true);
        match.getCurrentPlayer().getPlayingBuilder().setRisedThisTurn(false);
        match.setNextPlayer();
        reset();
        match.notifyView(listOfUpdateMessages);
    }
}
package it.polimi.ingsw2020.santorini.controller;

import it.polimi.ingsw2020.santorini.exceptions.EndMatchException;
import it.polimi.ingsw2020.santorini.model.Builder;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.model.gods.Apollo;
import it.polimi.ingsw2020.santorini.model.gods.Athena;
import it.polimi.ingsw2020.santorini.model.gods.Pan;
import it.polimi.ingsw2020.santorini.model.gods.Prometheus;
import it.polimi.ingsw2020.santorini.network.server.Server;
import it.polimi.ingsw2020.santorini.network.server.VirtualView;
import it.polimi.ingsw2020.santorini.utils.*;
import it.polimi.ingsw2020.santorini.utils.messages.actions.ActivateGodMessage;
import it.polimi.ingsw2020.santorini.utils.messages.actions.SelectedBuildingMessage;
import it.polimi.ingsw2020.santorini.utils.messages.actions.SelectedMoveMessage;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.PrometheusParamMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class TurnLogicTest {

    private Server server;
    private GameLogic controller;
    private VirtualView view;
    private Player player1;
    private Player player2;
    private TurnLogic turnLogic;
    private PhaseType phase;

    @Before
    public void setUp() throws Exception {
        server = new Server();
        controller = new GameLogic(server);
        view = new VirtualView(controller);
        player1 = new Player("Dog", new Date(1,0,1900));
        player2 = new Player("Cat", new Date(1,0,2000));
        server.getWaitingPlayers().put(player1, 2);
        server.getWaitingPlayers().put(player2, 2);
        controller.initializeMatch(view, 2);
        int[] pos = new int[2];
        pos[0] = 3;
        pos[1] = 4;
        Builder builder1 = new Builder(player1, 'F', controller.getMatch().getBoard(), pos);
        player1.setBuilderF(builder1);
        pos[0] = 2;
        pos[1] = 2;
        Builder builder2 = new Builder(player1, 'M', controller.getMatch().getBoard(), pos);
        player1.setBuilderM(builder2);
        pos[0] = 1;
        pos[1] = 1;
        Builder builder3 = new Builder(player2, 'F', controller.getMatch().getBoard(), pos);
        player2.setBuilderF(builder3);
        pos[0] = 5;
        pos[1] = 5;
        Builder builder4 = new Builder(player2, 'M', controller.getMatch().getBoard(), pos);
        player2.setBuilderM(builder4);
        player1.setDivinePower(new Athena());
        player2.setDivinePower(new Pan());
        turnLogic = controller.getTurnManager();
        turnLogic.setStartTurn();
    }

    @After
    public void tearDown() throws Exception {
        server.close();
    }

    @Test
    public void testHandlePhases_startTurn() throws EndMatchException {
        turnLogic.handlePhases(controller.getMatch());
        phase = turnLogic.getPhase();
        assertEquals(PhaseType.STANDBY_PHASE_1, phase);
    }

    @Test
    public void testHandlePhases_SP1() throws EndMatchException {
        phase = PhaseType.STANDBY_PHASE_1;
        turnLogic.setPhase(phase);
        turnLogic.handlePhases(controller.getMatch());
        phase = turnLogic.getPhase();
        assertEquals(PhaseType.MOVE_PHASE, phase);
    }

    @Test
    public void testHandlePhases_movePhase() throws EndMatchException {
        phase = PhaseType.MOVE_PHASE;
        controller.getMatch().getCurrentPlayer().setPlayingBuilder(controller.getMatch().getCurrentPlayer().getBuilderM());
        turnLogic.getRemainingActions().remove(ActionType.SELECT_BUILDER);
        turnLogic.setPhase(phase);
        turnLogic.handlePhases(controller.getMatch());
        phase = turnLogic.getPhase();
        assertEquals(PhaseType.MOVE_PHASE, phase);
        turnLogic.getRemainingActions().remove(ActionType.SELECT_CELL_MOVE);
        turnLogic.getRemainingActions().remove(ActionType.MOVE);
        turnLogic.handlePhases(controller.getMatch());
        phase = turnLogic.getPhase();
        assertEquals(PhaseType.BUILD_PHASE, phase);
    }

    @Test
    public void testHandlePhases_SP2() throws EndMatchException {
        phase = PhaseType.STANDBY_PHASE_2;
        controller.getMatch().getCurrentPlayer().setPlayingBuilder(controller.getMatch().getCurrentPlayer().getBuilderM());
        turnLogic.setPhase(phase);
        turnLogic.handlePhases(controller.getMatch());
        phase = turnLogic.getPhase();
        assertEquals(PhaseType.BUILD_PHASE, phase);
    }

    @Test
    public void testHandlePhases_buildPhase() throws EndMatchException {
        phase = PhaseType.BUILD_PHASE;
        controller.getMatch().getCurrentPlayer().setPlayingBuilder(controller.getMatch().getCurrentPlayer().getBuilderM());
        turnLogic.setPhase(phase);
        turnLogic.handlePhases(controller.getMatch());
        phase = turnLogic.getPhase();
        assertEquals(PhaseType.BUILD_PHASE, phase);
    }

    @Test
    public void testHandlePhases_SP3() throws EndMatchException {
        phase = PhaseType.STANDBY_PHASE_3;
        controller.getMatch().setNextPlayer();
        controller.getMatch().getCurrentPlayer().setPlayingBuilder(controller.getMatch().getCurrentPlayer().getBuilderM());
        controller.getMatch().getCurrentPlayer().getPlayingBuilder().setRisedThisTurn(true);
        turnLogic.setPhase(phase);
        turnLogic.handlePhases(controller.getMatch());
        phase = turnLogic.getPhase();
        assertEquals(PhaseType.END_TURN, phase);
    }

    @Test
    public void testHandlePhases_EndPhase() throws EndMatchException {
        phase = PhaseType.END_TURN;
        controller.getMatch().getCurrentPlayer().setPlayingBuilder(controller.getMatch().getCurrentPlayer().getBuilderM());
        turnLogic.setPhase(phase);
        turnLogic.handlePhases(controller.getMatch());
        phase = turnLogic.getPhase();
        assertEquals(PhaseType.START_TURN, phase);
        assertEquals(player1, controller.getMatch().getCurrentPlayer());
        assertTrue(player2.getMoveActions());
        assertTrue(player2.getRiseActions());
        assertFalse(player2.getPlayingBuilder().isRisedThisTurn());
        assertEquals(1, controller.getMatch().getCurrentPlayerIndex());
    }

    @Test
    public void testRequestManager_ActivateGod_Yes() throws EndMatchException {
        player2.setDivinePower(new Prometheus());
        ActivateGodMessage activateGodMessage = new ActivateGodMessage(true);
        Message message = new Message(player2.getNickname());
        message.buildActivateGodMessage(activateGodMessage);
        phase = PhaseType.STANDBY_PHASE_1;
        turnLogic.setPhase(phase);
        turnLogic.requestManager(ActionType.ACTIVATE_GOD, controller.getMatch(), message);
        assertEquals(phase, turnLogic.getPhase());
        assertFalse(turnLogic.getRemainingActions().contains(ActionType.ACTIVATE_GOD));
        assertTrue(turnLogic.getRemainingActions().contains(ActionType.USE_POWER));
        assertTrue(turnLogic.getRemainingActions().contains(ActionType.SELECT_PARAMETERS));
    }

    @Test
    public void testRequestManager_ActivateGod_No() throws EndMatchException {
        player2.setDivinePower(new Prometheus());
        ActivateGodMessage activateGodMessage = new ActivateGodMessage(false);
        Message message = new Message(player2.getNickname());
        message.buildActivateGodMessage(activateGodMessage);
        phase = PhaseType.STANDBY_PHASE_1;
        turnLogic.setPhase(phase);
        turnLogic.requestManager(ActionType.ACTIVATE_GOD, controller.getMatch(), message);
        phase = PhaseType.MOVE_PHASE;
        assertEquals(phase, turnLogic.getPhase());
        assertFalse(turnLogic.getRemainingActions().contains(ActionType.ACTIVATE_GOD));
        assertFalse(turnLogic.getRemainingActions().contains(ActionType.USE_POWER));
        assertFalse(turnLogic.getRemainingActions().contains(ActionType.SELECT_PARAMETERS));
    }

    @Test
    public void testRequestManager_UsePower() throws EndMatchException {
        player2.setDivinePower(new Prometheus());
        phase = PhaseType.STANDBY_PHASE_1;
        turnLogic.setPhase(phase);
        Message message = new Message(player2.getNickname());
        PrometheusParamMessage prometheusParamMessage = new PrometheusParamMessage();
        prometheusParamMessage.setBuilderSex('F');
        prometheusParamMessage.setDirection(Direction.EAST);
        message.buildPrometheusParamMessage(prometheusParamMessage);
        turnLogic.requestManager(ActionType.USE_POWER, controller.getMatch(), message);
        phase = PhaseType.MOVE_PHASE;
        assertEquals(phase, turnLogic.getPhase());
        assertFalse(turnLogic.getRemainingActions().contains(ActionType.USE_POWER));
        assertFalse(turnLogic.getRemainingActions().contains(ActionType.SELECT_PARAMETERS));
        assertEquals(AccessType.OCCUPIED, controller.getMatch().getBoard().getBoard()[1][1].getStatus());
        assertEquals(LevelType.BASE, controller.getMatch().getBoard().getBoard()[1][2].getLevel());
        assertEquals(player2.getBuilderF(), player2.getPlayingBuilder());
    }

    @Test
    public void testRequestManager_Move() throws EndMatchException {
        phase = PhaseType.MOVE_PHASE;
        turnLogic.setPhase(phase);
        player2.setPlayingBuilder(player2.getBuilderF());
        Message message = new Message(player2.getNickname());
        SelectedMoveMessage selectedMoveMessage = new SelectedMoveMessage(Direction.EAST);
        message.buildSelectedMoveMessage(selectedMoveMessage);
        turnLogic.requestManager(ActionType.MOVE, controller.getMatch(), message);
        phase = PhaseType.STANDBY_PHASE_2;
        assertEquals(phase, turnLogic.getPhase());
        assertFalse(turnLogic.getRemainingActions().contains(ActionType.MOVE));
        assertFalse(turnLogic.getRemainingActions().contains(ActionType.SELECT_CELL_MOVE));
        assertEquals(AccessType.OCCUPIED, controller.getMatch().getBoard().getBoard()[1][2].getStatus());
        assertEquals(AccessType.FREE, controller.getMatch().getBoard().getBoard()[1][1].getStatus());
        assertEquals(player2.getBuilderF(), player2.getPlayingBuilder());
    }

    @Test
    public void testRequestManager_Build() throws EndMatchException {
        phase = PhaseType.BUILD_PHASE;
        turnLogic.setPhase(phase);
        player2.setPlayingBuilder(player2.getBuilderF());
        Message message = new Message(player2.getNickname());
        SelectedBuildingMessage selectedBuildingMessage = new SelectedBuildingMessage(Direction.EAST);
        message.buildSelectedBuildingMessage(selectedBuildingMessage);
        turnLogic.requestManager(ActionType.BUILD, controller.getMatch(), message);
        phase = PhaseType.STANDBY_PHASE_3;
        assertEquals(phase, turnLogic.getPhase());
        assertFalse(turnLogic.getRemainingActions().contains(ActionType.BUILD));
        assertFalse(turnLogic.getRemainingActions().contains(ActionType.SELECT_CELL_BUILD));
        assertEquals(AccessType.OCCUPIED, controller.getMatch().getBoard().getBoard()[1][1].getStatus());
        assertEquals(LevelType.BASE, controller.getMatch().getBoard().getBoard()[1][2].getLevel());
        assertEquals(player2.getBuilderF(), player2.getPlayingBuilder());
    }
}
package it.polimi.ingsw2020.santorini.model;

import it.polimi.ingsw2020.santorini.controller.GameLogic;
import it.polimi.ingsw2020.santorini.network.server.Server;
import it.polimi.ingsw2020.santorini.network.server.VirtualView;
import it.polimi.ingsw2020.santorini.utils.PhaseType;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Month;
import java.util.Date;

import static org.junit.Assert.*;

public class MatchTest {

    private Board board;
    private VirtualView view;
    private Server server;

    @Before
    public void setup(){
        server = new Server();
        board = new Board(new GodDeck());
        view = new VirtualView(new GameLogic(server));
    }

    @After
    public void tearDown(){
        server.close();
    }

    @Test
    public void testGetCurrentPlayer() {
        Match match = new Match(board, 2, view);
        Player player[] = new Player[2];
        Date date = new Date(1999, 2, 15);
        player[0] = new Player("davsailor", date);
        player[1] = new Player("blue thunder", date);
        match.initialize(player);
        assertEquals(player[0], match.getCurrentPlayer());
    }

    @Test
    public void testSetCurrentPlayerIndex() {
        Match match = new Match(board, 3, view);
        match.setCurrentPlayerIndex(2);
        assertEquals(2, match.getCurrentPlayerIndex());
    }

    @Test
    public void testGetPlayerByName_correctInput_correctOutput() {
        Match match = new Match(board, 3, view);
        Player player[] = new Player[3];
        Date date = new Date(1999, 2, 15);
        player[0] = new Player("davsailor", date);
        player[1] = new Player("lukeMari", date);
        player[2] = new Player("AndreaMangla", date);
        match.initialize(player);
        Player test = match.getPlayerByName("AndreaMangla");
        assertEquals(player[2], test);
    }

    @Test
    public void testGetPlayerByName_wrongInput_nullOutput() {
        Match match = new Match(board, 3, view);
        Player[] player = new Player[3];
        Date date = new Date(1999, 2, 15);
        player[0] = new Player("davsailor", date);
        player[1] = new Player("lukeMari", date);
        player[2] = new Player("AndreaMangla", date);
        match.initialize(player);
        Player test = match.getPlayerByName("AndreaMangla");
        assertNull(test);
    }

    @Test
    public void testGetNumberOfCompletedTowers() {
        Match match = new Match(board, 3, view);
        match.setNumberOfCompletedTowers(5);
        assertEquals(5, match.getNumberOfCompletedTowers());
    }

    @Test
    public void testAddNumberOfCompletedTowers() {
        Match match = new Match(board, 3, view);
        match.setNumberOfCompletedTowers(4);
        match.addNumberOfCompletedTowers();
        assertEquals(5, match.getNumberOfCompletedTowers());
    }

    @Test
    public void testSetTurnPhase() {
        Match match = new Match(board, 2, view);
        match.setTurnPhase(PhaseType.END_TURN);
        assertEquals(PhaseType.END_TURN, match.getTurnPhase());
    }

    @Test
    public void testGetTurnPhase() {
        Match match = new Match(board, 2, view);
        assertEquals(PhaseType.START_TURN, match.getTurnPhase());
    }

    @Test
    public void testNextTurnNumber() {
        Match match = new Match(board, 2, view);
        match.nextTurnNumber();
        assertEquals(1, match.getTurnNumber());
    }

    @Test
    public void testGetTurnNumber() {
        Match match = new Match(board, 2, view);
        assertEquals(0, match.getTurnNumber());
    }

    @Test
    public void testSetEliminatedPlayer() {
        Match match = new Match(board, 2, view);
        match.setEliminatedPlayer(0);
        assertEquals(0, match.getEliminatedPlayer());
    }

    @Test
    public void testGetEliminatedPlayer() {
        Match match = new Match(board, 2, view);
        assertEquals(-1, match.getEliminatedPlayer());
    }

    @Test
    public void testSetNumberOfCompletedTowers() {
        Match match = new Match(board, 2, view);
        match.setNumberOfCompletedTowers(5);
        assertEquals(5, match.getNumberOfCompletedTowers());
    }

    @Test
    public void testGetPlayers() {
        Match match = new Match(board, 3, view);
        boolean containsAll = true;
        Player[] player = new Player[3];
        Date date = new Date(1999, 2, 15);
        player[0] = new Player("davsailor", date);
        player[1] = new Player("lukeMari", date);
        player[2] = new Player("AndreaMangla", date);
        match.initialize(player);
        Player[] playerCpy = match.getPlayers();
        for(int i = 0; i < match.getNumberOfPlayers() && containsAll; ++i){
            if(playerCpy[i].getNickname().equals(match.getPlayers()[i].getNickname())) {}
            else containsAll = false;
        }
        assertTrue("Test Passato!", containsAll && playerCpy.length == match.getNumberOfPlayers());
    }

    @Test
    public void testInitialize_correctInput_correctInitialization() {
        Match match = new Match(board, 3, view);
        Player[] player = new Player[3];
        Date date = new Date(1999, 2, 15);
        player[0] = new Player("davsailor", date);
        player[1] = new Player("lukeMari", date);
        player[2] = new Player("AndreaMangla", date);
        match.initialize(player);
        for(int i = 0; i < match.getNumberOfPlayers(); ++i){
            assertNotNull(match.getPlayers()[i].getDivinePower());
            assertNotNull(match.getPlayers()[i].getColor());
        }
        assertFalse(match.hasChanged());
    }

    @Test
    public void testGetMatchID() {
        Match match = new Match(board, 3, view);
        assertEquals(0, match.getMatchID());
    }

    @Test
    public void testGetCurrentPlayerIndex() {
        Match match = new Match(board, 3, view);
        assertEquals(0, match.getCurrentPlayerIndex());
    }

    @Test
    public void testGetBoard() {
        Match match = new Match(board, 3, view);
        assertEquals(board, match.getBoard());
    }

    @Test
    public void testGetNumberOfPlayers() {
        Match match = new Match(board, 3, view);
        assertEquals(3, match.getNumberOfPlayers());
    }

    @Test
    public void testEquals() {
        Match match1 = new Match(board, 3, view);
        Match match2 = new Match(board, 3, view);
        assertFalse(match1.equals(match2));
        assertTrue(match1.equals(match1));
        assertFalse(match1.equals(new Server()));
    }

    @Test
    public void notifyView() {
    }

    @Test
    public void testSetNextPlayer_firstPlayer_secondPlayer() {
        Match match = new Match(board, 3, view);
        Player[] player = new Player[3];
        Date date = new Date(1999, 2, 15);
        player[0] = new Player("davsailor", date);
        player[1] = new Player("lukeMari", date);
        player[2] = new Player("AndreaMangla", date);
        match.initialize(player);
        match.setNextPlayer();
        assertEquals(1, match.getCurrentPlayerIndex());
    }

    @Test
    public void testSetNextPlayer_secondPlayer_thirdPlayer() {
        Match match = new Match(board, 3, view);
        Player[] player = new Player[3];
        Date date = new Date(1999, 2, 15);
        player[0] = new Player("davsailor", date);
        player[1] = new Player("lukeMari", date);
        player[2] = new Player("AndreaMangla", date);
        match.initialize(player);
        match.setNextPlayer();
        match.setNextPlayer();
        assertEquals(2, match.getCurrentPlayerIndex());
    }

    @Test
    public void testSetNextPlayer_lastPlayer_firstPlayer() {
        Match match = new Match(board, 3, view);
        Player[] player = new Player[3];
        Date date = new Date(1999, 2, 15);
        player[0] = new Player("davsailor", date);
        player[1] = new Player("lukeMari", date);
        player[2] = new Player("AndreaMangla", date);
        match.initialize(player);
        match.setNextPlayer();
        match.setNextPlayer();
        match.setNextPlayer();
        assertEquals(0, match.getCurrentPlayerIndex());
    }
}
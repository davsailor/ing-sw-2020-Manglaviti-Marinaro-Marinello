package it.polimi.ingsw2020.santorini.model;

import it.polimi.ingsw2020.santorini.controller.GameLogic;
import it.polimi.ingsw2020.santorini.network.server.Server;
import it.polimi.ingsw2020.santorini.network.server.VirtualView;
import it.polimi.ingsw2020.santorini.utils.PhaseType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Date;

public class MatchTest {

    private Board board;
    private VirtualView view;
    private Server server;
    private final String[] names = {"davsailor", "lukeMari", "AndreaMangla"};

    @Before
    public void setup(){
        server = new Server();
        board = new Board(new GodDeck(2));
        view = new VirtualView(new GameLogic(server));
    }

    @After
    public void tearDown(){
        server.close();
    }

    @Test
    public void testGetCurrentPlayer() {
        Match match = new Match(board, 2, view);
        fullInit(2, match);
        assertEquals(match.getPlayers()[0], match.getCurrentPlayer());
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
        fullInit(3, match);
        Player test = match.getPlayerByName("AndreaMangla");
        assertEquals(match.getPlayers()[2], test);
    }

    @Test
    public void testGetPlayerByName_wrongInput_nullOutput() {
        Match match = new Match(board, 2, view);
        fullInit(2, match);
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
    public void testSetNumberOfCompletedTowers() {
        Match match = new Match(board, 2, view);
        match.setNumberOfCompletedTowers(5);
        assertEquals(5, match.getNumberOfCompletedTowers());
    }

    @Test
    public void testGetPlayers() {
        Match match = new Match(board, 3, view);
        boolean containsAll = true;
        fullInit(3, match);
        Player[] playerCpy = match.getPlayers();
        for(int i = 0; i < match.getPlayers().length && containsAll; ++i){
            if(playerCpy[i].getNickname().equals(match.getPlayers()[i].getNickname())) {}
            else containsAll = false;
        }
        assertTrue("Test Passato!", containsAll && playerCpy.length == match.getNumberOfPlayers());
    }

    @Test
    public void testInitialize_correctInput_correctInitialization() {
        Match match = new Match(board, 3, view);
        fullInit(3, match);
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
        assertFalse(match1.equals(server));
    }

    @Test
    public void notifyView() {
    }

    @Test
    public void testSetNextPlayer_firstPlayer_secondPlayer() {
        Match match = new Match(board, 3, view);
        match.setNextPlayer();
        assertEquals(1, match.getCurrentPlayerIndex());
    }

    @Test
    public void testSetNextPlayer_secondPlayer_thirdPlayer() {
        Match match = new Match(board, 3, view);
        match.setNextPlayer();
        match.setNextPlayer();
        assertEquals(2, match.getCurrentPlayerIndex());
    }

    @Test
    public void testSetNextPlayer_lastPlayer_firstPlayer() {
        Match match = new Match(board, 3, view);
        match.setNextPlayer();
        match.setNextPlayer();
        match.setNextPlayer();
        assertEquals(0, match.getCurrentPlayerIndex());
    }

    private void fullInit(int param, Match match) {
        Player[] player = new Player[param];
        Date date = new Date(1999, 2, 15);
        for(int i = 0; i < param; ++i){
            player[i] = new Player(names[i], date);
        }
        try {
            match.initialize(player);
        } catch (NullPointerException e){
            // we do not inizialize ClientHandler correctly (we need a proper connection C-S)
            // so we expect a NullPointerException
        }
    }
}
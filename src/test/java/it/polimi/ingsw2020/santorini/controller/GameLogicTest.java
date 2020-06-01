package it.polimi.ingsw2020.santorini.controller;

import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.network.server.ClientNetworkHandler;
import it.polimi.ingsw2020.santorini.network.server.Server;
import it.polimi.ingsw2020.santorini.network.server.VirtualView;
import it.polimi.ingsw2020.santorini.utils.Color;
import it.polimi.ingsw2020.santorini.utils.PlayerStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class GameLogicTest {

    private Server server;
    private GameLogic controller;
    private VirtualView view;
    private Player player1;
    private Player player2;
    private Player player3;

    @Before
    public void setUp() throws Exception {
        server = new Server();
        controller = new GameLogic(server);
        view = new VirtualView(controller);
        player1 = new Player("Dog", new Date(1,0,1900));
        player2 = new Player("Cat", new Date(1,0,2000));
        player3 = new Player("Mouse", new Date(1,0,2010));
    }

    @After
    public void tearDown() throws Exception {
        server.close();
    }

    @Test
    public void testInitializeMatch_twoPlayers() {
        server.getWaitingPlayers().put(player1, 2);
        server.getWaitingPlayers().put(player2, 2);
        controller.initializeMatch(view, 2);
        assertEquals(0, controller.getMatch().getMatchID());
        assertEquals(player2, controller.getMatch().getCurrentPlayer());
        assertEquals(player1, controller.getMatch().getPlayers()[1]);
        assertEquals(Color.PLAYER_CYAN, controller.getMatch().getCurrentPlayer().getColor());
        assertEquals(Color.PLAYER_GREEN, controller.getMatch().getPlayers()[1].getColor());
        assertEquals(PlayerStatus.WAITING, controller.getMatch().getCurrentPlayer().getStatus());
        assertEquals(PlayerStatus.WAITING, controller.getMatch().getPlayers()[1].getStatus());
        assertNull(controller.getMatch().getCurrentPlayer().getDivinePower());
        assertNull(controller.getMatch().getPlayers()[1].getDivinePower());
        assertTrue(server.getWaitingPlayers().isEmpty());
        assertEquals(0, controller.getMatch().getCurrentPlayerIndex());
    }

    @Test
    public void testInitializeMatch_threePlayers() {
        server.getWaitingPlayers().put(player1, 3);
        server.getWaitingPlayers().put(player2, 3);
        server.getWaitingPlayers().put(player3, 3);
        controller.initializeMatch(view, 3);
        assertEquals(0, controller.getMatch().getMatchID());
        assertEquals(player3, controller.getMatch().getCurrentPlayer());
        assertEquals(player2, controller.getMatch().getPlayers()[1]);
        assertEquals(player1, controller.getMatch().getPlayers()[2]);
        assertEquals(Color.PLAYER_CYAN, controller.getMatch().getCurrentPlayer().getColor());
        assertEquals(Color.PLAYER_GREEN, controller.getMatch().getPlayers()[1].getColor());
        assertEquals(Color.PLAYER_PURPLE, controller.getMatch().getPlayers()[2].getColor());
        assertEquals(PlayerStatus.WAITING, controller.getMatch().getCurrentPlayer().getStatus());
        assertEquals(PlayerStatus.WAITING, controller.getMatch().getPlayers()[1].getStatus());
        assertEquals(PlayerStatus.WAITING, controller.getMatch().getPlayers()[2].getStatus());
        assertNull(controller.getMatch().getCurrentPlayer().getDivinePower());
        assertNull(controller.getMatch().getPlayers()[1].getDivinePower());
        assertNull(controller.getMatch().getPlayers()[2].getDivinePower());
        assertTrue(server.getWaitingPlayers().isEmpty());
        assertEquals(0, controller.getMatch().getCurrentPlayerIndex());
    }
}
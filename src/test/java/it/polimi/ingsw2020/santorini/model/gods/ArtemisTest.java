package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.controller.GameLogic;
import it.polimi.ingsw2020.santorini.controller.TurnLogic;
import it.polimi.ingsw2020.santorini.exceptions.EndMatchException;
import it.polimi.ingsw2020.santorini.exceptions.IllegalMovementException;
import it.polimi.ingsw2020.santorini.model.Builder;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.network.server.Server;
import it.polimi.ingsw2020.santorini.network.server.VirtualView;
import it.polimi.ingsw2020.santorini.utils.Direction;
import it.polimi.ingsw2020.santorini.utils.LevelType;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.AresParamMessage;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.ArtemisParamMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class ArtemisTest {

    private Server server;
    private GameLogic controller;
    private VirtualView view;
    private Player player1;
    private Player player2;
    private TurnLogic turnLogic;
    private PhaseType phase;
    private Message message;
    private ArtemisParamMessage godParamMessage;

    @Before
    public void setUp() throws Exception {
        server = new Server();
        controller = new GameLogic(server);
        view = new VirtualView(controller);
        player1 = new Player("Dog", new Date(1, 0, 2000));
        player2 = new Player("Cat", new Date(1, 0, 1900));
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
        pos[0] = 3;
        pos[1] = 5;
        Builder builder4 = new Builder(player2, 'M', controller.getMatch().getBoard(), pos);
        player2.setBuilderM(builder4);
        player1.setDivinePower(new Artemis());
        player2.setDivinePower(new Pan());
        turnLogic = controller.getTurnManager();
        turnLogic.setStartTurn();
        message = new Message("Dog");
        godParamMessage = new ArtemisParamMessage();
        godParamMessage.setDirection(Direction.NORTH_WEST);
        message.buildArtemisParamMessage(godParamMessage);
        player1.setPlayingBuilder(player1.getBuilderF());
    }

    @After
    public void tearDown() throws Exception {
        server.close();
    }

    @Test
    public void testInvokeGod(){
        try {
            player1.getBuilderF().move(Direction.NORTH_WEST);
        } catch (IllegalMovementException e) {
            e.printStackTrace();
        } catch (EndMatchException e) {
            e.printStackTrace();
        }
        assertEquals(2, player1.getBuilderF().getPosX());
        assertEquals(3, player1.getBuilderF().getPosY());
        try {
            player1.getDivinePower().invokeGod(controller.getMatch(), message, turnLogic);
        } catch (EndMatchException e) {}
        assertEquals(1, player1.getBuilderF().getPosX());
        assertEquals(2, player1.getBuilderF().getPosY());
        assertTrue(player1.getDivinePower().canActivate(controller.getMatch()));
    }

    @Test(expected = EndMatchException.class)
    public void testInvokeGod_correctArgument_throwsEndMatchException() throws EndMatchException {
        controller.getMatch().getBoard().getBoard()[1][4].setLevel(LevelType.BASE);
        controller.getMatch().getBoard().getBoard()[1][4].setLevel(LevelType.MID);
        controller.getMatch().getBoard().getBoard()[2][4].setLevel(LevelType.BASE);
        controller.getMatch().getBoard().getBoard()[1][3].setLevel(LevelType.BASE);
        controller.getMatch().getBoard().getBoard()[1][3].setLevel(LevelType.MID);
        controller.getMatch().getBoard().getBoard()[1][3].setLevel(LevelType.TOP);
        try {
            player1.getBuilderF().move(Direction.NORTH);
        } catch (IllegalMovementException e) {
            e.printStackTrace();
        } catch (EndMatchException e) {
            e.printStackTrace();
        }
        try {
            player1.getBuilderF().move(Direction.NORTH);
        } catch (IllegalMovementException e) {
            e.printStackTrace();
        } catch (EndMatchException e) {
            e.printStackTrace();
        }
        godParamMessage.setDirection(Direction.WEST);
        message.buildArtemisParamMessage(godParamMessage);

        player1.getDivinePower().invokeGod(controller.getMatch(), message, turnLogic);
    }
}
package it.polimi.ingsw2020.santorini.model.gods;

import it.polimi.ingsw2020.santorini.controller.GameLogic;
import it.polimi.ingsw2020.santorini.controller.TurnLogic;
import it.polimi.ingsw2020.santorini.exceptions.EndMatchException;
import it.polimi.ingsw2020.santorini.exceptions.IllegalMovementException;
import it.polimi.ingsw2020.santorini.model.Builder;
import it.polimi.ingsw2020.santorini.model.GodCard;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.network.server.Server;
import it.polimi.ingsw2020.santorini.network.server.VirtualView;
import it.polimi.ingsw2020.santorini.utils.Direction;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.ApolloParamMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class ApolloTest {
    private Server server;
    private GameLogic controller;
    private VirtualView view;
    private Player player1;
    private Player player2;
    private TurnLogic turnLogic;
    private PhaseType phase;
    private GodCard tester;
    private Message message;
    private ApolloParamMessage godParamMessage;

    @Before
    public void setUp() throws Exception {
        server = new Server();
        controller = new GameLogic(server);
        view = new VirtualView(controller);
        player1 = new Player("Dog", new Date(1,0,2000));
        player2 = new Player("Cat", new Date(1,0,1900));
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
        pos[0] = 2;
        pos[1] = 3;
        Builder builder3 = new Builder(player2, 'F', controller.getMatch().getBoard(), pos);
        player2.setBuilderF(builder3);
        pos[0] = 3;
        pos[1] = 5;
        Builder builder4 = new Builder(player2, 'M', controller.getMatch().getBoard(), pos);
        player2.setBuilderM(builder4);
        player1.setDivinePower(new Apollo());
        player2.setDivinePower(new Pan());
        turnLogic = controller.getTurnManager();
        turnLogic.setStartTurn();
        tester = new Apollo();
        message = new Message("Dog");
        godParamMessage = new ApolloParamMessage();
        godParamMessage.setYourBuilderGender(player1.getBuilderF().getGender());
        godParamMessage.setOpponentBuilderDirection(Direction.NORTH_WEST);
        message.buildApolloParamMessage(godParamMessage);
    }

    @After
    public void tearDown() throws Exception {
        server.close();
    }

    @Test
    public void testInvokeGod() throws IllegalMovementException, EndMatchException {
        assertTrue(player1.getDivinePower().canActivate(controller.getMatch()));
        //Test di north_west
        player1.getDivinePower().invokeGod(controller.getMatch(), message, turnLogic );
        assertEquals(2, player1.getBuilderF().getPosX());
        assertEquals(3, player1.getBuilderF().getPosY());
        assertEquals(3, player2.getBuilderF().getPosX());
        assertEquals(4, player2.getBuilderF().getPosY());
        //TEst di south_east
        godParamMessage.setOpponentBuilderDirection(Direction.SOUTH_EAST);
        message.buildApolloParamMessage(godParamMessage);
        player1.getDivinePower().invokeGod(controller.getMatch(), message, turnLogic );
        assertEquals(3, player1.getBuilderF().getPosX());
        assertEquals(4, player1.getBuilderF().getPosY());
        assertEquals(2, player2.getBuilderF().getPosX());
        assertEquals(3, player2.getBuilderF().getPosY());
        //Test di north
        player2.getBuilderF().move(Direction.EAST);
        godParamMessage.setOpponentBuilderDirection(Direction.NORTH);
        message.buildApolloParamMessage(godParamMessage);
        player1.getDivinePower().invokeGod(controller.getMatch(), message, turnLogic );
        assertEquals(2, player1.getBuilderF().getPosX());
        assertEquals(4, player1.getBuilderF().getPosY());
        assertEquals(3, player2.getBuilderF().getPosX());
        assertEquals(4, player2.getBuilderF().getPosY());
        //Test di south
        godParamMessage.setOpponentBuilderDirection(Direction.SOUTH);
        message.buildApolloParamMessage(godParamMessage);
        player1.getDivinePower().invokeGod(controller.getMatch(), message, turnLogic );
        assertEquals(3, player1.getBuilderF().getPosX());
        assertEquals(4, player1.getBuilderF().getPosY());
        assertEquals(2, player2.getBuilderF().getPosX());
        assertEquals(4, player2.getBuilderF().getPosY());
        //Test di North_east
        player2.getBuilderF().move(Direction.EAST);
        godParamMessage.setOpponentBuilderDirection(Direction.NORTH_EAST);
        message.buildApolloParamMessage(godParamMessage);
        player1.getDivinePower().invokeGod(controller.getMatch(), message, turnLogic );
        assertEquals(2, player1.getBuilderF().getPosX());
        assertEquals(5, player1.getBuilderF().getPosY());
        assertEquals(3, player2.getBuilderF().getPosX());
        assertEquals(4, player2.getBuilderF().getPosY());
        //test di SOUTH_ WEST
        godParamMessage.setOpponentBuilderDirection(Direction.SOUTH_WEST);
        message.buildApolloParamMessage(godParamMessage);
        player1.getDivinePower().invokeGod(controller.getMatch(), message, turnLogic );
        assertEquals(3, player1.getBuilderF().getPosX());
        assertEquals(4, player1.getBuilderF().getPosY());
        assertEquals(2, player2.getBuilderF().getPosX());
        assertEquals(5, player2.getBuilderF().getPosY());
        //test di east
        player2.getBuilderF().move(Direction.SOUTH);
        godParamMessage.setOpponentBuilderDirection(Direction.EAST);
        message.buildApolloParamMessage(godParamMessage);
        player1.getDivinePower().invokeGod(controller.getMatch(), message, turnLogic );
        assertEquals(3, player1.getBuilderF().getPosX());
        assertEquals(5, player1.getBuilderF().getPosY());
        assertEquals(3, player2.getBuilderF().getPosX());
        assertEquals(4, player2.getBuilderF().getPosY());
        //test di ovest
        godParamMessage.setOpponentBuilderDirection(Direction.WEST);
        message.buildApolloParamMessage(godParamMessage);
        player1.getDivinePower().invokeGod(controller.getMatch(), message, turnLogic );
        assertEquals(3, player1.getBuilderF().getPosX());
        assertEquals(4, player1.getBuilderF().getPosY());
        assertEquals(3, player2.getBuilderF().getPosX());
        assertEquals(5, player2.getBuilderF().getPosY());
    }

}
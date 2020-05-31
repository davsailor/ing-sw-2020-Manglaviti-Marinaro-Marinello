package it.polimi.ingsw2020.santorini.model;

import it.polimi.ingsw2020.santorini.controller.GameLogic;
import it.polimi.ingsw2020.santorini.controller.TurnLogic;
import it.polimi.ingsw2020.santorini.exceptions.EndMatchException;
import it.polimi.ingsw2020.santorini.exceptions.IllegalConstructionException;
import it.polimi.ingsw2020.santorini.exceptions.IllegalMovementException;
import it.polimi.ingsw2020.santorini.model.gods.Athena;
import it.polimi.ingsw2020.santorini.model.gods.Pan;
import it.polimi.ingsw2020.santorini.network.server.Server;
import it.polimi.ingsw2020.santorini.network.server.VirtualView;
import it.polimi.ingsw2020.santorini.utils.Color;
import it.polimi.ingsw2020.santorini.utils.Direction;

import it.polimi.ingsw2020.santorini.utils.LevelType;
import it.polimi.ingsw2020.santorini.utils.PhaseType;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;



public class BuilderTest {

    private Server server;
    private GameLogic controller;
    private VirtualView view;
    private Player player1;
    private Player player2;
    private TurnLogic turnLogic;
    private PhaseType phase;
    private Board board;


    @Before
    public void setUp() {
        board = new Board((GodDeck) null);
        player1 = new Player("Dog", new Date(1,0,1900));
        player2 = new Player("Cat", new Date(1,0,2000));
        player1.setColor(Color.PLAYER_CYAN);
        player2.setColor(Color.PLAYER_GREEN);
        server = new Server();
        controller = new GameLogic(server);
        view = new VirtualView(controller);
        server.getWaitingPlayers().put(player1, 2);
        server.getWaitingPlayers().put(player2, 2);
        controller.initializeMatch(view, 2);
        int[] pos = new int[2];
        pos[0] = 3;
        pos[1] = 4;
        Builder builder1 = new Builder(player1, 'F', board, pos);
        player1.setBuilderF(builder1);
        pos[0] = 2;
        pos[1] = 2;
        Builder builder2 = new Builder(player1, 'M', board, pos);
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
        @Test
        public void testSwapBuilders(){

            player1.getBuilderF().swapBuilders(player2.getBuilderM());
            assertEquals(2, player1.getBuilderF().getPosX());
            assertEquals(2, player1.getBuilderF().getPosY());
            assertEquals(3, player2.getBuilderM().getPosX());
            assertEquals(4, player2.getBuilderM().getPosY());
        }

        @Test
        public void testGetColorAndPlayer(){

            assertEquals(Color.PLAYER_GREEN, player2.getBuilderM().getColor());
            assertEquals(player2, player2.getBuilderM().getPlayer());
        }

        @Test (expected = IllegalMovementException.class)
        public void testMove_wrongArgument_throwsIllegalMovementException() throws IllegalMovementException, EndMatchException{
            player1.getBuilderF().move(null);
        }

        @Test (expected = EndMatchException.class)
        public void testMove_wrongArgument_throwsEndMatchException() throws IllegalMovementException, EndMatchException {
            board.getBoard()[1][4].setLevel(LevelType.BASE);
            board.getBoard()[1][4].setLevel(LevelType.MID);
            board.getBoard()[2][4].setLevel(LevelType.BASE);
            board.getBoard()[1][3].setLevel(LevelType.BASE);
            board.getBoard()[1][3].setLevel(LevelType.MID);
            board.getBoard()[1][3].setLevel(LevelType.TOP);
            Direction direction =  Direction.NORTH;
            player1.getBuilderF().move(direction);
            direction =  Direction.NORTH;
            player1.getBuilderF().move(direction);
            direction =  Direction.WEST;
            player1.getBuilderF().move(direction);
        }

        @Test
        public void testMove() throws IllegalMovementException, EndMatchException {

            Direction direction =  Direction.NORTH;
            player1.getBuilderF().move(direction);
            assertEquals(2,player1.getBuilderF().getPosX());
            assertEquals(4,player1.getBuilderF().getPosY());
            assertEquals(4,player1.getBuilderF().getPossibleMoves()[2][1]);

            direction= Direction.SOUTH;//
            player1.getBuilderF().move(direction);
            assertEquals(3,player1.getBuilderF().getPosX());
            assertEquals(4,player1.getBuilderF().getPosY());
            assertEquals(4,player1.getBuilderF().getPossibleMoves()[0][1]);

            direction= Direction.NORTH_WEST;//
            player1.getBuilderF().move(direction);
            assertEquals(2,player1.getBuilderF().getPosX());
            assertEquals(3,player1.getBuilderF().getPosY());
            assertEquals(4,player1.getBuilderF().getPossibleMoves()[2][2]);

            direction= Direction.SOUTH_EAST;//
            player1.getBuilderF().move(direction);
            assertEquals(3,player1.getBuilderF().getPosX());
            assertEquals(4,player1.getBuilderF().getPosY());
            assertEquals(4,player1.getBuilderF().getPossibleMoves()[0][0]);

            direction= Direction.NORTH_EAST;//
            player1.getBuilderF().move(direction);
            assertEquals(2,player1.getBuilderF().getPosX());
            assertEquals(5,player1.getBuilderF().getPosY());
            assertEquals(4,player1.getBuilderF().getPossibleMoves()[2][0]);

            direction= Direction.SOUTH_WEST;
            player1.getBuilderF().move(direction);
            assertEquals(3,player1.getBuilderF().getPosX());
            assertEquals(4,player1.getBuilderF().getPosY());
            assertEquals(4,player1.getBuilderF().getPossibleMoves()[0][2]);

            direction= Direction.WEST;
            player1.getBuilderF().move(direction);
            assertEquals(3,player1.getBuilderF().getPosX());
            assertEquals(3,player1.getBuilderF().getPosY());
            assertEquals(4,player1.getBuilderF().getPossibleMoves()[1][2]);

            direction= Direction.EAST;
            player1.getBuilderF().move(direction);
            assertEquals(3,player1.getBuilderF().getPosX());
            assertEquals(4,player1.getBuilderF().getPosY());
            assertEquals(4,player1.getBuilderF().getPossibleMoves()[1][0]);

        }

        @Test (expected = IllegalConstructionException.class)
        public void testBuild_wrongArgument_throwsIllegalConstructionException() throws  IllegalConstructionException {
            player1.getBuilderF().build(null, controller.getMatch());
        }

        @Test
        public void testBuild() throws IllegalConstructionException {

            Direction direction = Direction.NORTH_WEST;
            player1.getBuilderF().build(direction, controller.getMatch());
            assertEquals(LevelType.BASE,board.getBoard()[player1.getBuilderF().getPosX()-1][player1.getBuilderF().getPosY()-1].getLevel());


            player1.getBuilderF().build(direction, controller.getMatch());
            assertEquals(LevelType.MID ,board.getBoard()[player1.getBuilderF().getPosX()-1][player1.getBuilderF().getPosY()-1].getLevel());


            player1.getBuilderF().build(direction, controller.getMatch());
            assertEquals(LevelType.TOP,board.getBoard()[player1.getBuilderF().getPosX()-1][player1.getBuilderF().getPosY()-1].getLevel());

            player1.getBuilderF().build(direction, controller.getMatch());
            assertEquals(LevelType.DOME,board.getBoard()[player1.getBuilderF().getPosX()-1][player1.getBuilderF().getPosY()-1].getLevel());
            assertEquals(-2 , player1.getBuilderF().getPossibleBuildings()[player1.getBuilderF().getBuildPosX()-player1.getBuilderF().getPosX() + 1][player1.getBuilderF().getBuildPosY()-player1.getBuilderF().getPosY() + 1]);

            direction = Direction.NORTH;
            player1.getBuilderF().build(direction, controller.getMatch());
            assertEquals(LevelType.BASE,board.getBoard()[player1.getBuilderF().getPosX()-1][player1.getBuilderF().getPosY()].getLevel());

            direction = Direction.NORTH_EAST;
            player1.getBuilderF().build(direction, controller.getMatch());
            assertEquals(LevelType.BASE,board.getBoard()[player1.getBuilderF().getPosX()-1][player1.getBuilderF().getPosY()+1].getLevel());

            direction = Direction.WEST;
            player1.getBuilderF().build(direction, controller.getMatch());
            assertEquals(LevelType.BASE,board.getBoard()[player1.getBuilderF().getPosX()][player1.getBuilderF().getPosY()-1].getLevel());

            direction = Direction.EAST;
            player1.getBuilderF().build(direction, controller.getMatch());
            assertEquals(LevelType.BASE,board.getBoard()[player1.getBuilderF().getPosX()][player1.getBuilderF().getPosY()+1].getLevel());


            direction = Direction.SOUTH_WEST;
            player1.getBuilderF().build(direction, controller.getMatch());
            assertEquals(LevelType.BASE,board.getBoard()[player1.getBuilderF().getPosX()+1][player1.getBuilderF().getPosY()-1].getLevel());

            direction = Direction.SOUTH;
            player1.getBuilderF().build(direction, controller.getMatch());
            assertEquals(LevelType.BASE,board.getBoard()[player1.getBuilderF().getPosX()-1][player1.getBuilderF().getPosY()].getLevel());

            direction = Direction.SOUTH_EAST;
            player1.getBuilderF().build(direction, controller.getMatch());
            assertEquals(LevelType.BASE,board.getBoard()[player1.getBuilderF().getPosX()+1][player1.getBuilderF().getPosY()+1].getLevel());
        }

    }

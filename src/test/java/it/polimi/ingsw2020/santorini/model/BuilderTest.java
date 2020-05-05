package it.polimi.ingsw2020.santorini.model;

import it.polimi.ingsw2020.santorini.exceptions.IllegalConstructionException;
import it.polimi.ingsw2020.santorini.exceptions.IllegalMovementException;
import it.polimi.ingsw2020.santorini.utils.Color;
import it.polimi.ingsw2020.santorini.utils.Direction;

import it.polimi.ingsw2020.santorini.utils.LevelType;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;



    public class BuilderTest {

        @Test
        public void swapBuildersTest(){
            Board board = new Board(null);
            Builder b1 = new Builder(null, 'm', board , null);
            b1.setPosX(1);
            b1.setPosY(1);
            Builder b2 = new Builder(null, 'm', board , null);
            b2.setPosX(2);
            b2.setPosY(2);
            b1.swapBuilders(b2);
            assertEquals(2, b1.getPosX());
            assertEquals(2, b1.getPosY());
            assertEquals(1, b2.getPosX());
            assertEquals(1, b2.getPosY());

        }

        @Test
        public void testGetColorAndPlayer(){
            Player player = new Player("davsailor", new Date(1999,2,15));
            player.setColor(Color.GREEN);
            Builder tester = new Builder(player, 'M', new Board(null), null);
            assertEquals(Color.GREEN, tester.getColor());
            assertEquals(player, tester.getPlayer());
        }

        @Test
        public void moveTest() throws IllegalMovementException {
            Board board = new Board(null);
            int[] pos = {3, 3};
            Builder builder = new Builder(null,'m',board, pos);

            Direction direction =  Direction.NORTH;
            builder.move(direction);
            assertEquals(2,builder.getPosX());
            assertEquals(3,builder.getPosY());
            assertEquals(4,builder.getPossibleMoves()[2][1]);

            direction= Direction.NORTH_WEST;
            builder.setPosX(3);
            builder.setPosY(3);
            builder.move(direction);
            assertEquals(2,builder.getPosX());
            assertEquals(2,builder.getPosY());
            assertEquals(4,builder.getPossibleMoves()[2][2]);

            direction= Direction.NORTH_EAST;
            builder.setPosX(3);
            builder.setPosY(3);
            builder.move(direction);
            assertEquals(2,builder.getPosX());
            assertEquals(4,builder.getPosY());
            assertEquals(4,builder.getPossibleMoves()[2][0]);

            direction= Direction.WEST;
            builder.setPosX(3);
            builder.setPosY(3);
            builder.move(direction);
            assertEquals(3,builder.getPosX());
            assertEquals(2,builder.getPosY());
            assertEquals(4,builder.getPossibleMoves()[1][2]);

            direction= Direction.EAST;
            builder.setPosX(3);
            builder.setPosY(3);
            builder.move(direction);
            assertEquals(3,builder.getPosX());
            assertEquals(4,builder.getPosY());
            assertEquals(4,builder.getPossibleMoves()[1][0]);

            direction= Direction.SOUTH_WEST;
            builder.setPosX(3);
            builder.setPosY(3);
            builder.move(direction);
            assertEquals(4,builder.getPosX());
            assertEquals(2,builder.getPosY());
            assertEquals(4,builder.getPossibleMoves()[0][2]);

            direction= Direction.SOUTH;
            builder.setPosX(3);
            builder.setPosY(3);
            builder.move(direction);
            assertEquals(4,builder.getPosX());
            assertEquals(3,builder.getPosY());
            assertEquals(4,builder.getPossibleMoves()[0][1]);

            direction= Direction.SOUTH_EAST;
            builder.setPosX(3);
            builder.setPosY(3);
            builder.move(direction);
            assertEquals(4,builder.getPosX());
            assertEquals(4,builder.getPosY());
            assertEquals(4,builder.getPossibleMoves()[0][0]);

        }

        @Test (expected = IllegalMovementException.class)
        public void moveTest_illegalInput_throwsIllegalMovementException() throws IllegalMovementException {
            Board board = new Board(null);
            Builder builder = new Builder(null,'m',board, null);
            int posX=3;
            int posY=3;
            builder.move(null);
        }

        @Test
        public void setPossibleBuildingsTest() {}

        @Test
        public void buildTest() throws IllegalConstructionException {
            Board board = new Board(null);
            int[] pos = {4, 4};
            Builder builder = new Builder(null, 'm', board, pos);

            Direction direction = Direction.NORTH_WEST;
            builder.build(direction);
            assertEquals(0,builder.getBuildPosX()-builder.getPosX() + 1);
            assertEquals(0,builder.getBuildPosY()-builder.getPosY() + 1);
            assertEquals(-2 , builder.getPossibleBuildings()[builder.getBuildPosX()-builder.getPosX() + 1][builder.getBuildPosY()-builder.getPosY() + 1]);
            assertEquals(LevelType.BASE, board.getBoard()[builder.getBuildPosX()][builder.getBuildPosY()].getLevel());

            builder.build(direction);
            assertEquals(0,builder.getBuildPosX()-builder.getPosX() + 1);
            assertEquals(0,builder.getBuildPosY()-builder.getPosY() + 1);
            assertEquals(-2 , builder.getPossibleBuildings()[builder.getBuildPosX()-builder.getPosX() + 1][builder.getBuildPosY()-builder.getPosY() + 1]);
            assertEquals(LevelType.MID, board.getBoard()[builder.getBuildPosX()][builder.getBuildPosY()].getLevel());

            builder.build(direction);
            assertEquals(0,builder.getBuildPosX()-builder.getPosX() + 1);
            assertEquals(0,builder.getBuildPosY()-builder.getPosY() + 1);
            assertEquals(-2 , builder.getPossibleBuildings()[builder.getBuildPosX()-builder.getPosX() + 1][builder.getBuildPosY()-builder.getPosY() + 1]);
            assertEquals(LevelType.TOP, board.getBoard()[builder.getBuildPosX()][builder.getBuildPosY()].getLevel());

            builder.build(direction);
            assertEquals(0,builder.getBuildPosX()-builder.getPosX() + 1);
            assertEquals(0,builder.getBuildPosY()-builder.getPosY() + 1);
            assertEquals(-2 , builder.getPossibleBuildings()[builder.getBuildPosX()-builder.getPosX() + 1][builder.getBuildPosY()-builder.getPosY() + 1]);
            assertEquals(LevelType.DOME, board.getBoard()[builder.getBuildPosX()][builder.getBuildPosY()].getLevel());

            direction = Direction.NORTH;
            builder.build(direction);
            assertEquals(0,builder.getBuildPosX()-builder.getPosX() + 1);
            assertEquals(1,builder.getBuildPosY()-builder.getPosY() + 1);
            assertEquals(-2 , builder.getPossibleBuildings()[builder.getBuildPosX()-builder.getPosX() + 1][builder.getBuildPosY()-builder.getPosY() + 1]);

            direction = Direction.NORTH_EAST;
            builder.build(direction);
            assertEquals(0,builder.getBuildPosX()-builder.getPosX() + 1);
            assertEquals(2,builder.getBuildPosY()-builder.getPosY() + 1);
            assertEquals(-2 , builder.getPossibleBuildings()[builder.getBuildPosX()-builder.getPosX() + 1][builder.getBuildPosY()-builder.getPosY() + 1]);

            direction = Direction.WEST;
            builder.build(direction);
            assertEquals(1,builder.getBuildPosX()-builder.getPosX() + 1);
            assertEquals(0,builder.getBuildPosY()-builder.getPosY() + 1);
            assertEquals(-2 , builder.getPossibleBuildings()[builder.getBuildPosX()-builder.getPosX() + 1][builder.getBuildPosY()-builder.getPosY() + 1]);

            direction = Direction.EAST;
            builder.build(direction);
            assertEquals(1,builder.getBuildPosX()-builder.getPosX() + 1);
            assertEquals(2,builder.getBuildPosY()-builder.getPosY() + 1);
            assertEquals(-2 , builder.getPossibleBuildings()[builder.getBuildPosX()-builder.getPosX() + 1][builder.getBuildPosY()-builder.getPosY() + 1]);

            direction = Direction.SOUTH_WEST;
            builder.build(direction);
            assertEquals(2,builder.getBuildPosX()-builder.getPosX() + 1);
            assertEquals(0,builder.getBuildPosY()-builder.getPosY() + 1);
            assertEquals(-2 , builder.getPossibleBuildings()[builder.getBuildPosX()-builder.getPosX() + 1][builder.getBuildPosY()-builder.getPosY() + 1]);

            direction = Direction.SOUTH;
            builder.build(direction);
            assertEquals(2,builder.getBuildPosX()-builder.getPosX() + 1);
            assertEquals(1,builder.getBuildPosY()-builder.getPosY() + 1);
            assertEquals(-2 , builder.getPossibleBuildings()[builder.getBuildPosX()-builder.getPosX() + 1][builder.getBuildPosY()-builder.getPosY() + 1]);

            direction = Direction.SOUTH_EAST;
            builder.build(direction);
            assertEquals(2,builder.getBuildPosX()-builder.getPosX() + 1);
            assertEquals(2,builder.getBuildPosY()-builder.getPosY() + 1);
            assertEquals(-2 , builder.getPossibleBuildings()[builder.getBuildPosX()-builder.getPosX() + 1][builder.getBuildPosY()-builder.getPosY() + 1]);
        }

        @Test (expected = IllegalConstructionException.class)
        public void testBuild_tooMuchBuildActions_throwsException() throws IllegalConstructionException{
            Board board = new Board(null);
            int[] pos = {4, 4};
            Builder builder = new Builder(null, 'm', board, pos);

            Direction direction = Direction.NORTH_WEST;
            builder.build(direction);
            builder.build(direction);
            builder.build(direction);
            builder.build(direction);
            builder.build(direction);
        }
    }

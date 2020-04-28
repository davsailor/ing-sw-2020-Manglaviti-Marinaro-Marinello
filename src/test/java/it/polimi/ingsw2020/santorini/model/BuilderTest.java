package it.polimi.ingsw2020.santorini.model;

import it.polimi.ingsw2020.santorini.exceptions.IllegalConstructionException;
import it.polimi.ingsw2020.santorini.exceptions.IllegalMovementException;
import it.polimi.ingsw2020.santorini.utils.Direction;

import org.junit.Test;
import org.junit.Rule;
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
        public void setPossibleMovesTest(){     }

        @Test
        public void getPossibleMovesTest(){ }

        @Test
        public void moveTest() throws IllegalMovementException {
            Board board = new Board(null);
            Builder builder = new Builder(null,'m',board, null);
            int posX=3;
            int posY=3;

            Direction direction =  Direction.NORTH;
            posX=3;
            posY=3;
            builder.move(direction, posX, posY);
            assertEquals(2,builder.getPosX());
            assertEquals(3,builder.getPosY());
            assertEquals(4,builder.getPossibleMoves()[2][1]);

            direction= Direction.NORTH_WEST;
            posX=3;
            posY=3;
            builder.move(direction, posX, posY);
            assertEquals(2,builder.getPosX());
            assertEquals(2,builder.getPosY());
            assertEquals(4,builder.getPossibleMoves()[2][2]);

            direction= Direction.NORTH_EAST;
            posX=3;
            posY=3;
            builder.move(direction, posX, posY);
            assertEquals(2,builder.getPosX());
            assertEquals(4,builder.getPosY());
            assertEquals(4,builder.getPossibleMoves()[2][0]);

            direction= Direction.WEST;
            posX=3;
            posY=3;
            builder.move(direction, posX, posY);
            assertEquals(3,builder.getPosX());
            assertEquals(2,builder.getPosY());
            assertEquals(4,builder.getPossibleMoves()[1][2]);

            direction= Direction.EAST;
            posX=3;
            posY=3;
            builder.move(direction, posX, posY);
            assertEquals(3,builder.getPosX());
            assertEquals(4,builder.getPosY());
            assertEquals(4,builder.getPossibleMoves()[1][0]);

            direction= Direction.SOUTH_WEST;
            posX=3;
            posY=3;
            builder.move(direction, posX, posY);
            assertEquals(4,builder.getPosX());
            assertEquals(2,builder.getPosY());
            assertEquals(4,builder.getPossibleMoves()[0][2]);

            direction= Direction.SOUTH;
            posX=3;
            posY=3;
            builder.move(direction, posX, posY);
            assertEquals(4,builder.getPosX());
            assertEquals(3,builder.getPosY());
            assertEquals(4,builder.getPossibleMoves()[0][1]);

            direction= Direction.SOUTH_EAST;
            posX=3;
            posY=3;
            builder.move(direction, posX, posY);
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
            builder.move(null,posX,posY);
        }

        @Test
        public void setPossibleBuildingsTest() {}

        @Test
        public void buildTest() throws IllegalConstructionException {
            Board board = new Board(null);
            Builder builder = new Builder(null, 'm', board, null);
            int posX = 4;
            int posY = 4;
            int buildPosX=3;
            int buildPosY=3;
            Direction direction = Direction.NORTH_WEST;
            builder.build(posX,posY,direction);
            assertEquals(0,builder.i);
            assertEquals(0,builder.j);
            assertEquals(-2 , builder.getPossibleBuildings()[builder.i][builder.j]);

            buildPosX=3;
            buildPosY=4;
            direction = Direction.NORTH;
            builder.build(posX,posY,direction);
            assertEquals(0,builder.i);
            assertEquals(1,builder.j);
            assertEquals(-2 , builder.getPossibleBuildings()[builder.i][builder.j]);
            buildPosX=3;
            buildPosY=5;
            direction = Direction.NORTH_EAST;
            builder.build(posX,posY,direction);
            assertEquals(0,builder.i);
            assertEquals(2,builder.j);
            assertEquals(-2 , builder.getPossibleBuildings()[builder.i][builder.j]);
            buildPosX=4;
            buildPosY=3;
            direction = Direction.WEST;
            builder.build(posX,posY,direction);
            assertEquals(1,builder.i);
            assertEquals(0,builder.j);
            assertEquals(-2 , builder.getPossibleBuildings()[builder.i][builder.j]);
            buildPosX=4;
            buildPosY=5;
            direction = Direction.EAST;
            builder.build(posX,posY,direction);
            assertEquals(1,builder.i);
            assertEquals(2,builder.j);
            assertEquals(-2 , builder.getPossibleBuildings()[builder.i][builder.j]);
            buildPosX=5;
            buildPosY=3;
            direction = Direction.SOUTH_WEST;
            builder.build(posX,posY,direction);
            assertEquals(2,builder.i);
            assertEquals(0,builder.j);
            assertEquals(-2 , builder.getPossibleBuildings()[builder.i][builder.j]);
            buildPosX=5;
            buildPosY=4;
            direction = Direction.SOUTH;
            builder.build(posX,posY,direction);
            assertEquals(2,builder.i);
            assertEquals(1,builder.j);
            assertEquals(-2 , builder.getPossibleBuildings()[builder.i][builder.j]);
            buildPosX=5;
            buildPosY=5;
            direction = Direction.SOUTH_EAST;
            builder.build(posX,posY,direction);
            assertEquals(2,builder.i);
            assertEquals(2,builder.j);
            assertEquals(-2 , builder.getPossibleBuildings()[builder.i][builder.j]);
        }
    }

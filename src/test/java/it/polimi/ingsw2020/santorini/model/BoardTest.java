package it.polimi.ingsw2020.santorini.model;

import it.polimi.ingsw2020.santorini.utils.AccessType;
import it.polimi.ingsw2020.santorini.utils.LevelType;

import org.junit.Test;
import org.junit.Rule;
import static org.junit.Assert.*;

public class BoardTest {

    @Test
    public void testNeighboringStatusCell() {
        Board board = new Board(null);
        int[][] neighborMatrix;
        neighborMatrix = new int[3][3];
        board.getBoard()[1][2].setLevel(LevelType.BASE);
        board.getBoard()[2][1].setLevel(LevelType.MID);
        //neighborMatrix = board.neighboringStatusCell(board.getBoard(), 1,1, AccessType.FREE);
        assertEquals(0, neighborMatrix[0][0]);
        assertEquals(1, neighborMatrix[2][2]);
        assertEquals(0, neighborMatrix[2][1]);
        assertEquals(2, neighborMatrix[1][2]);
        //neighborMatrix = board.neighboringStatusCell(board.getBoard(),2,1, AccessType.FREE);
        assertEquals(3, neighborMatrix[0][1]);
    }

    @Test
    public void testNeighborLevelCell() {
        Board board = new Board(null);
        int[][] neighborMatrix;
        neighborMatrix = new int[3][3];
        board.buildBlock(2, 2, LevelType.BASE);
        //neighborMatrix = board.neighborLevelCell(board.getBoard(),1 ,1 );
        assertEquals(-1, neighborMatrix[0][0]);
        assertEquals(1, neighborMatrix[2][2]);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNeighborLevelCell_wrongArgument_throwsException(){
        Board board = new Board(null);
        int[][] matrix = new int[3][3];
        //matrix = board.neighborLevelCell(board.getBoard(),0,0);
    }

    @Test
    public void testBuildBlock() {
        Board board = new Board(null);
        board.buildBlock(0,0, LevelType.BASE);
        assertEquals(LevelType.BASE, board.getBoard()[0][0].getLevel());
    }
}
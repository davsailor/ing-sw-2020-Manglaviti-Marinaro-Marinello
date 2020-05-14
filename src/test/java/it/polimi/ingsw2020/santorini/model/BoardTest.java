package it.polimi.ingsw2020.santorini.model;

import it.polimi.ingsw2020.santorini.utils.AccessType;
import it.polimi.ingsw2020.santorini.utils.LevelType;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {

    private Board board;
    private Player player1;
    private Player player2;

    @Before
    public void setUp() {
        board = new Board((Cell[][]) null);
        player1 = new Player("Dog", null);
        player2 = new Player ("Cat", null);
        int[] pos = new int[2];
        pos[0] = 3;
        pos[1] = 3;
        Builder builder1 = new Builder(player1, 'F', board, pos);
        player1.setBuilderF(builder1);
        pos[0] = 2;
        pos[1] = 3;
        Builder builder2 = new Builder(player2, 'M', board, pos);
        player2.setBuilderM(builder2);
    }




    @Test
    public void testNeighboringStatusCell() {
        int[][] neighborMatrix;
        setUp();
        neighborMatrix = new int[3][3];
        board.getBoard()[3][2].setLevel(LevelType.BASE);
        board.getBoard()[2][3].setLevel(LevelType.MID);
        neighborMatrix = board.neighboringStatusCell(player1.getBuilderF(), AccessType.FREE);
        System.out.println(player1.getBuilderF().getBoard().getBoard()[4][4].getLevel().toString());
        System.out.println(player1.getBuilderF().getBoard().getBoard()[4][4].getStatus().toString());
        assertEquals(1, neighborMatrix[0][0]);
        assertEquals(1, neighborMatrix[2][2]);
        assertEquals(2, neighborMatrix[1][0]);
        assertEquals(0, neighborMatrix[0][1]);
        neighborMatrix = board.neighboringStatusCell(player2.getBuilderM(), AccessType.FREE);
        assertEquals(3, neighborMatrix[0][1]);

    }

    @Test
    public void testNeighborLevelCell() {
        int[][] neighborMatrix;
        setUp();
        neighborMatrix = new int[3][3];
        board.buildBlock(2, 2, LevelType.TOP);
        neighborMatrix = board.neighboringLevelCell(player1.getBuilderF());
        assertEquals(3, neighborMatrix[0][0]);
        assertEquals(-1, neighborMatrix[0][1]);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNeighborLevelCell_wrongArgument_throwsException(){
        Board board = new Board((Cell[][])null);
        int[][] matrix = new int[3][3];
        matrix = Board.neighboringLevelCell(player1.getBuilderF());
    }

    @Test
    public void testBuildBlock() {
        Board board = new Board((Cell[][]) null);
        board.buildBlock(0,0, LevelType.BASE);
        assertEquals(LevelType.BASE, board.getBoard()[0][0].getLevel());
    }
}

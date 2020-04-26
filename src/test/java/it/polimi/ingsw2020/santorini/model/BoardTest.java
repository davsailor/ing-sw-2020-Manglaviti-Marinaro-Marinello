package it.polimi.ingsw2020.santorini.model;

import it.polimi.ingsw2020.santorini.utils.AccessType;
import it.polimi.ingsw2020.santorini.utils.LevelType;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw2020.santorini.model.Board;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void neighboringStatusCellTest() {
        Board board = new Board(null);
        int[][] neighborMatrix;
        neighborMatrix = new int[3][3];
        neighborMatrix = board.neighboringStatusCell(1,1, AccessType.FREE);
        assertEquals(0, neighborMatrix[0][0]);
        assertEquals(1, neighborMatrix[2][2]);
    }

    @Test
    void neighborLevelCellTest() {
        Board board = new Board(null);
        int[][] neighborMatrix;
        neighborMatrix = new int[3][3];
        board.buildBlock(2, 2, LevelType.BASE);
        neighborMatrix = board.neighborLevelCell(1 ,1 );
        assertEquals(0, neighborMatrix[0][0]);
        assertEquals(1, neighborMatrix[2][2]);
    }

    @Test
    void buildBlockTest() {
        Board board = new Board(null);
        board.buildBlock(0,0, LevelType.BASE);
        assertEquals(LevelType.BASE, board.getBoard()[0][0].getLevel());
    }
}
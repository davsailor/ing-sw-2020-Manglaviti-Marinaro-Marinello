package it.polimi.ingsw2020.santorini.model;
import it.polimi.ingsw2020.santorini.utils.AccessType;
import it.polimi.ingsw2020.santorini.model.Cell;
import it.polimi.ingsw2020.santorini.utils.LevelType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    //Cell cell = null;
    //@Before
    //public void setUp(){
        //cell = new Cell(AccessType.FREE);
    //}

    //@After
    //public void tearDwn(){}

    @Test
    void getLevel_() {
        Cell cell = new Cell(AccessType.FREE);
        assertEquals(0, cell.getLevel().getHeight());
    }

    @Test
    void setLevel() {
        Cell cell = new Cell(AccessType.FREE);
        cell.setLevel(LevelType.BASE);
        assertEquals(1, cell.getLevel().getHeight());
    }

    @Test
    void getStatus() {
        Cell cell = new Cell(AccessType.FREE);
        assertEquals( AccessType.FREE.toString(), cell.getStatus());
    }

    @Test
    void setStatus() {
        Cell cell = new Cell(AccessType.FREE);
        cell.setStatus(AccessType.OCCUPIED);
        assertEquals( AccessType.OCCUPIED.toString(), cell.getStatus());
    }

    @Test
    void calculateJump() {
        Cell cell = new Cell(AccessType.FREE);
        Cell cell2 = new Cell(AccessType.FREE);
        cell2.setLevel(LevelType.BASE);
        assertEquals( 1, cell.calculateJump(cell2));
    }
}
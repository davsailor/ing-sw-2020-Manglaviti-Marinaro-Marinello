package it.polimi.ingsw2020.santorini.model;
import it.polimi.ingsw2020.santorini.utils.AccessType;
import it.polimi.ingsw2020.santorini.utils.LevelType;

import org.junit.Test;
import static org.junit.Assert.*;

public class CellTest {

    @Test
    public void getLevel_() {
        Cell cell = new Cell(AccessType.FREE);
        assertEquals(0, cell.getLevel().getHeight());
    }

    @Test
    public void setLevel() {
        Cell cell = new Cell(AccessType.FREE);
        cell.setLevel(LevelType.BASE);
        assertEquals(1, cell.getLevel().getHeight());
    }

    @Test
    public void getStatus() {
        Cell cell = new Cell(AccessType.FREE);
        assertEquals( AccessType.FREE, cell.getStatus());
    }

    @Test
    public void setStatus() {
        Cell cell = new Cell(AccessType.FREE);
        cell.setStatus(AccessType.OCCUPIED);
        assertEquals( AccessType.OCCUPIED, cell.getStatus());
    }

    @Test
    public void calculateJump() {
        Cell cell = new Cell(AccessType.FREE);
        Cell cell2 = new Cell(AccessType.FREE);
        cell2.setLevel(LevelType.BASE);
        assertEquals( 1, cell.calculateJump(cell2));
    }
}
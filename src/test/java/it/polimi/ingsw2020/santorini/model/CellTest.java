package it.polimi.ingsw2020.santorini.model;
import it.polimi.ingsw2020.santorini.utils.AccessType;
import it.polimi.ingsw2020.santorini.utils.LevelType;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class CellTest {

    @Test
    public void testGetLevel() {
        Cell cell = new Cell(AccessType.FREE);
        assertEquals(0, cell.getLevel().getHeight());
    }

    @Test
    public void testSetLevel() {
        Cell cell = new Cell(AccessType.FREE);
        cell.setLevel(LevelType.BASE);
        assertEquals(1, cell.getLevel().getHeight());
    }

    @Test
    public void testSetBuilder(){
        Cell cell = new Cell(AccessType.FREE);
        Player player = new Player("davsailor", new Date(1999, 2, 15));
        Builder builder = new Builder(player, '♂', new Board(new GodDeck(2)), null);
        cell.setBuilder(builder);
        assertEquals(builder, cell.getBuilder());
    }

    @Test
    public void testGetBuilder(){
        Cell cell = new Cell(AccessType.FREE);
        assertNull(cell.getBuilder());
        Player player = new Player("davsailor", new Date(1999, 2, 15));
        Builder builder = new Builder(player, '♂', new Board(new GodDeck(2)), null);
        cell.setBuilder(builder);
        assertNotNull(cell.getBuilder());
        assertEquals(builder, cell.getBuilder());
    }

    @Test
    public void testGetStatus() {
        Cell cell = new Cell(AccessType.FREE);
        assertEquals( AccessType.FREE, cell.getStatus());
    }

    @Test
    public void testSetStatus() {
        Cell cell = new Cell(AccessType.FREE);
        cell.setStatus(AccessType.OCCUPIED);
        assertEquals( AccessType.OCCUPIED, cell.getStatus());
    }

    @Test
    public void testCalculateJump() {
        Cell cell = new Cell(AccessType.FREE);
        Cell cell2 = new Cell(AccessType.FREE);
        cell2.setLevel(LevelType.BASE);
        assertEquals( 1, cell.calculateJump(cell2));
    }

    @Test
    public void testDemolish(){
        Cell cell = new Cell(AccessType.FREE);
        cell.setLevel(LevelType.BASE);
        cell.setLevel(LevelType.MID);
        cell.demolish();
        assertEquals(LevelType.BASE, cell.getLevel());
    }
}
package edu.cmu.cs214.Santorini.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class CellTest {
    @Test
    public void testConstructor() {
        Cell cell = new Cell();
        assertEquals(cell.getHeight(), 0);
        cell = new Cell(Cell.MAX_HEIGHT);
        assertEquals(cell.getHeight(), Cell.MAX_HEIGHT);
    }

    @Test(expected = Cell.InvalidCellHeightError.class)
    public void testConstructorInvalidHeight() {
        new Cell(Cell.MAX_HEIGHT + 1);
    }

    @Test
    public void testHeights() {
        Cell cell = new Cell(Cell.MAX_HEIGHT - 1);
        assertTrue(cell.isHighestWithoutDome());
        assertFalse(cell.isDome());
        cell.setHeight(-Cell.MAX_HEIGHT);
        assertTrue(cell.isDome());
        assertFalse(cell.isHighestWithoutDome());
        cell.setHeight(0);
        assertFalse(cell.isDome());
        assertFalse(cell.isHighestWithoutDome());
    }

    @Test
    public void testBuild() {
        Cell cell = new Cell();
        assertEquals(cell.getHeight(), 0);
        assertEquals(cell.build(), 1);
        assertEquals(cell.getHeight(), 1);

        cell = new Cell(-Cell.MAX_HEIGHT);
        assertEquals(cell.getHeight(), -Cell.MAX_HEIGHT);
        assertEquals(cell.build(), 0);
        assertEquals(cell.getHeight(), -Cell.MAX_HEIGHT);
    }

    @Test
    public void testBuildDome() {
        Cell cell = new Cell();
        assertEquals(0, cell.getHeight() );
        assertEquals(-1, cell.buildDome() );
        assertEquals(-1, cell.getHeight());
        assertTrue(cell.isDome());

        cell = new Cell(-2);
        assertTrue(cell.isDome());
        assertEquals(0, cell.buildDome());
        assertEquals(-2, cell.getHeight());
    }

    @Test
    public void testCopy() {
        Cell cell = new Cell(3);
        Cell cellCopy = cell.copy();
        assertEquals(3, cellCopy.getHeight());
        cell.setHeight(2);
        assertEquals(2, cell.getHeight());
        assertEquals(3, cellCopy.getHeight());
    }
}

package edu.cmu.cs214.Santorini.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class DirectionTest {
    @Test
    public void testGetDirectionFromKey() {
        assertEquals(Direction.N, Direction.getDirectionFromKey("N"));
        assertEquals(Direction.NE, Direction.getDirectionFromKey("NE"));
        assertEquals(Direction.W, Direction.getDirectionFromKey("W"));
        assertNull(Direction.getDirectionFromKey(""));
        assertNull(Direction.getDirectionFromKey("Q"));
    }

}

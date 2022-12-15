package edu.cmu.cs214.Santorini.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PointTest {
    @Test
    public void test() {
        System.out.println("point test");
        Point p1 = new Point(1, 1);
        Point p2 = new Point(2, 1);
        Point p3 = new Point(2, 1);
        assertEquals(p2, p3);
        assertEquals(p2.hashCode(), p3.hashCode());
        assertEquals(p3, p2);
        assertNotEquals(p1, p2);
        assertNotEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1, p3);
        assertNotEquals(p1.hashCode(), p3.hashCode());
        assertNotEquals(p1, null);
        assertNotEquals(null, p2);
    }
}

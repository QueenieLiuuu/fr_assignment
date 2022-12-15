package edu.cmu.cs214.Santorini.model;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PlayerTest {
    Board board;
    Player player;

    @Before
    public void setUp() {
        board = new Board();
        player = new Player(1);
    }

    @Test
    public void testAddWorker() {
        assertEquals(0, player.getWorkers().size());
        assertTrue(player.addWorker(board, new Point(0, 0)));
        assertEquals(1, player.getWorkers().size());
        // test add worker oob
        assertFalse(player.addWorker(board, new Point(-1, 0)));
        // test add worker on occupied cell
        assertFalse(player.addWorker(board, new Point(0, 0)));
        assertTrue(player.addWorker(board, new Point(0, 1)));
        assertEquals(2, player.getWorkers().size());

        // test add 3rd worker
        assertFalse(player.addWorker(board, new Point(1, 1)));
        assertEquals(2, player.getWorkers().size());
    }

    @Test
    public void testCopy() {
        player.addWorker(board, new Point(1, 2));
        player.addWorker(board, new Point(2, 2));
        Player copyPlayer = player.copy();
        assertEquals(copyPlayer.getWorkers().get(0).getP(), player.getWorkers().get(0).getP());
        player.getWorkers().get(0).setP(new Point(0, 0));
        assertEquals(new Point(1, 2), copyPlayer.getWorkers().get(0).getP());
    }
}

package edu.cmu.cs214.Santorini.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WorkerTest {
    Player player;
    Worker worker;
    @Before
    public void setUp() {
        player = new Player(1);
        worker = new Worker(1);
    }
    @Test
    public void testConstructor() {
        assertEquals(1, worker.getId());
        worker = new Worker(2, player);
        assertEquals(player.getId(), worker.getPlayer().getId());
    }

    @Test
    public void testCopy() {
        worker.setP(new Point(1, 3));
        Worker workerCopy = worker.copy();
        assertEquals(worker.getPlayer(), workerCopy.getPlayer());
        assertEquals(worker.getId(), workerCopy.getId());
        assertEquals(worker.getP(), workerCopy.getP());
    }

    @Test
    public void testGetWorkerById() {
        Player p1 = new Player(1);
        Worker w1 = new Worker(1, p1);
        Worker w2 = new Worker(2, p1);
        p1.getWorkers().add(w1);
        p1.getWorkers().add(w2);
        Player p2 = new Player(2);
        Worker w3 = new Worker(3, p2);
        Worker w4 = new Worker(4, p2);
        p2.getWorkers().add(w3);
        p2.getWorkers().add(w4);
        assertEquals(w1, Worker.getWorkerById(p1, p2, 1));
        assertEquals(w2, Worker.getWorkerById(p1, p2, 2));
        assertEquals(w3, Worker.getWorkerById(p1, p2, 3));
        assertEquals(w4, Worker.getWorkerById(p1, p2, 4));
    }
}

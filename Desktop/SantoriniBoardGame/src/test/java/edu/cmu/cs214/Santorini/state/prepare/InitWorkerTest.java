package edu.cmu.cs214.Santorini.state.prepare;

import edu.cmu.cs214.Santorini.AbstractTest;
import edu.cmu.cs214.Santorini.game.Context;
import edu.cmu.cs214.Santorini.model.Point;
import edu.cmu.cs214.Santorini.state.run.BeforeMove;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class InitWorkerTest extends AbstractTest {
    @Test
    public void testInitWorker() {
        Context init = super.createContextAfterGodChosen();
        InitWorker state = (InitWorker) init.getState();

        assertEquals(0, init.getPlayer1().getWorkers().size());
        // test oob
        assertNull(state.onInitWorker(init.getBoard(), init.getPlayer1(), new Point(6, 4)));
        assertEquals(0, init.getPlayer1().getWorkers().size());

        Context next = state.onInitWorker(init.getBoard(), init.getPlayer1(), new Point(1, 1));
        assertEquals(InitWorker.class, next.getState().getClass());
        next = ((InitWorker) next.getState()).onInitWorker(next.getBoard(), next.getPlayer1(),
                new Point(2, 1));
        assertEquals(InitWorker.class, next.getState().getClass());
        // test add 3rd worker fail
        assertNull(((InitWorker) next.getState()).onInitWorker(next.getBoard(), next.getPlayer1(), new Point(3,
                1)));
        assertEquals(2, next.getPlayer1().getWorkers().size());

        // test add worker on occupied cell
        assertNull(state.onInitWorker(next.getBoard(), next.getPlayer1(), new Point(1, 1)));
        next = ((InitWorker) next.getState()).onInitWorker(next.getBoard(), next.getPlayer2(), new Point(1, 2));
        next = ((InitWorker) next.getState()).onInitWorker(next.getBoard(), next.getPlayer2(), new Point(1, 3));
        assertEquals(2, next.getPlayer1().getWorkers().size());
        assertEquals(2, next.getPlayer2().getWorkers().size());
        // next state should be beforeMove
        assertEquals(BeforeMove.class, next.getState().getClass());
     }
}

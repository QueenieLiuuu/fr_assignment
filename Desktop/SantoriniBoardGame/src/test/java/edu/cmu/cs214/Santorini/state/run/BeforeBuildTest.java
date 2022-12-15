package edu.cmu.cs214.Santorini.state.run;

import edu.cmu.cs214.Santorini.AbstractTest;
import edu.cmu.cs214.Santorini.game.Context;
import edu.cmu.cs214.Santorini.model.Point;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BeforeBuildTest extends AbstractTest {
    BeforeBuild state;

    @Before
    public void setUp() {
        super.setUp();
        Context context = super.createContextBeforeBuild();
        state = (BeforeBuild) context.getState();
        worker1 = context.getPlayer1().getWorkers().get(0);
        worker2 = context.getPlayer1().getWorkers().get(1);
        worker3 = context.getPlayer2().getWorkers().get(0);
        worker4 = context.getPlayer2().getWorkers().get(1);
        board = context.getBoard();
    }

    @Test
    public void testMove() {
        // for man, cannot move after move
        assertNull(state.onMove(worker2, new Point(0, 3)));
    }

    @Test
    public void testBuild() {
        // test inactive worker cannot build
        assertNull(state.onBuild(worker1, new Point(0, 3), false));
        assertEquals(0, board.getCell(0, 3).getHeight());

        // test build on self
        assertNull(state.onBuild(worker2, new Point(0, 2), false));
        // test building on another worker
        assertNull(state.onBuild(worker2, new Point(1, 1), false));
        // test building oob
        assertNull(state.onBuild(worker1, new Point(-1, 2)));
        board.setCell(0, 3, -4);
        // test building on dome
        assertNull(state.onBuild(worker2, new Point(0, 3)));
        board.setCell(0, 3, 0);
        // test build successfully
        Context next = state.onBuild(worker2, new Point(0, 3));

        assertEquals(BeforeMove.class, next.getState().getClass());
        assertEquals(1, next.getBoard().getCell(0, 3).getHeight());

        // activePlayerIdSwitched
        assertEquals(2, next.getActivePlayerId());
        // activeWorkerCleared
        assertEquals(0, next.getActiveWorkerId());
    }
}

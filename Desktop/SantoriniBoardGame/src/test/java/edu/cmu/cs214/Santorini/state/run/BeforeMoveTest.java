package edu.cmu.cs214.Santorini.state.run;

import edu.cmu.cs214.Santorini.AbstractTest;
import edu.cmu.cs214.Santorini.game.Context;
import edu.cmu.cs214.Santorini.model.Point;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BeforeMoveTest extends AbstractTest {
    BeforeMove state;

    @Before
    public void setUp() {
        super.setUp();
        Context context = super.createContextAfterInitWorkers();
        state = (BeforeMove) context.getState();
        player1 = context.getPlayer1();
        player2 = context.getPlayer2();
        worker1 = context.getPlayer1().getWorkers().get(0);
        worker2 = context.getPlayer1().getWorkers().get(1);
        worker3 = context.getPlayer2().getWorkers().get(0);
        worker4 = context.getPlayer2().getWorkers().get(1);
        board = context.getBoard();
    }

    @Test
    public void testMove() {
        assertEquals(2, player1.getWorkers().size());
        assertEquals(2, player2.getWorkers().size());
        // test wrong moving order
        assertNull(state.onMove(worker3, new Point(0, 0)));
        assertEquals(new Point(1, 0), worker3.getP());

        // test moving to self
        assertNull(state.onMove(worker1, new Point(0, 0)));
        // test moving to another worker
        assertNull(state.onMove(worker1, new Point(0, 1)));
        // test moving oob
        assertNull(state.onMove(worker1, new Point(0, -1)));
        board.setCell(0, 2, -4);
        // test moving to dome
        assertNull(state.onMove(worker2, new Point(0, 2)));
        board.setCell(0, 2, 0);
        // test moving successfully
        Context next = state.onMove(worker2, new Point(0, 2));

        assertEquals(BeforeBuild.class, next.getState().getClass());
        assertEquals(new Point(0, 2), next.getPlayer1().getWorkers().get(1).getP());
    }

    @Test
    public void testBuild() {
        // for man, cannot build before move
        assertNull(state.onBuild(worker2, new Point(0, 2), false));
    }

    @Test
    public void testCheckLose() {
        assertFalse(state.onCheckLose(board, player1));
        assertFalse(state.onCheckLose(board, player2));
        board.setCell(0, 2, 1);
        board.setCell(1, 2, 1);
        // height=1 wall cannot block
        assertFalse(state.onCheckLose(board, player1));
        assertFalse(state.onCheckLose(board, player2));
        board.setCell(0, 2, 2);
        board.setCell(1, 2, 2);
        assertTrue(state.onCheckLose(board, player1));
    }
}

package edu.cmu.cs214.Santorini.game;

import edu.cmu.cs214.Santorini.AbstractTest;
import edu.cmu.cs214.Santorini.model.Point;
import edu.cmu.cs214.Santorini.state.run.BeforeMove;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.HashSet;

public class ContextTest extends AbstractTest {
    @Test
    public void testContext() {
        Context cur = createContextAfterInitWorkers();
        BeforeMove state = new BeforeMove();
        cur.getBoard().setCell(1, 1, 1);
        Context next = cur.generateNext(state, new HashSet<>(), cur.copy());
        assertEquals(cur.getId() + 1, next.getId());
        assertEquals(cur.getPlayer1().toString(), next.getPlayer1().toString());
        assertEquals(cur.getPlayer2().toString(), next.getPlayer2().toString());
        assertEquals(state.getClass().getName(), next.getState().getClass().getName());
        assertEquals(cur.getActivePlayerId(), next.getActivePlayerId());
        assertEquals(cur.getBoard().getCell(1, 1), next.getBoard().getCell(1, 1));
        assertEquals(3 - next.getCurrentPlayer().getId(), next.getOpponentPlayer().getId());
        assertEquals(cur.toString(), next.getLastContext().toString());

        // test switch player
        cur = next;
        state = new BeforeMove();
        next = cur.generateNext(state, true, new HashSet<>(), next.copy());
        assertEquals(cur.getId() + 1, next.getId());
        assertEquals(cur.getPlayer1().toString(), next.getPlayer1().toString());
        assertEquals(cur.getPlayer2().toString(), next.getPlayer2().toString());
        assertEquals(cur.getOpponentPlayer().toString(), next.getCurrentPlayer().toString());
        assertEquals(cur.getCurrentPlayer().toString(), next.getOpponentPlayer().toString());
        assertEquals(3 - next.getCurrentPlayer().getId(), next.getOpponentPlayer().getId());

        // test immutability correct
        cur.getPlayer1().getWorkers().get(0).setP(new Point(3, 3));
        assertEquals(new Point(0, 0), next.getPlayer1().getWorkers().get(0).getP());
    }

}

package edu.cmu.cs214.Santorini.god.strategy;

import edu.cmu.cs214.Santorini.AbstractTest;
import edu.cmu.cs214.Santorini.game.Context;
import edu.cmu.cs214.Santorini.god.GodFactory;
import edu.cmu.cs214.Santorini.god.GodName;
import edu.cmu.cs214.Santorini.model.Player;
import edu.cmu.cs214.Santorini.model.Point;
import edu.cmu.cs214.Santorini.state.run.BeforeBuild;
import edu.cmu.cs214.Santorini.state.run.BeforeMove;
import edu.cmu.cs214.Santorini.state.run.RunState;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MinotaurStrategyTest extends AbstractTest {
    RunState state;
    Player player1;
    Player player2;
    @Before
    public void setUp() {
        super.setUp();
        Context context = super.createContextAfterInitWorkers();
        state = (BeforeMove) context.getState();
        player1 = state.getContext().getPlayer1();
        player2 = state.getContext().getPlayer2();
        player1.setGod(GodFactory.createGod(GodName.Minotaur));
    }

    @Test
    public void testMinotaur() {
        assertEquals("Minotaur", player1.getGod().getName());

        // test cannot dash an ally worker
        assertNull(state.onMove(player1.getWorkers().get(0), new Point(0, 1)));
        // test cannot dash to a wall
        assertNull(state.onMove(player1.getWorkers().get(1), new Point(1, 0)));
        // test push an opponent worker
        Context next = state.onMove(player1.getWorkers().get(0), new Point(1, 1));
        assertEquals(BeforeBuild.class, next.getState().getClass());
        assertEquals(new Point(1, 1), next.getPlayer1().getWorkers().get(0).getP());
        assertEquals(new Point(2, 2), next.getPlayer2().getWorkers().get(1).getP());
    }
}

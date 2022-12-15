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

public class ApolloStrategyTest extends AbstractTest {
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
        player1.setGod(GodFactory.createGod(GodName.Apollo));
    }

    @Test
    public void testApollo() {
        assertEquals("Apollo", player1.getGod().getName());

        // test moving to ally worker
        assertNull(state.onMove(player1.getWorkers().get(0), new Point(0, 1)));
        // test moving to opponent worker
        Context next = state.onMove(player1.getWorkers().get(0), new Point(1, 0));
        assertFalse(next.getNextAvailActions().contains("buildDome"));
        assertFalse(next.getNextAvailActions().contains("move"));
        assertTrue(next.getNextAvailActions().contains("build"));

        assertEquals(BeforeBuild.class, next.getState().getClass());
        assertEquals(new Point(1, 0), next.getPlayer1().getWorkers().get(0).getP());
        assertEquals(new Point(0, 0), next.getPlayer2().getWorkers().get(0).getP());
    }
}

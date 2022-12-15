package edu.cmu.cs214.Santorini.god.strategy;

import edu.cmu.cs214.Santorini.AbstractTest;
import edu.cmu.cs214.Santorini.game.Context;
import edu.cmu.cs214.Santorini.god.GodFactory;
import edu.cmu.cs214.Santorini.god.GodName;
import edu.cmu.cs214.Santorini.model.Player;
import edu.cmu.cs214.Santorini.model.Point;
import edu.cmu.cs214.Santorini.state.run.BeforeBuild;
import edu.cmu.cs214.Santorini.state.run.BeforeMove;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArtemisStrategyTest extends AbstractTest {
    Context init;
    Player player1;
    Player player2;

    @Before
    public void setUp() {
        super.setUp();
        init = super.createContextAfterInitWorkers();
        player1 = init.getPlayer1();
        player2 = init.getPlayer2();
    }

    @Test
    public void testArtemis() {
        player1.setGod(GodFactory.createGod(GodName.Artemis));
        BeforeMove beforeMove = (BeforeMove) init.getState();
        assertEquals("Artemis", player1.getGod().getName());

        // test move 1
        Context next = beforeMove.onMove(player1.getWorkers().get(1), new Point(0, 2));
        assertEquals(BeforeBuild.class, next.getState().getClass());
        assertTrue(next.getNextAvailActions().contains("move"));
        assertTrue(next.getNextAvailActions().contains("build"));
        assertFalse(next.getNextAvailActions().contains("buildDome"));
        BeforeBuild beforeBuild = (BeforeBuild) next.getState();
        assertEquals(new Point(0, 2), next.getPlayer1().getWorkers().get(1).getP());
        // test move 2 not go back
        assertNull(beforeBuild.onMove(next.getPlayer1().getWorkers().get(1), new Point(0, 1)));
        // test move 2 wrong worker
        assertNull(beforeBuild.onMove(next.getPlayer1().getWorkers().get(0), new Point(0, 3)));
        // test move 2 succeed
        next = beforeBuild.onMove(next.getPlayer1().getWorkers().get(1), new Point(0, 3));
        assertEquals(new Point(0, 3), next.getPlayer1().getWorkers().get(1).getP());
        beforeBuild = (BeforeBuild) next.getState();
        assertNull(beforeBuild.onMove(next.getPlayer1().getWorkers().get(1), new Point(0, 4)));

        assertEquals(BeforeBuild.class, next.getState().getClass());
    }
}

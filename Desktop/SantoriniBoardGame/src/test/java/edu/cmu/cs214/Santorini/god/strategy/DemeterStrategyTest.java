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

public class DemeterStrategyTest extends AbstractTest {
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
        board = state.getContext().getBoard();
    }

    @Test
    public void testDemeter() {
        player1.setGod(GodFactory.createGod(GodName.Demeter));
        assertEquals("Demeter", player1.getGod().getName());

        // test move
        Context next = state.onMove(player1.getWorkers().get(1), new Point(0, 2));
        state = (RunState) next.getState();
        assertEquals(BeforeBuild.class, next.getState().getClass());
        assertEquals(new Point(0, 2), next.getPlayer1().getWorkers().get(1).getP());
        // test build 1
        assertTrue(next.getNextAvailActions().contains("build"));
        assertFalse(next.getNextAvailActions().contains("pass"));
        next = state.onBuild(next.getPlayer1().getWorkers().get(1), new Point(0, 3));
        state = (RunState) next.getState();
        RunState stateForPass = state.copy();
        assertEquals(1, next.getBoard().getCell(0, 3).getHeight());
        // test cannot build on same point
        assertTrue(next.getNextAvailActions().contains("build"));
        assertTrue(next.getNextAvailActions().contains("pass"));
        assertNull(state.onBuild(next.getPlayer1().getWorkers().get(1), new Point(0, 3)));

        // test build twice
        Context nextBuildTwice = state.onBuild(next.getPlayer1().getWorkers().get(1), new Point(1, 2));
        RunState stateBuildTwice = (RunState) next.getState();
        assertEquals(1, next.getBoard().getCell(1, 2).getHeight());
        assertEquals(2, nextBuildTwice.getCurrentPlayer().getId());
    }

    @Test
    public void testDemeterPass(){
        player1.setGod(GodFactory.createGod(GodName.Demeter));
        // test move
        Context next = state.onMove(player1.getWorkers().get(1), new Point(0, 2));
        state = (RunState) next.getState();
        // test build 1
        next = state.onBuild(next.getPlayer1().getWorkers().get(1), new Point(0, 3));
        state = (RunState) next.getState();

        // test pass
        next = state.onPass();
        assertEquals(BeforeMove.class, next.getState().getClass());
        assertEquals(2, next.getCurrentPlayer().getId());
    }
}

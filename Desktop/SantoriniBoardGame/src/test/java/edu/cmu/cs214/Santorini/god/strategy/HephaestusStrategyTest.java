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

public class HephaestusStrategyTest extends AbstractTest {
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
        player1.setGod(GodFactory.createGod(GodName.Hephaestus));
    }

    @Test
    public void testHephaestus() {
        assertEquals("Hephaestus", player1.getGod().getName());

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
        // test cannot build on different point
        assertTrue(next.getNextAvailActions().contains("build"));
        assertTrue(next.getNextAvailActions().contains("pass"));
        assertNull(state.onBuild(next.getPlayer1().getWorkers().get(1), new Point(1, 2)));
        // test cannot use god power to build a dome
        next.getBoard().setCell(0, 3, 3);
        assertNull(state.onBuild(next.getPlayer1().getWorkers().get(1), new Point(0, 3)));

        next.getBoard().setCell(0, 3, 1);
        // test build twice
        next = state.onBuild(next.getPlayer1().getWorkers().get(1), new Point(0, 3));
        assertEquals(2, next.getBoard().getCell(0, 3).getHeight());
        assertEquals(2, next.getCurrentPlayer().getId());
    }

    @Test
    public void testHephaestusPass() {
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

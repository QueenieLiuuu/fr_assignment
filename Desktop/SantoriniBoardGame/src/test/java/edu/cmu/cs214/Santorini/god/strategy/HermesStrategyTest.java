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

public class HermesStrategyTest extends AbstractTest {
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
        player1.setGod(GodFactory.createGod(GodName.Hermes));
    }

    @Test
    public void testHermesWalkThreeTimesAndBuild() {
        assertEquals("Hermes", player1.getGod().getName());

        // test move 1
        Context next = state.onMove(player1.getWorkers().get(1), new Point(0, 2));
        state = (RunState) next.getState();
        assertEquals(BeforeMove.class, next.getState().getClass());
        assertEquals(new Point(0, 2), next.getPlayer1().getWorkers().get(1).getP());
        // test move 2
        next = state.onMove(next.getPlayer1().getWorkers().get(1), new Point(0, 3));
        state = (RunState) next.getState();
        assertEquals(BeforeMove.class, next.getState().getClass());
        assertEquals(new Point(0, 3), next.getPlayer1().getWorkers().get(1).getP());
        // test move 3 up
        next.getBoard().setCell(0, 4, 1);
        next = state.onMove(next.getPlayer1().getWorkers().get(1), new Point(0, 4));
        state = (RunState) next.getState();
        assertEquals(BeforeBuild.class, next.getState().getClass());
        // already moved up, cannot move anymore
        assertNull(state.onMove(next.getPlayer1().getWorkers().get(1), new Point(1, 4)));
        // cannot pass either
        assertNull(state.onPass());
        // can build
        next = state.onBuild(next.getPlayer1().getWorkers().get(1), new Point(0, 3));
        state = (RunState) next.getState();
        assertEquals(1, next.getBoard().getCell(0, 3).getHeight());
        assertEquals(BeforeMove.class, next.getState().getClass());
        assertEquals(2, next.getActivePlayerId());
    }

    @Test
    public void testHermesWalkTwoAndPass() {
        assertEquals("Hermes", player1.getGod().getName());

        // test move 1
        Context next = state.onMove(player1.getWorkers().get(1), new Point(0, 2));
        state = (RunState) next.getState();
        // test move 2
        next = state.onMove(next.getPlayer1().getWorkers().get(1), new Point(0, 3));
        state = (RunState) next.getState();
        // test pass
        next = state.onPass();
        state = (RunState) next.getState();
        assertEquals(0, next.getBoard().getCell(0, 3).getHeight());
        assertEquals(BeforeMove.class, next.getState().getClass());
        assertEquals(2, next.getActivePlayerId());
    }

    @Test
    public void testHermesNoWalkJustBuild() {
        assertEquals("Hermes", player1.getGod().getName());

        // test build
        Context next = state.onBuild(player1.getWorkers().get(1), new Point(0, 2));
        state = (RunState) next.getState();
        assertEquals(1, next.getBoard().getCell(0, 2).getHeight());
        assertEquals(BeforeMove.class, next.getState().getClass());
        assertEquals(2, next.getActivePlayerId());
    }

    @Test
    public void testHermesNoActionJustPass() {
        assertEquals("Hermes", player1.getGod().getName());

        Context next = state.onPass();
        state = (RunState) next.getState();
        assertEquals(BeforeMove.class, next.getState().getClass());
        assertEquals(2, next.getActivePlayerId());
    }
}

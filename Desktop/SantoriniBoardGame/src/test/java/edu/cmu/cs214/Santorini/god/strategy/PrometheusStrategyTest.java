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

public class PrometheusStrategyTest extends AbstractTest {
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
    public void testPrometheusBuildBeforeAndAfter() {
        player1.setGod(GodFactory.createGod(GodName.Prometheus));
        assertEquals("Prometheus", player1.getGod().getName());
        // test build
        Context next = state.onBuild(player1.getWorkers().get(1), new Point(0, 2));
        state = (RunState) next.getState();
        assertEquals(BeforeMove.class, next.getState().getClass());
        assertEquals(1, next.getBoard().getCell(0, 2).getHeight());
        // test cannot move up
        assertNull(state.onMove(player1.getWorkers().get(1), new Point(0, 2)));
        // test move horizontally
        next = state.onMove(next.getPlayer1().getWorkers().get(1), new Point(1, 2));
        state = (RunState) next.getState();
        assertEquals(new Point(1, 2), next.getPlayer1().getWorkers().get(1).getP());
        // test build
        next = state.onBuild(next.getPlayer1().getWorkers().get(1), new Point(0, 2));
        state = (RunState) next.getState();
        assertEquals(BeforeMove.class, next.getState().getClass());
        assertEquals(2, next.getBoard().getCell(0, 2).getHeight());
        assertEquals(2, next.getActivePlayerId());
    }

    @Test
    public void testPrometheusGiveUpBuildAndMoveUp(){
        player1.setGod(GodFactory.createGod(GodName.Prometheus));
        assertEquals("Prometheus", player1.getGod().getName());
        // test move up
        state.getContext().getBoard().setCell(0, 2, 1);
        Context next = state.onMove(player1.getWorkers().get(1), new Point(0, 2));
        state = (RunState) next.getState();
        assertEquals(new Point(0, 2), next.getPlayer1().getWorkers().get(1).getP());
        assertEquals(BeforeBuild.class, next.getState().getClass());
        // test build
        next = state.onBuild(next.getPlayer1().getWorkers().get(1), new Point(1, 2));
        state = (RunState) next.getState();
        assertEquals(BeforeMove.class, next.getState().getClass());
        assertEquals(1, next.getBoard().getCell(1, 2).getHeight());
        assertEquals(2, next.getActivePlayerId());
    }
}

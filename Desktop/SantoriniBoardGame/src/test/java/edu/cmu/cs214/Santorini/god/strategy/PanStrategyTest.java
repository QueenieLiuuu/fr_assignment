package edu.cmu.cs214.Santorini.god.strategy;

import edu.cmu.cs214.Santorini.AbstractTest;
import edu.cmu.cs214.Santorini.game.Context;
import edu.cmu.cs214.Santorini.god.GodFactory;
import edu.cmu.cs214.Santorini.god.GodName;
import edu.cmu.cs214.Santorini.model.Player;
import edu.cmu.cs214.Santorini.model.Point;
import edu.cmu.cs214.Santorini.state.prepare.InitWorker;
import edu.cmu.cs214.Santorini.state.run.BeforeBuild;
import edu.cmu.cs214.Santorini.state.run.BeforeMove;
import edu.cmu.cs214.Santorini.state.run.RunState;
import edu.cmu.cs214.Santorini.state.run.Win;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

public class PanStrategyTest extends AbstractTest {
    Context next;
    RunState state;
    Player player1;
    Player player2;

    @Before
    public void setUp() {
        super.setUp();
        Context context = super.createContextAfterGodChosen();
        InitWorker initWorker = (InitWorker) context.getState();
        board = context.getBoard();
        board.setCell(0, 1, 2);
        board.setCell(0, 2, 0);
        board.setCell(1, 1, 1);
        player1 = context.getPlayer1();
        player2 = context.getPlayer2();
        player1.addWorker(board, new Point(0, 0));
        player1.addWorker(board, new Point(0, 1));
        player1.setGod(GodFactory.createGod(GodName.Pan));
        BeforeMove state = new BeforeMove();
        this.next = new Context(
                state, context.getPlayer1(), context.getPlayer2(),
                1, 0, board, new HashSet<>(), context).copy();
        this.next.getState().setContext(next);
    }

    @Test
    public void testPanDown2Level() {
        assertEquals("Pan", player1.getGod().getName());
        state = (RunState) next.getState();
        Context next = state.onMove(player1.getWorkers().get(1), new Point(0, 2));
        assertEquals(Win.class, next.getState().getClass());
    }

    @Test
    public void testPanDown1Level() {
        assertEquals("Pan", player1.getGod().getName());
        state = (RunState) next.getState();
        Context next = state.onMove(player1.getWorkers().get(1), new Point(1, 1));
        assertEquals(BeforeBuild.class, next.getState().getClass());
    }
}

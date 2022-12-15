package edu.cmu.cs214.Santorini.god.strategy;

import edu.cmu.cs214.Santorini.AbstractTest;
import edu.cmu.cs214.Santorini.game.Context;
import edu.cmu.cs214.Santorini.god.GodFactory;
import edu.cmu.cs214.Santorini.god.GodName;
import edu.cmu.cs214.Santorini.model.Player;
import edu.cmu.cs214.Santorini.model.Point;
import edu.cmu.cs214.Santorini.state.Buff;
import edu.cmu.cs214.Santorini.state.run.BeforeBuild;
import edu.cmu.cs214.Santorini.state.run.BeforeMove;
import edu.cmu.cs214.Santorini.state.run.RunState;
import org.junit.Before;
import org.junit.Test;

import static edu.cmu.cs214.Santorini.state.Buff.BuffType.cannotMoveUp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AthenaStrategyTest extends AbstractTest {
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
        player1.setGod(GodFactory.createGod(GodName.Athena));
    }

    @Test
    public void testAthenaMoveUp() {
        assertEquals("Athena", player1.getGod().getName());
        board.setCell(0, 2, 1);

        // test moving up
        Context next = state.onMove(player1.getWorkers().get(1), new Point(0, 2));
        state = (RunState) next.getState();
        Buff buff = state.getBuffs().get(0);
        assertEquals(2, buff.getToPlayerId());
        assertEquals(1, buff.getLastRound());
        assertEquals(Buff.BuffType.cannotMoveUp, buff.getType());

        next = state.onBuild(player1.getWorkers().get(1), new Point(1, 2));
        state = (RunState) next.getState();
        assertEquals(1, next.getBoard().getCell(1, 2).getHeight());

        // test opponent cannot move up
        assertNull(state.onMove(next.getPlayer2().getWorkers().get(1), new Point(1, 2)));

        // but can move horizontally
        next = state.onMove(next.getPlayer2().getWorkers().get(1), new Point(2, 1));
        state = (RunState) next.getState();
        assertEquals(0, next.getBoard().getCell(2, 1).getHeight());
        next = state.onBuild(next.getPlayer2().getWorkers().get(1), new Point(2, 2));
        state = (RunState) next.getState();
        assertEquals(0, state.getBuffs().size());
    }
}

package edu.cmu.cs214.Santorini.god.strategy;

import edu.cmu.cs214.Santorini.AbstractTest;
import edu.cmu.cs214.Santorini.game.Context;
import edu.cmu.cs214.Santorini.god.GodFactory;
import edu.cmu.cs214.Santorini.god.GodName;
import edu.cmu.cs214.Santorini.model.Player;
import edu.cmu.cs214.Santorini.model.Point;
import edu.cmu.cs214.Santorini.state.run.BeforeMove;
import edu.cmu.cs214.Santorini.state.run.RunState;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AtlasStrategyTest extends AbstractTest {
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
        player1.setGod(GodFactory.createGod(GodName.Atlas));
    }

    @Test
    public void testAtlasMoveUp() {
        assertEquals("Atlas", player1.getGod().getName());
        Context next = state.onMove(player1.getWorkers().get(1), new Point(0, 2));
        state = (RunState) next.getState();

        // buildDome
        next = state.onBuild(player1.getWorkers().get(1), new Point(1, 2), true);
        state = (RunState) next.getState();
        assertTrue( next.getBoard().getCell(1, 2).isDome());
    }
}

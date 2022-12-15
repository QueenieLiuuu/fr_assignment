package edu.cmu.cs214.Santorini.state.prepare;

import edu.cmu.cs214.Santorini.AbstractTest;
import edu.cmu.cs214.Santorini.game.Context;
import org.junit.Test;

import static org.junit.Assert.*;

public class GodChosenTest extends AbstractTest {
    @Test
    public void testChooseGod() {
        Context init = super.createInitContext();
        GodChosen godChosen = (GodChosen) init.getState();

        assertNull(init.getPlayer1().getGod());
        // test wrong godname
        assertNull(godChosen.onChooseGod(init.getPlayer1(), "Feiyang"));
        assertNull(init.getPlayer1().getGod());

        Context next = godChosen.onChooseGod(init.getPlayer1(), "Apollo");
        godChosen = (GodChosen) next.getState();
        assertEquals("Apollo", next.getPlayer1().getGod().getName());
        // player2 not choose yet
        assertEquals(next.getState().getClass(), GodChosen.class);

        // test player1 choose twice
        assertNull(godChosen.onChooseGod(next.getPlayer1(), "Pan"));

        // player2 choose god
        next = godChosen.onChooseGod(next.getPlayer2(), "Pan");
        assertEquals("Pan", next.getPlayer2().getGod().getName());
        assertEquals(next.getState().getClass(), InitWorker.class);
    }
}

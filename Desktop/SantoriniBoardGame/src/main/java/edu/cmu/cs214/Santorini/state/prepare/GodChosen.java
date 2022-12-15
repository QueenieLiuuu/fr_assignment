package edu.cmu.cs214.Santorini.state.prepare;

import edu.cmu.cs214.Santorini.game.Context;
import edu.cmu.cs214.Santorini.god.God;
import edu.cmu.cs214.Santorini.god.GodName;
import edu.cmu.cs214.Santorini.model.Player;
import edu.cmu.cs214.Santorini.god.GodFactory;
import edu.cmu.cs214.Santorini.state.State;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Game just init and need to choose god
 * BeforeState: null
 * NextState: GodChosen/InitWorker
 */
public class GodChosen extends PrepareState {
    public GodChosen() {
    }

    public GodChosen(Context context) {
        super(context);
    }

    /**
     * player choose god based on god name, choose Man if in simple mode
     *
     * @param player  player who chooses god
     * @param godName name of god
     * @return context(GodChosen) if player2 god unset, else return context(InitWorker)
     */
    public Context onChooseGod(Player player, String godName) {
        Context lastContext = context.copy();
        if (player.getGod() != null) {  // already chosen
            return null;
        }
        God god;
        try {
            god = GodFactory.createGod(GodName.valueOf(godName));
        } catch (IllegalArgumentException e) {
            return null;
        }
        if (god == null) {
            return null;
        }
        player.setGod(god);
        int activePlayerId;
        State nextState;
        if (context.getPlayer2().getGod() != null) {
            activePlayerId = 1;
            nextState = new InitWorker();
        } else {
            activePlayerId = 2;
            nextState = new GodChosen();
        }

        Context nextContext = context.generateNext(
                nextState, activePlayerId, 0, new HashSet<>(), lastContext);
        nextContext.getState().setContext(nextContext);
        return nextContext;
    }


    @Override
    public State copy() {
        return new GodChosen(context);
    }
}

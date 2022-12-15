package edu.cmu.cs214.Santorini.state.prepare;

import edu.cmu.cs214.Santorini.game.Context;
import edu.cmu.cs214.Santorini.model.Board;
import edu.cmu.cs214.Santorini.model.Player;
import edu.cmu.cs214.Santorini.model.Point;
import edu.cmu.cs214.Santorini.state.State;
import edu.cmu.cs214.Santorini.state.run.BeforeMove;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Players completed choosing god, now they init workers
 * BeforeState: ChooseGod
 * NextState: InitWorker/BeforeMove
 */
public class InitWorker extends PrepareState {
    public InitWorker(Context context) {
        super(context);
    }

    public InitWorker() {}

    /**
     * player set worker's initial positions on the board
     * @param board board to set worker on
     * @param player player who sets worker
     * @param point the point the worker going to stand on
     * @return context(BeforeMove) if both players set all workers, else context(InitWorker)
     */
    public Context onInitWorker(Board board, Player player, Point point) {
        Context lastContext = context.copy();
        if (player.getWorkers() != null && player.getWorkers().size() >= 2) {
            return null;
        }
        if (!player.addWorker(board, point)) {
            return null;
        }
        State nextState;
        Set<String> nextAvailActions = new HashSet<>();
        int activePlayerId = context.getPlayer1().getWorkers().size() < 2 ? 1 : 2;
        if (context.getPlayer2().getWorkers().size() < 2) {
            nextState = new InitWorker();
        } else {
            activePlayerId = 1;
            nextState = new BeforeMove(true);
            nextAvailActions = context.getCurrentStrategy().getAvailActions(
                    (BeforeMove) nextState, lastContext);
        }
        Context nextContext = context.generateNext(nextState, activePlayerId, 0,
                nextAvailActions, lastContext);
        nextContext.getState().setContext(nextContext);
        return nextContext;
    }

    @Override
    public InitWorker copy() {
        return new InitWorker(context);
    }
}

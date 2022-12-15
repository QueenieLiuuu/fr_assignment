package edu.cmu.cs214.Santorini.god.strategy;

import edu.cmu.cs214.Santorini.model.Board;
import edu.cmu.cs214.Santorini.model.Point;
import edu.cmu.cs214.Santorini.model.Worker;
import edu.cmu.cs214.Santorini.state.Action;
import edu.cmu.cs214.Santorini.state.Buff;
import edu.cmu.cs214.Santorini.state.run.BeforeBuild;
import edu.cmu.cs214.Santorini.state.run.BeforeMove;
import edu.cmu.cs214.Santorini.state.run.RunState;
import edu.cmu.cs214.Santorini.state.run.Win;

import java.util.HashSet;
import java.util.List;

/**
 * Your Turn: If your Worker does not move up,
 * it may build both before and after moving.
 */
public class PrometheusStrategy extends ManStrategy {
    public PrometheusStrategy() {
        buildableStates = new HashSet<>(List.of(BeforeMove.class, BeforeBuild.class));
    }

    @Override
    public RunState build(Board board, Worker worker, Point p, int activeWorkerId,
                          boolean builtDome, RunState runState) {
        if (builtDome && !this.buildActions.contains(Action.buildDome) ||
                !canBuild(board, worker, p, activeWorkerId, runState)) {
            return null;
        }
        List<Buff> buffs = runState.getBuffs();
        boolean isNewTurn = true;
        if (runState.getClass().equals(BeforeMove.class)) {
            // if build before move, then cannot move up
            buffs.add(new Buff(worker.getPlayer().getId(),
                    1, Buff.BuffType.cannotMoveUp));
            isNewTurn = false;
        }
        try {
            board.build(p, builtDome);
        } catch (RuntimeException e) {
            return null;
        }
        if (isNewTurn) {
            buffs = countdownBuff(worker.getPlayer(), buffs);
        }
        BeforeMove nextState = new BeforeMove(buffs, isNewTurn);
        if (nextState.onCheckLose(runState.getContext().getBoard(),
                runState.getContext().getOpponentPlayer())) {
            return new Win(runState.getContext().getCurrentPlayer());
        }
        return nextState;
    }
}

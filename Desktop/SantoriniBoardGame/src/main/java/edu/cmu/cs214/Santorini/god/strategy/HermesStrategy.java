package edu.cmu.cs214.Santorini.god.strategy;

import edu.cmu.cs214.Santorini.model.Board;
import edu.cmu.cs214.Santorini.model.Point;
import edu.cmu.cs214.Santorini.model.Worker;
import edu.cmu.cs214.Santorini.state.Action;
import edu.cmu.cs214.Santorini.state.run.BeforeBuild;
import edu.cmu.cs214.Santorini.state.run.BeforeMove;
import edu.cmu.cs214.Santorini.state.run.RunState;
import edu.cmu.cs214.Santorini.state.run.Win;

import java.util.HashSet;
import java.util.List;

/**
 * Your Turn: If your Workers do not move up or down,
 * they may each move any number of times (even zero), and then either builds.
 */
public class HermesStrategy extends ManStrategy {
    public HermesStrategy() {
        moveActions = new HashSet<>(List.of(Action.move, Action.pass));
        buildableStates = new HashSet<>(List.of(BeforeMove.class, BeforeBuild.class));
        buildActions = new HashSet<>(List.of(Action.build, Action.pass));
    }

    @Override
    public RunState move(Board board, Worker worker, Point p, RunState runState) {
        if (!canMove(board, worker, p, runState, false)) {
            return null;
        }
        if (board.getCell(worker.getP()).getHeight() == board.getCell(p).getHeight()) {
            // moved horizontally
            worker.setP(p);
            return new BeforeMove(runState.getBuffs(), false);
        }
        worker.setP(p);
        // moved up or down, lose privilege
        moveActions.remove(Action.pass);
        buildActions.remove(Action.pass);
        if (isWin(board, worker, runState)) {
            return new Win(worker.getPlayer());
        }
        return new BeforeBuild(runState.getBuffs());
    }

    @Override
    public RunState pass(RunState runState) {
        if(!this.buildActions.contains(Action.pass)) {
            return null;
        }
        return new BeforeMove(countdownBuff(
                runState.getContext().getCurrentPlayer(), runState.getBuffs()), true);
    }
}

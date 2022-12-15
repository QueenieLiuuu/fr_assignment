package edu.cmu.cs214.Santorini.god.strategy;

import edu.cmu.cs214.Santorini.model.Board;
import edu.cmu.cs214.Santorini.model.Point;
import edu.cmu.cs214.Santorini.model.Worker;
import edu.cmu.cs214.Santorini.state.Buff;
import edu.cmu.cs214.Santorini.state.run.BeforeBuild;
import edu.cmu.cs214.Santorini.state.run.RunState;
import edu.cmu.cs214.Santorini.state.run.Win;

import java.util.ArrayList;
import java.util.List;

/*
 * During opponent’s turn: If one of your Workers moved up on your last turn,
 * opponent Workers cannot move up this turn.
 */
public class AthenaStrategy extends ManStrategy {
    @Override
    public RunState move(Board board, Worker worker, Point p, RunState runState) {
        if (!canMove(board, worker, p, runState, false)) {
            return null;
        }
        List<Buff> buffs = runState.getBuffs();
        if (board.getCell(worker.getP()).getHeight() < board.getCell(p).getHeight()) {
            buffs.add(new Buff(runState.getContext().getOpponentPlayer().getId(),
                    1, Buff.BuffType.cannotMoveUp));
        }
        worker.setP(p);
        if (isWin(board, worker, runState)) {
            return new Win(worker.getPlayer());
        }
        return new BeforeBuild(buffs);
    }
}

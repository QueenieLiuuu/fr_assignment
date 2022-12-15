package edu.cmu.cs214.Santorini.god.strategy;

import edu.cmu.cs214.Santorini.model.Board;
import edu.cmu.cs214.Santorini.model.Point;
import edu.cmu.cs214.Santorini.model.Worker;
import edu.cmu.cs214.Santorini.state.run.BeforeBuild;
import edu.cmu.cs214.Santorini.state.run.RunState;
import edu.cmu.cs214.Santorini.state.run.Win;

/*
 * Your Worker may move into an opponent Worker’s space,
 * if their Worker can be forced one space straight backwards
 * to an unoccupied space at any level.
 */
public class MinotaurStrategy extends ManStrategy {
    @Override
    public RunState move(Board board, Worker worker, Point p, RunState runState) {
        if (!canMove(board, worker, p, runState, true)) {
            return null;
        }
        Worker workerToDash = board.getWorker(p);
        if (workerToDash != null && workerToDash.getPlayer().getId() != worker.getPlayer().getId()) {
            // if there is a worker and not ally, we can dash
            Point targetPoint = board.dashToPosition(worker.getP(), workerToDash.getP());
            if (targetPoint == null) {
                return null;  // invalid dash position
            }
            workerToDash.setP(targetPoint);
        }
        worker.setP(p);
        if (isWin(board, worker, runState)) {
            return new Win(worker.getPlayer());
        }
        return new BeforeBuild(runState.getBuffs());
    }
}

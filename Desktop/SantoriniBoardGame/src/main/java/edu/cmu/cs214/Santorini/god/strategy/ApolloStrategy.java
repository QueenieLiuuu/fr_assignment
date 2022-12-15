package edu.cmu.cs214.Santorini.god.strategy;

import edu.cmu.cs214.Santorini.model.Board;
import edu.cmu.cs214.Santorini.model.Point;
import edu.cmu.cs214.Santorini.model.Worker;
import edu.cmu.cs214.Santorini.state.run.BeforeBuild;
import edu.cmu.cs214.Santorini.state.run.RunState;
import edu.cmu.cs214.Santorini.state.run.Win;

/*
 * Your Move: Your Worker may move into an opponent Worker's space by forcing their Worker to the space yours just vacated.
 */
public class ApolloStrategy extends ManStrategy {
    @Override
    public RunState move(Board board, Worker worker, Point p, RunState runState) {
        if (!canMove(board, worker, p, runState, true)) {
            return null;
        }
        Worker workerToSwitch = board.getWorker(p);
        if (workerToSwitch != null) {
            workerToSwitch.setP(worker.getP());
        }
        worker.setP(p);
        if (isWin(board, worker, runState)) {
            return new Win(worker.getPlayer());
        }
        return new BeforeBuild(runState.getBuffs());
    }
}

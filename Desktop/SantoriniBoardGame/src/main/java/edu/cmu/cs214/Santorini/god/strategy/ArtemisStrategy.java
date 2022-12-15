package edu.cmu.cs214.Santorini.god.strategy;

import edu.cmu.cs214.Santorini.game.Context;
import edu.cmu.cs214.Santorini.model.Board;
import edu.cmu.cs214.Santorini.model.Point;
import edu.cmu.cs214.Santorini.model.Worker;
import edu.cmu.cs214.Santorini.state.run.BeforeBuild;
import edu.cmu.cs214.Santorini.state.run.BeforeMove;
import edu.cmu.cs214.Santorini.state.run.RunState;
import edu.cmu.cs214.Santorini.state.run.Win;

import java.util.HashSet;
import java.util.List;

/*
 * Your Move: Your Worker may move into an opponent Worker's space by forcing their Worker to the space yours just vacated.
 */
public class ArtemisStrategy extends ManStrategy {
    public ArtemisStrategy() {
        movableStates = new HashSet<>(List.of(BeforeMove.class, BeforeBuild.class));
    }

    @Override
    public RunState move(Board board, Worker worker, Point p, RunState runState) {
        if (!canMove(board, worker, p, runState)) {
            return null;
        }
        Context lastContext = runState.getContext().getLastContext();
        if (runState.getClass() == BeforeBuild.class) { //
            // second move
            if (lastContext.getState().getClass() != BeforeMove.class) {
                return null; // cannot move forever
            }
            Worker lastActiveWorker = Worker.getWorkerById(
                    lastContext.getPlayer1(),
                    lastContext.getPlayer2(),
                    worker.getId());
            if (p.equals(lastActiveWorker.getP())) {
                return null;  // cannot go back to last moving place
            }
        }
        worker.setP(p);
        if (isWin(board, worker, runState)) {
            return new Win(worker.getPlayer());
        }
        return new BeforeBuild(runState.getBuffs());
    }
}

package edu.cmu.cs214.Santorini.god.strategy;

import edu.cmu.cs214.Santorini.game.Context;
import edu.cmu.cs214.Santorini.model.Board;
import edu.cmu.cs214.Santorini.model.Worker;
import edu.cmu.cs214.Santorini.state.run.RunState;

/**
 * You also win if your Worker moves down two or more levels.
 */
public class PanStrategy extends ManStrategy {
    @Override
    public boolean isWin(Board board, Worker w, RunState runState) {
        if (super.isWin(board, w, runState)) {
            return true;
        }
        Context lastContext = runState.getContext().getLastContext();
        Worker lastActiveWorker = Worker.getWorkerById(
                lastContext.getPlayer1(),
                lastContext.getPlayer2(),
                w.getId());
        if (lastActiveWorker == null) {
            return false;
        }
        int previousHeight = lastContext.getBoard().getCell(lastActiveWorker.getP()).getHeight();
        int currentHeight = runState.getContext().getBoard().getCell(w.getP()).getHeight();
        return previousHeight - currentHeight >= 2;
    }
}

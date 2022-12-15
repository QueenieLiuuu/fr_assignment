package edu.cmu.cs214.Santorini.god.strategy;

import edu.cmu.cs214.Santorini.game.Context;
import edu.cmu.cs214.Santorini.model.Board;
import edu.cmu.cs214.Santorini.model.Point;
import edu.cmu.cs214.Santorini.model.Worker;
import edu.cmu.cs214.Santorini.state.Action;
import edu.cmu.cs214.Santorini.state.run.BeforeBuild;
import edu.cmu.cs214.Santorini.state.run.BeforeMove;
import edu.cmu.cs214.Santorini.state.run.RunState;
import edu.cmu.cs214.Santorini.state.run.Win;


/**
 * Your Build: Your Worker may build one additional block (not dome) on top of your first block.
 */
public class HephaestusStrategy extends DemeterStrategy {
    @Override
    public RunState build(Board board, Worker worker, Point p, int activeWorkerId,
                          boolean builtDome, RunState runState) {
        if (builtDome && !this.buildActions.contains(Action.buildDome) ||
                !canBuild(board, worker, p, activeWorkerId, runState)) {
            return null;
        }
        boolean is2ndBuild =
                BeforeBuild.class == runState.getContext().getLastContext().getState().getClass();
        Board lastBoard = runState.getContext().getLastContext().getBoard();
        Point diffPoint = Board.findBoardDiff(lastBoard, board);
        if (is2ndBuild && (diffPoint == null || !diffPoint.equals(p) ||
                board.getCell(p).isHighestWithoutDome() || board.getCell(p).isHighestWithoutDome())) {
            return null;  // cannot build on different place, cannot build a dome
        }
        try {
            board.build(p, builtDome);
        } catch (RuntimeException e) {
            return null;
        }
        if (is2ndBuild) {
            // switch player
            BeforeMove nextState = new BeforeMove(countdownBuff(
                    runState.getContext().getCurrentPlayer(), runState.getBuffs()));
            nextState.setNewTurn(true);
            if (nextState.onCheckLose(runState.getContext().getBoard(),
                    runState.getContext().getOpponentPlayer())) {
                return new Win(runState.getContext().getCurrentPlayer());
            }
            return nextState;
        }
        // build again
        return new BeforeBuild(runState.getBuffs());
    }
}

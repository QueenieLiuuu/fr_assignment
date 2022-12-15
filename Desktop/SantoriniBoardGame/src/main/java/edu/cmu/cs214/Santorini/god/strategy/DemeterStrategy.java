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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Your Worker may build one additional time, but not on the same space.
 * (UI hint: You will likely need a way to indicate that the player wants to
 * skip the optional second build, e.g., with a "pass" button or by clicking
 * on the worker's current location)
 */
public class DemeterStrategy extends ManStrategy {
    public DemeterStrategy() {
        buildableStates = new HashSet<>(List.of(BeforeMove.class, BeforeBuild.class));
        buildActions = new HashSet<>(List.of(Action.build, Action.pass));
    }

    @Override
    public RunState build(Board board, Worker worker, Point p, int activeWorkerId,
                          boolean builtDome, RunState runState) {
        if (builtDome && !this.buildActions.contains(Action.buildDome) ||
                !canBuild(board, worker, p, activeWorkerId, runState)) {
            return null;
        }
        Board lastBoard = runState.getContext().getLastContext().getBoard();
        Point diffPoint = Board.findBoardDiff(lastBoard, board);
        if (diffPoint != null && diffPoint.equals(p)) {
            return null;  // cannot build on same place
        }
        try {
            board.build(p, builtDome);
        } catch (RuntimeException e) {
            return null;
        }
        if (BeforeBuild.class == runState.getContext().getLastContext().getState().getClass()) {
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

    @Override
    public RunState pass(RunState runState) {
        Context lastContext = runState.getContext().getLastContext();
        if (!(runState.getClass() == BeforeBuild.class &&
                lastContext.getState().getClass() == BeforeBuild.class)) {
            return null;
        }
        return new BeforeMove(countdownBuff(
                runState.getContext().getCurrentPlayer(), runState.getBuffs()), true);
    }

    @Override
    public Set<String> getAvailActions(RunState runState, Context lastContext) {
        Set<String> nextAvailActions = new HashSet<>();
        if (movableStates.contains(runState.getClass())) {
            nextAvailActions.addAll(moveActions.stream().map(Enum::name).toList());
        }
        if (buildableStates.contains(runState.getClass())) {
            nextAvailActions.add(Action.build.name());
            if (BeforeBuild.class == lastContext.getState().getClass() &&
                    BeforeBuild.class == runState.getClass()) {
                nextAvailActions.add(Action.pass.name());
            }
        }
        return nextAvailActions;
    }

}

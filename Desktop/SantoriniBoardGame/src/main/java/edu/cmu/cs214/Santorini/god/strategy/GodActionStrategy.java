package edu.cmu.cs214.Santorini.god.strategy;

import edu.cmu.cs214.Santorini.game.Context;
import edu.cmu.cs214.Santorini.model.Board;
import edu.cmu.cs214.Santorini.model.Player;
import edu.cmu.cs214.Santorini.model.Point;
import edu.cmu.cs214.Santorini.model.Worker;
import edu.cmu.cs214.Santorini.state.Buff;
import edu.cmu.cs214.Santorini.state.run.RunState;

import java.util.List;
import java.util.Set;

/**
 * Using Strategy Pattern to implement god powers.
 * Different god powers interact at different game stages
 * In each implementation, only parts of the methods need to be override
 */
public interface GodActionStrategy {
    boolean canMove(Board board, Worker worker, Point p, RunState runState);

    boolean canMove(Board board, Worker worker, Point p, RunState runState, boolean onOpponent);

    boolean canBuild(Board board, Worker worker, Point p, int activeWorkerId,
                     RunState runState);

    RunState build(Board board, Worker worker, Point p, int activeWorkerId, boolean builtDome,
                   RunState runState);

    RunState move(Board board, Worker worker, Point p, RunState runState);

    RunState pass(RunState runState);

    boolean isWin(Board board, Worker worker, RunState runState);

    List<Buff> countdownBuff(Player player, List<Buff> buffs);

    Set<String> getAvailActions(RunState runState, Context lastContext);
}

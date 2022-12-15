package edu.cmu.cs214.Santorini.god.strategy;

import edu.cmu.cs214.Santorini.game.Context;
import edu.cmu.cs214.Santorini.model.Board;
import edu.cmu.cs214.Santorini.model.Player;
import edu.cmu.cs214.Santorini.model.Point;
import edu.cmu.cs214.Santorini.model.Worker;
import edu.cmu.cs214.Santorini.state.Action;
import edu.cmu.cs214.Santorini.state.run.*;
import edu.cmu.cs214.Santorini.state.Buff;

import java.util.*;

/**
 * Just an ordinary man, has nothing but luck.
 */
public class ManStrategy implements GodActionStrategy {
    Set<Class<? extends RunState>> movableStates = new HashSet<>(List.of(BeforeMove.class));
    Set<Action> moveActions = new HashSet<>(List.of(Action.move));
    Set<Class<? extends RunState>> buildableStates = new HashSet<>(List.of(BeforeBuild.class));
    Set<Action> buildActions = new HashSet<>(List.of(Action.build));

    @Override
    public boolean canMove(Board board, Worker worker, Point p, RunState runState,
                           boolean onOpponent) {
        if (!movableStates.contains(runState.getClass())) {
            return false;  // cannot move at current state
        }
        if (!board.getWorkers().contains(worker)) {
            return false;  // invalid worker
        }
        var movablePoints = board.getPoints(worker, true, onOpponent);
        boolean can = movablePoints.contains(p);
        Buff cannotMoveUpBuff = runState.getBuffs().stream()
                .filter(b -> b.getType().equals(Buff.BuffType.cannotMoveUp))
                .findFirst().orElse(null);
        if (cannotMoveUpBuff != null && cannotMoveUpBuff.getToPlayerId() == runState.getContext().getActivePlayerId()) {
            Point currentP = worker.getP();
            can = can && board.getCell(p).getHeight() <= board.getCell(currentP).getHeight();
        }
        return can;
    }

    @Override
    public boolean canMove(Board board, Worker worker, Point p, RunState runState) {
        return this.canMove(board, worker, p, runState, false);
    }

    @Override
    public RunState move(Board board, Worker worker, Point p, RunState runState) {
        if (!canMove(board, worker, p, runState, false)) {
            return null;
        }
        worker.setP(p);
        if (isWin(board, worker, runState)) {
            return new Win(worker.getPlayer());
        }
        return new BeforeBuild(runState.getBuffs());
    }

    @Override
    public RunState pass(RunState runState) {
        return null;
    }

    @Override
    public boolean isWin(Board board, Worker w, RunState runState) {
        return board.getCell(w.getP()).isHighestWithoutDome();
    }

    @Override
    public Set<String> getAvailActions(RunState runState, Context lastContext) {
        Set<String> nextAvailActions = new HashSet<>();
        if (movableStates.contains(runState.getClass())) {
            nextAvailActions.addAll(moveActions.stream().map(Enum::name).toList());
        }
        if (buildableStates.contains(runState.getClass())) {
            nextAvailActions.addAll(buildActions.stream().map(Enum::name).toList());
            System.out.println(nextAvailActions);
        }
        return nextAvailActions;
    }

    @Override
    public boolean canBuild(Board board, Worker worker, Point p, int activeWorkerId,
                            RunState runState) {
        if (!buildableStates.contains(runState.getClass())) {
            return false;  // cannot build at current state
        }
        if (!board.getWorkers().contains(worker) || activeWorkerId > 0 && activeWorkerId != worker.getId()) {
            return false;
        }
        var buildablePoints = board.getPoints(worker, false);
        return buildablePoints.contains(p);
    }

    @Override
    public RunState build(Board board, Worker worker, Point p, int activeWorkerId,
                          boolean builtDome, RunState runState) {
        if (builtDome && !this.buildActions.contains(Action.buildDome) ||
                !canBuild(board, worker, p, activeWorkerId, runState)) {
            return null;
        }
        try {
            board.build(p, builtDome);
        } catch (RuntimeException e) {
            return null;
        }

        BeforeMove nextState = new BeforeMove(
                countdownBuff(runState.getContext().getCurrentPlayer(), runState.getBuffs()), true);
        if (nextState.onCheckLose(runState.getContext().getBoard(),
                runState.getContext().getOpponentPlayer())) {
            return new Win(runState.getContext().getCurrentPlayer());
        }
        return nextState;
    }

    @Override
    public List<Buff> countdownBuff(Player player, List<Buff> buffs) {
        List<Buff> ret = new ArrayList<>();
        for (var buff : buffs) {
            if ((buff.getToPlayerId() == player.getId()) && buff.getLastRound() >= 1) {
                buff.setLastRound(buff.getLastRound() - 1);
            }
            if (buff.getLastRound() > 0) {
                ret.add(buff);
            }
        }
        return ret;
    }
}
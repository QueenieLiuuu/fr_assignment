package edu.cmu.cs214.Santorini.state.run;

import edu.cmu.cs214.Santorini.game.Context;
import edu.cmu.cs214.Santorini.model.Board;
import edu.cmu.cs214.Santorini.model.Player;
import edu.cmu.cs214.Santorini.model.Worker;
import edu.cmu.cs214.Santorini.state.Buff;

import java.util.ArrayList;
import java.util.List;

/**
 * Players inited all workers, now worker can move
 * BeforeState: InitWorker
 * NextState: BeforeMove(some god powers)/Win/BeforeBuild
 */
public class BeforeMove extends RunState {
    private boolean isNewTurn;

    public BeforeMove(List<Buff> buffs, boolean isNewTurn) {
        super(buffs);
        this.isNewTurn = isNewTurn;
    }

    public boolean isNewTurn() {
        return isNewTurn;
    }

    public void setNewTurn(boolean newTurn) {
        isNewTurn = newTurn;
    }

    public BeforeMove() {
        this.isNewTurn = true;
    }

    public BeforeMove(boolean isNewTurn) {
        this.isNewTurn = isNewTurn;
    }

    public BeforeMove(Context context, List<Buff> buffs, boolean isNewTurn) {
        super(context, buffs);
        this.isNewTurn = isNewTurn;
    }

    public BeforeMove(List<Buff> buffs) {
        super(buffs);
    }

    public BeforeMove(Context context, List<Buff> buffs) {
        super(context, buffs);
    }

    /**
     * check if player loses because all workers cannot move
     *
     * @param board  board
     * @param player player
     * @return whether all workers of player cannot move and lose
     */
    public boolean onCheckLose(Board board, Player player) {
        for (Worker worker : player.getWorkers()) {
            if (board.getPoints(worker, true).size() != 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public BeforeMove copy() {
        return new BeforeMove(context, new ArrayList<>(buffs), this.isNewTurn);
    }
}

package edu.cmu.cs214.Santorini.state.run;

import edu.cmu.cs214.Santorini.game.Context;
import edu.cmu.cs214.Santorini.god.strategy.GodActionStrategy;
import edu.cmu.cs214.Santorini.model.Point;
import edu.cmu.cs214.Santorini.model.Worker;
import edu.cmu.cs214.Santorini.state.Buff;
import edu.cmu.cs214.Santorini.state.State;

import java.util.ArrayList;
import java.util.List;

/**
 * (PrepareState)          -> (RunState loops)
 * (GodChosen->InitWorker) -> (BeforeMove->BeforeBuild) -> (nextPlayer)(BeforeMove->...)
 */
public abstract class RunState extends State {
    List<Buff> buffs;

    public RunState(List<Buff> buffs) {
        this.buffs = buffs == null ? new ArrayList<>() : buffs;
    }

    public RunState() {
        this.buffs = new ArrayList<>();
    }

    public RunState(Context context, List<Buff> buffs) {
        super(context);
        this.buffs = buffs;
    }

    public List<Buff> getBuffs() {
        return buffs;
    }

    /**
     * worker move to point
     *
     * @param w worker who moves
     * @param p point the worker will move
     * @return next context
     */
    public Context onMove(Worker w, Point p) {
        Context lastContext = context.copy();
        GodActionStrategy strategy = this.context.getCurrentStrategy();
        RunState state = strategy.move(
                context.getBoard(), w, p, this);
        if (state == null) {
            return null;
        }
        Context next = context.generateNext(state, context.getActivePlayerId(), w.getId(),
                strategy.getAvailActions(state, lastContext), lastContext);
        next.getState().setContext(next);
        return next;
    }

    /**
     * onBuild with default buildDome=false
     */
    public Context onBuild(Worker w, Point p) {
        return onBuild(w, p, false);
    }

    /**
     * worker build on point p
     * Note: buildDome only used for some god build specifically a dome on any level
     * if you just build on level 3, you can build a dome without setting buildDome
     *
     * @param w         the worker who builds
     * @param p         the point to build on
     * @param buildDome for god power which can build dome on any level
     * @return next context
     */
    public Context onBuild(Worker w, Point p, boolean buildDome) {
        Context lastContext = context.copy();
        GodActionStrategy strategy = this.context.getCurrentStrategy();
        RunState state = this.context.getCurrentStrategy().build(
                context.getBoard(), w, p, context.getActiveWorkerId(), buildDome, this);
        if (state == null) {
            return null;
        }
        Context next;
        if (state instanceof BeforeMove && ((BeforeMove) state).isNewTurn()) {  // switch player
            next = context.generateNext(state, true, strategy.getAvailActions(state, lastContext),
                    lastContext);
        } else {
            next = context.generateNext(state, strategy.getAvailActions(state, lastContext), lastContext);
        }
        next.getState().setContext(next);
        return next;
    }

    /**
     * worker pass his turn
     * Note: only available in some god powers
     *
     * @return next context
     */
    public Context onPass() {
        Context lastContext = context.copy();
        GodActionStrategy strategy = this.context.getCurrentStrategy();
        RunState state = this.context.getCurrentStrategy().pass(this);
        if (state == null) {
            return null;
        }
        Context next = context.generateNext(
                state, true, strategy.getAvailActions(state, lastContext), lastContext);
        next.getState().setContext(next);
        return next;
    }

    public abstract RunState copy();
}

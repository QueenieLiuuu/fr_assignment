package edu.cmu.cs214.Santorini.game;

import edu.cmu.cs214.Santorini.model.Board;
import edu.cmu.cs214.Santorini.model.Player;
import edu.cmu.cs214.Santorini.model.Worker;
import edu.cmu.cs214.Santorini.state.State;
import edu.cmu.cs214.Santorini.god.strategy.GodActionStrategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Game context, has one2one relation to state
 * Each timeframe has a context with all the information of current stage
 * Half-Immutable (some getter doesn't return copy for unittest convenience)
 */
public final class Context {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);
    final private int id;
    final private State state;
    final private Player player1;
    final private Player player2;
    final private int activePlayerId;
    final private int activeWorkerId;
    final private Board board;
    final private Set<String> nextAvailActions;
    final private Context lastContext;

    public Context(State state, Player player1, Player player2, int activePlayerId, int activeWorkerId, Board board, Set<String> nextAvailActions, Context lastContext) {
        this.activePlayerId = activePlayerId;
        this.activeWorkerId = activeWorkerId;
        this.lastContext = lastContext;
        this.id = ID_GENERATOR.getAndIncrement();
        this.state = state;
        this.player1 = player1;
        this.player2 = player2;
        this.board = board;
        this.nextAvailActions = nextAvailActions;
    }

    public Context(int id, State state, Player player1, Player player2, int activePlayerId,
                   int activeWorkerId, Board board, Set<String> nextAvailActions, Context lastContext) {
        this.id = id;
        this.activePlayerId = activePlayerId;
        this.activeWorkerId = activeWorkerId;
        this.lastContext = lastContext;
        this.state = state;
        this.player1 = player1;
        this.player2 = player2;
        this.board = board;
        this.nextAvailActions = nextAvailActions;
    }

    public State getState() {
        return state;
    }

    /**
     * Get current player's god strategy for moving/building etc
     *
     * @return current strategy
     */
    public GodActionStrategy getCurrentStrategy() {
        return getCurrentPlayer().getGod().getStrategy();
    }

    public Set<String> getNextAvailActions() {
        return nextAvailActions;
    }

    public Player getCurrentPlayer() {
        if (activePlayerId == 1) {
            return player1;
        } else if (activePlayerId == 2) {
            return player2;
        } else {
            return null;
        }
    }

    public Player getOpponentPlayer() {
        if (activePlayerId == 1) {
            return player2;
        } else if (activePlayerId == 2) {
            return player1;
        } else {
            return null;
        }
    }

    public int getActivePlayerId() {
        return activePlayerId;
    }

    public int getActiveWorkerId() {
        return activeWorkerId;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Board getBoard() {
        return board;
    }

    public Context getLastContext() {
        return lastContext;
    }

    /**
     * Get next Context with next state
     * All params remain for next Context, e.g. BeforeBuild->AfterBuilt
     *
     * @param state next state
     * @return next Context
     */
    public Context generateNext(State state, Set<String> nextAvailActions, Context lastContext) {
        return generateNext(state, activePlayerId, activeWorkerId, nextAvailActions, lastContext);
    }

    public Context generateNext(State state, boolean switchPlayer, Set<String> nextAvailActions,
                                Context lastContext) {
        int nextActivePlayer = switchPlayer ? 3 - activePlayerId : activePlayerId;
        int nextActiveWorker = switchPlayer ? 0 : activeWorkerId;
        return generateNext(state, nextActivePlayer, nextActiveWorker, nextAvailActions, lastContext);
    }

    public Context generateNext(State state, int activePlayerId, int activeWorkerId,
                                Set<String> nextAvailActions, Context lastContext) {
        Context next = new Context(state, player1, player2, activePlayerId, activeWorkerId,
                board, nextAvailActions, lastContext).copy();
        next.getState().setContext(next);
        return next;
    }

    public ContextDto toDto() {
        return this.toDto(false);
    }

    public ContextDto toDto(boolean forLastContext) {
        ContextDto lastContextDto = null;
        if (this.lastContext != null) {
            lastContextDto = forLastContext ? null : this.lastContext.toDto();
        }
        return new ContextDto(
                this.id,
                this.state.getClass().getSimpleName(),
                this.player1.toDto(),
                this.player2.toDto(),
                this.activePlayerId,
                this.activeWorkerId,
                this.board.to2DHeightList(),
                this.nextAvailActions,
                lastContextDto
        );
    }

    public Context copy() {
        return copy(false);
    }

    public Context copy(boolean forLastContext) {
        Context lastContext = null;
        if (this.lastContext != null) {
            lastContext = forLastContext ? null : this.lastContext.copy();
        }
        Player copyPlayer1 = this.player1.copy();
        Player copyPlayer2 = this.player2.copy();
        ArrayList<Worker> copyWorkers = new ArrayList<>(copyPlayer1.getWorkers());
        copyWorkers.addAll(copyPlayer2.getWorkers());
        return new Context(
                this.id,
                this.state.copy(),
                copyPlayer1,
                copyPlayer2,
                this.activePlayerId,
                this.activeWorkerId,
                this.board.copy(copyWorkers),
                new HashSet<>(this.nextAvailActions),
                lastContext
        );
    }


    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Context{" +
                "id=" + id +
                ", state=" + state.getClass().getSimpleName() +
                ", player1=" + player1 +
                ", player2=" + player2 +
                ", activePlayerId=" + activePlayerId +
                ", activeWorkerId=" + activeWorkerId +
                ", board=" + board +
                ", nextAvailActions=" + nextAvailActions +
                ", lastContext=" + (lastContext != null ? lastContext.getId() : 0) +
                '}';
    }
}
package edu.cmu.cs214.Santorini.game;

import edu.cmu.cs214.Santorini.model.Board;
import edu.cmu.cs214.Santorini.model.Player;
import edu.cmu.cs214.Santorini.model.Worker;
import edu.cmu.cs214.Santorini.state.State;
import edu.cmu.cs214.Santorini.state.prepare.GodChosen;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Game core class, forever running. Store contexts (in memory) and currentContext
 */
@Service
@Scope("application")
public class Game {
    private Integer currentContextId;

    public List<Context> getContexts() {
        return contexts;
    }

    public void addContext(Context context) {
        this.contexts.add(context);
    }

    public void addContextAndRemoveFollowing(Context context) {
        if (this.contexts.size() > 0 &&
                this.contexts.get(this.contexts.size() - 1).getId() < context.getId()) {
            this.addContext(context);
            return;
        }
        List<Context> contexts = new ArrayList<>();
        for (var c : this.contexts) {
            if (c.getId() < context.getId()) {
                contexts.add(c);
            }
        }
        contexts.add(context);
        this.contexts = contexts;
    }

    private List<Context> contexts = new ArrayList<>();

    public Context getCurrentContext() {
        if (this.currentContextId == null) {
            return null;
        }
        for (var c : this.contexts) {
            if (c.getId() == this.currentContextId) {
                return c;
            }
        }
        return null;
    }

    public Context createFirstContext() {
        assert this.contexts.size() == 0;
        Board board = new Board();
        Player p1 = new Player(1);
        Player p2 = new Player(2);
        State state = new GodChosen();
        Context context = new Context(
                state, p1, p2, 1, 0, board, new HashSet<>(), null);
        context.getState().setContext(context);
        contexts.add(context);
        currentContextId = context.getId();
        return context;
    }

    public Worker getWorkerById(int id, boolean ensureActive) {
        Player player1 = getCurrentContext().getPlayer1();
        Player player2 = getCurrentContext().getPlayer2();
        int activePlayerId = this.getCurrentContext().getActivePlayerId();
        Worker worker = Worker.getWorkerById(player1, player2, id);
        if (!ensureActive || id == activePlayerId * 2 || id == activePlayerId * 2 - 1) {
            return worker;
        }
        return null;
    }

    public void reset() {
        this.contexts = new ArrayList<>();
        this.currentContextId = null;
    }

    public Game() {
        this.currentContextId = null;  // null when the game is not running
    }

    public Integer getCurrentContextId() {
        return currentContextId;
    }

    public void setCurrentContextId(Integer currentContextId) {
        this.currentContextId = currentContextId;
    }
}
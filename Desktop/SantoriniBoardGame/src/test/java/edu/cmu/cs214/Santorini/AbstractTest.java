package edu.cmu.cs214.Santorini;

import edu.cmu.cs214.Santorini.game.Context;
import edu.cmu.cs214.Santorini.god.GodFactory;
import edu.cmu.cs214.Santorini.god.GodName;
import edu.cmu.cs214.Santorini.model.Board;
import edu.cmu.cs214.Santorini.model.Player;
import edu.cmu.cs214.Santorini.model.Point;
import edu.cmu.cs214.Santorini.model.Worker;
import edu.cmu.cs214.Santorini.state.prepare.GodChosen;
import edu.cmu.cs214.Santorini.state.prepare.InitWorker;
import edu.cmu.cs214.Santorini.state.run.BeforeBuild;
import edu.cmu.cs214.Santorini.state.run.BeforeMove;
import org.junit.Before;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class AbstractTest {
    protected Board board;
    protected Player player1;
    protected Player player2;
    protected Worker worker1;
    protected Worker worker2;
    protected Worker worker3;
    protected Worker worker4;


    @Before
    public void setUp() {
        player1 = new Player(1);
        player2 = new Player(2);
        board = new Board();
    }

    public Context createInitContext() {
        GodChosen state = new GodChosen();
        Context context = new Context(
                state, new Player(1), new Player(2), 1, 0, new Board(), new HashSet<>(), null).copy();
        context.getState().setContext(context);
        return context;
    }

    public Context createContextAfterGodChosen() {
        Context cur = createInitContext();
        InitWorker state = new InitWorker();
        cur.getPlayer1().setGod(GodFactory.createGod(GodName.Man));
        cur.getPlayer2().setGod(GodFactory.createGod(GodName.Man));
        Context next = new Context(
                state, cur.getPlayer1(), cur.getPlayer2(), 1, 0, cur.getBoard(), new HashSet<>(), cur).copy();
        next.getState().setContext(next);
        return next;
    }

    public Context createContextAfterInitWorkers() {
        Context cur = createContextAfterGodChosen();
        cur.getPlayer1().addWorker(cur.getBoard(), new Point(0, 0));
        cur.getPlayer1().addWorker(cur.getBoard(), new Point(0, 1));
        cur.getPlayer2().addWorker(cur.getBoard(), new Point(1, 0));
        cur.getPlayer2().addWorker(cur.getBoard(), new Point(1, 1));
        BeforeMove state = new BeforeMove();
        Context next = new Context(
                state, cur.getPlayer1(), cur.getPlayer2(), 1, 0, cur.getBoard(), new HashSet<>(), cur).copy();
        next.getState().setContext(next);
        return next;
    }

    public Context createContextBeforeBuild() {
        Context cur = createContextAfterInitWorkers();
        BeforeBuild state = new BeforeBuild();
        cur.getPlayer1().getWorkers().get(1).setP(new Point(0, 2));
        Context next = new Context(
                state, cur.getPlayer1(), cur.getPlayer2(), 1, 2, cur.getBoard(), new HashSet<>(), cur).copy();
        next.getState().setContext(next);
        return next;
    }
}
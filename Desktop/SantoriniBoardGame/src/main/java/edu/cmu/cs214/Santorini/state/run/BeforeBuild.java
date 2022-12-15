package edu.cmu.cs214.Santorini.state.run;

import edu.cmu.cs214.Santorini.game.Context;
import edu.cmu.cs214.Santorini.god.strategy.GodActionStrategy;
import edu.cmu.cs214.Santorini.state.Buff;
import edu.cmu.cs214.Santorini.state.State;

import java.util.ArrayList;
import java.util.List;

/**
 * A worker just moved, now worker can build (or other actions)
 * BeforeState: BeforeMove/others
 * NextState: BeforeMove(some god powers)/Win
 */
public class BeforeBuild extends RunState {
    public BeforeBuild() {
        super();
    }

    public BeforeBuild(List<Buff> buffs) {
        super(buffs);
    }

    public BeforeBuild(Context context, List<Buff> buffs) {
        super(context, buffs);
    }

    @Override
    public BeforeBuild copy() {
        return new BeforeBuild(context, new ArrayList<>(buffs));
    }
}

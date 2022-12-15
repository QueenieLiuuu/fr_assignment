package edu.cmu.cs214.Santorini.state.run;

import edu.cmu.cs214.Santorini.game.Context;
import edu.cmu.cs214.Santorini.model.Player;
import edu.cmu.cs214.Santorini.state.Buff;

import java.util.ArrayList;
import java.util.List;

public class Win extends RunState {
    public Player getPlayer() {
        return player;
    }

    private Player player;

    public Win(Context context, Player player, List<Buff> buffs) {
        super(context, buffs);
        this.player = player;
    }

    public Win(Player player) {
        this.player = player;
    }

    @Override
    public Win copy() {
        ArrayList<Buff> copyBuffs = new ArrayList<>(buffs);
        return new Win(context, player.copy(), copyBuffs);
    }
}

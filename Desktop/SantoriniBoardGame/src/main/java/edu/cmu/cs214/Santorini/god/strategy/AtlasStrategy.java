package edu.cmu.cs214.Santorini.god.strategy;

import edu.cmu.cs214.Santorini.state.Action;

import java.util.HashSet;
import java.util.List;

/*
 * Your Build: Your Worker may build a dome at any level.
 */
public class AtlasStrategy extends ManStrategy {
    public AtlasStrategy() {
        this.buildActions = new HashSet<>(List.of(Action.build, Action.buildDome));
    }
}

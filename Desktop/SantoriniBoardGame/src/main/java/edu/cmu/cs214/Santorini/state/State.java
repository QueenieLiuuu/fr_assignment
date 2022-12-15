package edu.cmu.cs214.Santorini.state;

import edu.cmu.cs214.Santorini.game.Context;

public abstract class State {
    protected Context context;

    public State(Context context) {
        this.context = context;
    }

    public State() {
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public abstract State copy();
}

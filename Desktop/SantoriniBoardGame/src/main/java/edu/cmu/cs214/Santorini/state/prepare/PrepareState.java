package edu.cmu.cs214.Santorini.state.prepare;

import edu.cmu.cs214.Santorini.game.Context;
import edu.cmu.cs214.Santorini.state.State;

/**
 * (PrepareState)          -> (RunState loops)
 * (GodChosen->InitWorker) -> (BeforeMove-> .... ) -> (next RunStates...)
 */
public abstract class PrepareState extends State {
    public PrepareState() {
    }

    public PrepareState(Context context) {
        super(context);
    }
}

package io.github.verdantis;

import com.badlogic.gdx.utils.Array;

/**
 * records the state of the game.
 */
public class GameState {
    private State state = State.DEFAULT;
    private final Array<StateChangeCallback> callbacks = new Array<>();

    private boolean changingState = false;
    private State nextState = null;


    public void changeState(State state) {
        if (state == this.state) {
            return;
        }

        if (changingState) {
            nextState = state;
            return;
        }

        changingState = true;
        this.state = state;
        for (StateChangeCallback callback : callbacks) {
            callback.stateChanged(state);
        }
        changingState = false;

        if (nextState != null && nextState != this.state) {
            changeState(nextState);
            nextState = null;
        }
    }

    public void registerCallback(StateChangeCallback callback) {
        callbacks.add(callback);
    }

    public State getState() {
        return state;
    }

    public enum State {
        DEFAULT,
        DEFEAT,
        VICTORY,
        RESTART
    }

    public interface StateChangeCallback {
        void stateChanged(State state);
    }
}

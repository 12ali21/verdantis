package io.github.verdantis.components;

/**
 * records the state of the game.
 */
public class GameState {
    private State state = State.DEFAULT;

    public void changeState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public static enum State {
        DEFAULT,
        PLANTING
    }
}

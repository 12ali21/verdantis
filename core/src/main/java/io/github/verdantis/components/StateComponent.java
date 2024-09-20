package io.github.verdantis.components;

import com.badlogic.ashley.core.Component;

public class StateComponent implements Component {
    public States currentState = States.DEFAULT;
    public float time;
    public enum States {
        DEFAULT, SHOOTING
    }
}

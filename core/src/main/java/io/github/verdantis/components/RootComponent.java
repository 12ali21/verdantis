package io.github.verdantis.components;

import com.badlogic.ashley.core.Component;

public class RootComponent implements Component {
    public RootState state = RootState.READY;
    public float waitDuration = 1f;
    public float waitTimer = 0f;

    public enum RootState {
        READY, ACTIVATED, EXPANDING, FINISHED
    }
}

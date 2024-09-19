package io.github.verdantis.components;

import com.badlogic.ashley.core.Component;

public class FreezingComponent implements Component {
    public float freezeDuration = 2f;
    public float freezeTimer = 0;
    public float slowFactor = 4f;
    public boolean slowed = false;
    public float originalDrag;
}

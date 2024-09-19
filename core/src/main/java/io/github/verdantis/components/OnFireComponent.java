package io.github.verdantis.components;

import com.badlogic.ashley.core.Component;

public class OnFireComponent implements Component {
    public float fireDamage = 0.5f;
    public int ticks = 1;
    public float tickDuration = 1f;
    public float tickTimer = 0f;
}

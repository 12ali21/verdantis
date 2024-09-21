package io.github.verdantis.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

public class OnFireComponent implements Component {
    public float fireDamage = 0.5f;
    public int ticks = 1;
    public float tickDuration = 1f;
    public float tickTimer = 0f;
    public Entity effect = null;
    public Vector2 effectOffset = new Vector2();
}

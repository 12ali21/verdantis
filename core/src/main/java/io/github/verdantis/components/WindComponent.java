package io.github.verdantis.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class WindComponent implements Component {
    public float windPower = 2f;
    public float maxWindSpeed = 1.5f;
    public float windTimer = 0;
    public float windDuration = 0.8f;
    public boolean winded = false;
    public Vector2 originalAcceleration = new Vector2();
}

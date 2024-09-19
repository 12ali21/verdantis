package io.github.verdantis.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class MovementComponent implements Component {
    public Vector2 acceleration = new Vector2();
    public Vector2 velocity = new Vector2();
    public float maxSpeed = 1.5f;
    public float drag = 0.1f;
    public float mass = 1f;
}

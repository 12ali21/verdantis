package io.github.verdantis.components;

import com.badlogic.ashley.core.Component;

public class EnemyComponent implements Component {
    public State state = State.WALKING;
    public float maxSpeed = 1f;
    public float damage = 1f;
    public float damageCooldown = 1f;
    public float damageTimer = 1f;
    public int soulAmount = 1;

    public enum State {
        WALKING, DAMAGING
    }
}

package io.github.verdantis.components;

import com.badlogic.ashley.core.Component;

public class HealthComponent implements Component {
    private float health = 1f;

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }
}

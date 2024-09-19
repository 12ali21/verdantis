package io.github.verdantis.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.verdantis.components.FreezingComponent;
import io.github.verdantis.components.MovementComponent;

public class FreezingSystem extends IteratingSystem {
    public FreezingSystem() {
        super(Family.all(FreezingComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        FreezingComponent freezing = entity.getComponent(FreezingComponent.class);
        MovementComponent movement = entity.getComponent(MovementComponent.class);

        freezing.freezeTimer += deltaTime;
        if (freezing.freezeTimer >= freezing.freezeDuration) {
            movement.drag = freezing.originalDrag;
            entity.remove(FreezingComponent.class);
        } else if(!freezing.slowed) {
            // Slow down the entity
            freezing.originalDrag = movement.drag;
            movement.drag *= freezing.slowFactor;
            freezing.slowed = true;
        }
    }
}

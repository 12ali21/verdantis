package io.github.verdantis.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;

import io.github.verdantis.components.FreezingComponent;
import io.github.verdantis.components.MovementComponent;
import io.github.verdantis.components.TextureComponent;
import io.github.verdantis.utils.Mappers;

public class FreezingSystem extends IteratingSystem {
    public FreezingSystem() {
        super(Family.all(FreezingComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        FreezingComponent freezing = Mappers.freezing.get(entity);
        MovementComponent movement = Mappers.movement.get(entity);
        TextureComponent texture = Mappers.texture.get(entity);

        freezing.freezeTimer += deltaTime;
        if (freezing.freezeTimer >= freezing.freezeDuration) {
            movement.drag = freezing.originalDrag;
            entity.remove(FreezingComponent.class);
            texture.color = Color.WHITE;
        } else if(!freezing.slowed) {
            // Slow down the entity
            freezing.originalDrag = movement.drag;
            movement.drag *= freezing.slowFactor;
            freezing.slowed = true;
            texture.color = new Color(0x23b0ffff);
        }
    }
}

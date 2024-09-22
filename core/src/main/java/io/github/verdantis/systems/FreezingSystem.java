package io.github.verdantis.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.verdantis.components.AnimationComponent;
import io.github.verdantis.components.FreezingComponent;
import io.github.verdantis.components.MovementComponent;
import io.github.verdantis.components.OnFireComponent;
import io.github.verdantis.components.StateComponent;
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
        StateComponent stateComponent = Mappers.state.get(entity);
        AnimationComponent animationComponent = Mappers.animation.get(entity);

        OnFireComponent onFireComponent = Mappers.onFire.get(entity);
        if (onFireComponent != null) {
            TextureComponent effectTexture = Mappers.texture.get(onFireComponent.effect);
            effectTexture.color = TextureComponent.BLUE_FIRE_COLOR;
            onFireComponent.fireDamage *= 1.2f;
        }

        freezing.freezeTimer += deltaTime;
        if (freezing.freezeTimer >= freezing.freezeDuration) {
            movement.drag = freezing.originalDrag;
            entity.remove(FreezingComponent.class);
            texture.color = Color.WHITE;

            // Change the animation speed
            for (Animation<TextureRegion> animation : animationComponent.animations.values()) {
                animation.setFrameDuration(animation.getFrameDuration() / 2);
            }
        } else if (!freezing.slowed) {
            // Slow down the entity
            freezing.originalDrag = movement.drag;
            movement.acceleration.y /= freezing.slowFactor;
            freezing.slowed = true;
            texture.color = TextureComponent.FROZEN_COLOR;

            // Change the animation speed
            for (Animation<TextureRegion> animation : animationComponent.animations.values()) {
                animation.setFrameDuration(animation.getFrameDuration() * 2);
            }
        }
    }
}

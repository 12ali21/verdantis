package io.github.verdantis.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.verdantis.components.TransformComponent;
import io.github.verdantis.components.VelocityComponent;
import io.github.verdantis.utils.Mappers;

public class VelocitySystem extends IteratingSystem {
    public VelocitySystem() {
        super(Family.all(VelocityComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transform = Mappers.transform.get(entity);
        VelocityComponent velocity = Mappers.velocity.get(entity);

        transform.position.x += velocity.velocity.x * deltaTime;
        transform.position.y += velocity.velocity.y * deltaTime;
    }
}

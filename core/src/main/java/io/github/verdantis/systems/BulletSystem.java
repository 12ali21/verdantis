package io.github.verdantis.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.verdantis.components.BulletComponent;
import io.github.verdantis.components.TransformComponent;
import io.github.verdantis.utils.Constants;
import io.github.verdantis.utils.Mappers;

public class BulletSystem extends IteratingSystem {
    public BulletSystem() {
        super(Family.all(BulletComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transformComponent = Mappers.transform.get(entity);

        // remove bullet if out of screen
        if (transformComponent.position.y > Constants.WORLD_HEIGHT + 2f) { // + offset
            getEngine().removeEntity(entity);
        }
    }
}

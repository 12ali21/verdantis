package io.github.verdantis.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.verdantis.components.EffectComponent;
import io.github.verdantis.utils.Mappers;

public class EffectSystem extends IteratingSystem {
    public EffectSystem() {
        super(Family.all(EffectComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        EffectComponent effectComponent = Mappers.effect.get(entity);
        if (effectComponent.duration < 0) {
            return;
        }

        effectComponent.timer += deltaTime;
        if (effectComponent.timer >= effectComponent.duration) {
            getEngine().removeEntity(entity);
        }
    }
}

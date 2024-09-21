package io.github.verdantis.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.verdantis.components.EnemyComponent;
import io.github.verdantis.components.HealthComponent;
import io.github.verdantis.components.OnFireComponent;
import io.github.verdantis.components.TransformComponent;
import io.github.verdantis.utils.Mappers;

public class FireDamageSystem extends IteratingSystem {
    public FireDamageSystem() {
        super(Family.all(OnFireComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        OnFireComponent onFireComponent = Mappers.onFire.get(entity);
        EnemyComponent enemy = Mappers.enemy.get(entity);
        HealthComponent enemyHealth = Mappers.health.get(entity);

        TransformComponent enemyTransform = Mappers.transform.get(entity);
        if (onFireComponent.effect != null) {
            Mappers.transform.get(onFireComponent.effect)
                    .setCenter(enemyTransform.getCenter().add(onFireComponent.effectOffset));
        }

        onFireComponent.tickTimer += deltaTime;
        if (onFireComponent.tickTimer >= onFireComponent.tickDuration) {
            onFireComponent.tickTimer = 0;
            onFireComponent.ticks--;
            enemyHealth.setHealth(enemyHealth.getHealth() - onFireComponent.fireDamage);

            if (onFireComponent.ticks <= 0) {
                getEngine().removeEntity(onFireComponent.effect);
                System.out.println("removed effect");
                entity.remove(OnFireComponent.class);
            }
        }
    }
}

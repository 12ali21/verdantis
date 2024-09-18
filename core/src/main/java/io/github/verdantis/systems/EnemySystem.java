package io.github.verdantis.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;

import io.github.verdantis.components.EnemyComponent;
import io.github.verdantis.components.PlantComponent;
import io.github.verdantis.components.TransformComponent;
import io.github.verdantis.components.VelocityComponent;
import io.github.verdantis.utils.Mappers;

public class EnemySystem extends IteratingSystem {

    public EnemySystem() {
        super(Family.all(EnemyComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        EnemyComponent enemy = Mappers.enemy.get(entity);
        TransformComponent enemyTransform = Mappers.transform.get(entity);
        VelocityComponent enemyVelocity = Mappers.velocity.get(entity);


        if (enemy.state == EnemyComponent.State.DAMAGING) {
            enemy.damageTimer -= deltaTime;
        }

        ImmutableArray<Entity> plants =
                getEngine().getEntitiesFor(Family.all(PlantComponent.class).get());
        boolean damaged = false;
        for (Entity plantEntity : plants) {
            PlantComponent plant = Mappers.plant.get(plantEntity);
            if (!plant.isPlanted) {
                continue;
            }

            if (enemyTransform.getRect().overlaps(Mappers.transform.get(plantEntity).getRect())) {
                enemy.state = EnemyComponent.State.DAMAGING;
                enemyVelocity.velocity.setZero();
                damaged = true;
                if (enemy.damageTimer <= 0) {
                    enemy.damageTimer = enemy.damageCooldown;
                    plant.health -= enemy.damage;
                }
            }
        }

        if (!damaged && enemy.state == EnemyComponent.State.DAMAGING) {
            enemy.damageTimer = enemy.damageCooldown;
            enemy.state = EnemyComponent.State.WALKING;
            enemyVelocity.velocity.set(0, -enemy.maxSpeed);
        }

        if (enemy.health <= 0) {
            getEngine().removeEntity(entity);
        }
    }
}

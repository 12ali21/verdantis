package io.github.verdantis.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.audio.Sound;

import io.github.verdantis.Assets;
import io.github.verdantis.components.EnemyComponent;
import io.github.verdantis.components.HealthComponent;
import io.github.verdantis.components.OnFireComponent;
import io.github.verdantis.components.PlantComponent;
import io.github.verdantis.components.RootComponent;
import io.github.verdantis.components.TransformComponent;
import io.github.verdantis.components.MovementComponent;
import io.github.verdantis.utils.Mappers;

public class EnemySystem extends IteratingSystem {

    private final UIManager uiManager;
    private final Assets assets;

    public EnemySystem(UIManager uiManager, Assets assets) {
        super(Family.all(EnemyComponent.class).get());
        this.uiManager = uiManager;
        this.assets = assets;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        EnemyComponent enemy = Mappers.enemy.get(entity);
        HealthComponent enemyHealth = Mappers.health.get(entity);
        MovementComponent enemyVelocity = Mappers.movement.get(entity);


        if (enemy.state == EnemyComponent.State.DAMAGING) {
            enemy.damageTimer -= deltaTime;
        }

        boolean damaged = damagePlants(entity);

        if (!damaged && enemy.state == EnemyComponent.State.DAMAGING) {
            enemy.damageTimer = enemy.damageCooldown;
            enemy.state = EnemyComponent.State.WALKING;
            enemyVelocity.velocity.set(0, -enemy.maxSpeed);
        }

        // Enemy death
        if (enemyHealth.getHealth() <= 0) {
            uiManager.changeSoulAmount(enemy.soulAmount);
            OnFireComponent onFireComponent = Mappers.onFire.get(entity);
            if (onFireComponent != null) {
                if (onFireComponent.effect != null)
                    getEngine().removeEntity(onFireComponent.effect);
            }
            getEngine().removeEntity(entity);
            assets.manager.get(Assets.SLIME_DEATH_SFX, Sound.class).play();
        }
    }

    private boolean damagePlants(Entity enemy) {
        EnemyComponent enemyComponent = Mappers.enemy.get(enemy);
        TransformComponent enemyTransform = Mappers.transform.get(enemy);
        MovementComponent enemyVelocity = Mappers.movement.get(enemy);

        ImmutableArray<Entity> plants =
                getEngine().getEntitiesFor(Family.all(PlantComponent.class).get());
        boolean damaged = false;
        for (Entity plantEntity : plants) {
            PlantComponent plant = Mappers.plant.get(plantEntity);
            HealthComponent plantHealth = Mappers.health.get(plantEntity);
            if (!plant.isPlanted) {
                continue;
            }

            if (enemyTransform.getRect().overlaps(Mappers.transform.get(plantEntity).getRect())) {
                enemyComponent.state = EnemyComponent.State.DAMAGING;
                enemyVelocity.velocity.setZero();
                damaged = true;
                if (enemyComponent.damageTimer <= 0) {
                    enemyComponent.damageTimer = enemyComponent.damageCooldown;
                    plantHealth.setHealth(plantHealth.getHealth() - enemyComponent.damage);
                    assets.manager.get(Assets.SLIME_ATTACK_SFX, Sound.class).play();
                    break;
                }
            }
        }
        damaged = damaged || damageRoots(enemy);
        return damaged;
    }

    private boolean damageRoots(Entity enemy) {
        EnemyComponent enemyComponent = Mappers.enemy.get(enemy);
        TransformComponent enemyTransform = Mappers.transform.get(enemy);
        MovementComponent enemyVelocity = Mappers.movement.get(enemy);

        ImmutableArray<Entity> roots =
                getEngine().getEntitiesFor(Family.all(RootComponent.class).get());
        boolean damaged = false;
        for (Entity rootEntity : roots) {
            HealthComponent rootHealth = Mappers.health.get(rootEntity);

            if (enemyTransform.getRect().overlaps(Mappers.transform.get(rootEntity).getRect())) {
                enemyComponent.state = EnemyComponent.State.DAMAGING;
                enemyVelocity.velocity.setZero();
                damaged = true;
                if (enemyComponent.damageTimer <= 0) {
                    enemyComponent.damageTimer = enemyComponent.damageCooldown;
                    rootHealth.setHealth(rootHealth.getHealth() - enemyComponent.damage);
                    assets.manager.get(Assets.SLIME_ATTACK_SFX, Sound.class).play();
                    break;
                }
            }
        }
        return damaged;
    }
}

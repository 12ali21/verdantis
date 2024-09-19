package io.github.verdantis.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;


import io.github.verdantis.components.BulletComponent;
import io.github.verdantis.components.EnemyComponent;
import io.github.verdantis.components.OnFireComponent;
import io.github.verdantis.components.TransformComponent;
import io.github.verdantis.utils.Constants;
import io.github.verdantis.utils.Element;
import io.github.verdantis.utils.Mappers;

public class BulletSystem extends IteratingSystem {
    public BulletSystem() {
        super(Family.all(BulletComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transformComponent = Mappers.transform.get(entity);
        BulletComponent bulletComponent = Mappers.bullet.get(entity);

        ImmutableArray<Entity> enemies =
                getEngine().getEntitiesFor(Family.all(EnemyComponent.class).get());
        for (Entity enemy : enemies) {
            TransformComponent enemyTransform = Mappers.transform.get(enemy);
            Rectangle rect1 = enemyTransform.getRect();
            Rectangle rect2 = transformComponent.getRect();

            // If hit enemy
            if (rect1.overlaps(rect2)) {
                EnemyComponent enemyComponent = Mappers.enemy.get(enemy);
                enemyComponent.health -= bulletComponent.damage;
                if (bulletComponent.bulletType == Element.FIRE) {
                    dealFireDamage(enemy);
                }


                getEngine().removeEntity(entity);
                return;
            }
        }

        // remove bullet if out of screen
        if (transformComponent.position.y > Constants.WORLD_HEIGHT + 2f) { // + offset
            getEngine().removeEntity(entity);
        }
    }

    private static void dealFireDamage(Entity enemy) {
        int fireTicks = 4;
        OnFireComponent onFireComponent = Mappers.onFire.get(enemy);
        if (onFireComponent == null) {
            onFireComponent = new OnFireComponent();
            onFireComponent.ticks = fireTicks;
            enemy.add(onFireComponent);
        } else {
            onFireComponent.ticks = fireTicks;
        }
    }
}

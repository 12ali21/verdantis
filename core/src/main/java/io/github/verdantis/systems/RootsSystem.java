package io.github.verdantis.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.verdantis.Assets;
import io.github.verdantis.components.AnimationComponent;
import io.github.verdantis.components.EffectComponent;
import io.github.verdantis.components.EnemyComponent;
import io.github.verdantis.components.FreezingComponent;
import io.github.verdantis.components.HealthComponent;
import io.github.verdantis.components.MovementComponent;
import io.github.verdantis.components.OnFireComponent;
import io.github.verdantis.components.RootComponent;
import io.github.verdantis.components.StateComponent;
import io.github.verdantis.components.TextureComponent;
import io.github.verdantis.components.TransformComponent;
import io.github.verdantis.utils.Constants;
import io.github.verdantis.utils.DrawingPriorities;
import io.github.verdantis.utils.Mappers;
import io.github.verdantis.utils.Utils;

public class RootsSystem extends IteratingSystem {

    private final static int NUM_FIRE_TICKS = 3;
    private final static int FIRE_DAMAGE = 5;
    private final Assets assets;

    public RootsSystem(Assets assets) {
        super(Family.all(RootComponent.class).get());
        this.assets = assets;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        RootComponent rootComponent = Mappers.roots.get(entity);
        HealthComponent rootHealth = Mappers.health.get(entity);

        if (rootComponent.state == RootComponent.RootState.READY && rootHealth.getHealth() <= 0) {
            rootComponent.state = RootComponent.RootState.ACTIVATED;
        }

        if (rootComponent.state == RootComponent.RootState.ACTIVATED) {
            rootComponent.state = RootComponent.RootState.EXPANDING;
            // shoot the roots across the screen damaging enemies
            MovementComponent movementComponent = new MovementComponent();
            movementComponent.acceleration.set(0, 100f);
            movementComponent.maxSpeed = 30f;
            entity.add(movementComponent);
            assets.manager.get(Assets.ROOTS_EXPANDING_SFX, Sound.class).play();
        }

        if (rootComponent.state == RootComponent.RootState.EXPANDING) {
            MovementComponent movementComponent = Mappers.movement.get(entity);
            TransformComponent transform = Mappers.transform.get(entity);
            // damage enemies
            Rectangle rootRect = transform.getRect();
            ImmutableArray<Entity> enemies =
                    getEngine().getEntitiesFor(Family.all(EnemyComponent.class).get());
            for (Entity enemy : enemies) {
                TransformComponent enemyTransform = Mappers.transform.get(enemy);
                Rectangle enemyRect = enemyTransform.getRect();
                if (rootRect.overlaps(enemyRect)) {
                    OnFireComponent onFireComponent = Mappers.onFire.get(enemy);
                    if (onFireComponent == null) {
                        onFireComponent = new OnFireComponent();
                        onFireComponent.effect = getTrappedEffect(enemyTransform.getCenter());
                        enemy.add(onFireComponent);
                    }
                    onFireComponent.fireDamage = FIRE_DAMAGE;
                    onFireComponent.ticks = NUM_FIRE_TICKS;

                    FreezingComponent freezingComponent = Mappers.freezing.get(enemy);
                    if (freezingComponent == null) {
                        freezingComponent = new FreezingComponent();
                        enemy.add(freezingComponent);
                    }
                    freezingComponent.freezeTimer = 0;
                    freezingComponent.slowFactor = Float.POSITIVE_INFINITY;
                    freezingComponent.freezeDuration = 3f;
                }

            }
            // reached the end of screen
            if (transform.position.y + transform.height > Constants.WORLD_HEIGHT + 1) {
                rootComponent.state = RootComponent.RootState.FINISHED;
                movementComponent.velocity.setZero();
                movementComponent.acceleration.setZero();
            }
        }
        if (rootComponent.state == RootComponent.RootState.FINISHED) {
            rootComponent.waitTimer += deltaTime;
            if (rootComponent.waitTimer >= rootComponent.waitDuration) {
                getEngine().removeEntity(entity);
            }
        }
    }

    private Entity getTrappedEffect(Vector2 position) {
        Entity effect = Utils.createEntityCenter(getEngine(), assets.spritesAtlas().findRegion(Assets.TRAPPED_EFFECT),
                position.x,
                position.y, 1f, 1f, DrawingPriorities.FIRE_EFFECT
        );

        TextureComponent textureComponent = Mappers.texture.get(effect);
        getEngine().addEntity(effect);

        return effect;
    }
}

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
import io.github.verdantis.components.BulletComponent;
import io.github.verdantis.components.EffectComponent;
import io.github.verdantis.components.EnemyComponent;
import io.github.verdantis.components.FreezingComponent;
import io.github.verdantis.components.HealthComponent;
import io.github.verdantis.components.OnFireComponent;
import io.github.verdantis.components.StateComponent;
import io.github.verdantis.components.TextureComponent;
import io.github.verdantis.components.TransformComponent;
import io.github.verdantis.components.WindComponent;
import io.github.verdantis.utils.Constants;
import io.github.verdantis.utils.DrawingPriorities;
import io.github.verdantis.utils.Element;
import io.github.verdantis.utils.Mappers;
import io.github.verdantis.utils.Utils;

public class BulletSystem extends IteratingSystem {
    private final Assets assets;

    public BulletSystem(Assets assets) {
        super(Family.all(BulletComponent.class).get());
        this.assets = assets;
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
                HealthComponent enemyHealth = Mappers.health.get(enemy);

                enemyHealth.setHealth(enemyHealth.getHealth() - bulletComponent.damage);
                if (bulletComponent.bulletType == Element.FIRE) {
                    dealFireDamage(enemy);
                    assets.manager.get(Assets.FIRE_SFX, Sound.class).play();
                } else if (bulletComponent.bulletType == Element.ICE) {
                    dealIceDamage(enemy);
                    assets.manager.get(Assets.FREEZE_SFX, Sound.class).play();
                } else if (bulletComponent.bulletType == Element.AIR) {
                    applyWindEffect(transformComponent.getCenter());
                    assets.manager.get(Assets.WIND_SFX, Sound.class).play();
                } else {
                    assets.manager.get(Assets.SLIME_DAMAGE_SFX, Sound.class).play();
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

    private void dealFireDamage(Entity enemy) {
        int fireTicks = 4;
        OnFireComponent onFireComponent = Mappers.onFire.get(enemy);
        if (onFireComponent == null) {
            onFireComponent = new OnFireComponent();
            onFireComponent.effect =
                    getFireEffect(Mappers.transform.get(enemy).getCenter());
            onFireComponent.effectOffset.set(0, 0.2f);
            enemy.add(onFireComponent);
        }
        onFireComponent.ticks = fireTicks;
    }

    private Entity getFireEffect(Vector2 position) {
        Animation<TextureRegion> effectAnimation =
                new Animation<>(0.1f, assets.effectsAtlas().findRegions(Assets.FIRE_EFFECT),
                        Animation.PlayMode.LOOP
                );

        Entity effect = Utils.createEntityCenter(getEngine(), effectAnimation.getKeyFrame(0f),
                position.x,
                position.y, 1f, 1f, DrawingPriorities.FIRE_EFFECT
        );

        TextureComponent textureComponent = Mappers.texture.get(effect);
        textureComponent.color = new Color(1, 1, 1, 0.2f);
        textureComponent.textureScale = 1.5f;

        StateComponent stateComponent = new StateComponent();
        stateComponent.currentState = StateComponent.States.DEFAULT;

        AnimationComponent ani = new AnimationComponent();
        ani.animations.put(stateComponent.currentState.ordinal(), effectAnimation);

        EffectComponent effectComponent = new EffectComponent();
        effectComponent.duration = -1;

        effect.add(stateComponent);
        effect.add(ani);
        effect.add(effectComponent);

        getEngine().addEntity(effect);

        return effect;
    }

    private void dealIceDamage(Entity enemy) {
        FreezingComponent freezing = Mappers.freezing.get(enemy);
        if (freezing == null) {
            freezing = new FreezingComponent();
            enemy.add(freezing);
        } else {
            freezing.freezeTimer = 0f;
        }
    }

    private void applyWindEffect(Vector2 contactPos) {
        float windDistance = 1f;
        ImmutableArray<Entity> enemies =
                getEngine().getEntitiesFor(Family.all(EnemyComponent.class).get());
        Rectangle effectRect = new Rectangle(contactPos.x, contactPos.y, 0.1f, windDistance);

        for (Entity enemy : enemies) {
            TransformComponent enemyTransform = Mappers.transform.get(enemy);
            Rectangle rect1 = enemyTransform.getRect();

            if (rect1.overlaps(effectRect)) {
                dealWindDamage(enemy);
            }
        }

        // effect entity
        getWindEffect(contactPos);
    }

    private void getWindEffect(Vector2 contactPos) {
        Animation<TextureRegion> effectAnimation =
                new Animation<>(0.1f, assets.effectsAtlas().findRegions(Assets.GUST_EFFECT));

        Entity effect = Utils.createEntity(getEngine(), effectAnimation.getKeyFrame(0f),
                contactPos.x - 0.5f,
                contactPos.y, 1f, 2f, DrawingPriorities.GUST
        );
        Mappers.texture.get(effect).color = new Color(1, 1, 1, 0.5f);


        StateComponent stateComponent = new StateComponent();
        stateComponent.currentState = StateComponent.States.DEFAULT;

        AnimationComponent ani = new AnimationComponent();
        ani.animations.put(stateComponent.currentState.ordinal(), effectAnimation);

        EffectComponent effectComponent = new EffectComponent();
        effectComponent.duration = effectAnimation.getAnimationDuration();

        effect.add(stateComponent);
        effect.add(ani);
        effect.add(effectComponent);

        getEngine().addEntity(effect);
    }

    private void dealWindDamage(Entity entity) {
        WindComponent wind = Mappers.wind.get(entity);
        if (wind == null) {
            wind = new WindComponent();
            entity.add(wind);
        } else {
            wind.windTimer = 0f;
        }
    }
}

package io.github.verdantis.utils;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.verdantis.Assets;
import io.github.verdantis.systems.EnemyManagerSystem;

public class AnimationFactory {
    private final TextureAtlas shootingAtlas;
    private final TextureAtlas slimeAtlas;

    public AnimationFactory(Assets assets) {
        shootingAtlas = assets.canonShootingAtlas();
        slimeAtlas = assets.slimeAtlas();
    }

    public Animation<TextureRegion> getShootingAnimation(Element element) {
        int numFrames = 10;
        float duration = 2f / numFrames;
        switch (element) {
            case FIRE:
                return new Animation<>(duration, shootingAtlas.findRegions(Assets.FIRE_SHOOTING),
                        Animation.PlayMode.LOOP
                );
            case ICE:
                return new Animation<>(duration, shootingAtlas.findRegions(Assets.ICE_SHOOTING),
                        Animation.PlayMode.LOOP
                );
            case EARTH:
                return new Animation<>(duration, shootingAtlas.findRegions(Assets.GREEN_SHOOTING),
                        Animation.PlayMode.LOOP
                );
            case AIR:
                return new Animation<>(duration, shootingAtlas.findRegions(Assets.AIR_SHOOTING),
                        Animation.PlayMode.LOOP
                );
            default:
                throw new IllegalArgumentException("Invalid element: " + element);
        }
    }

    public Animation<TextureRegion> getSlimeMovingAnimation(EnemyManagerSystem.EnemyType type) {
        float duration = 0.15f;
        switch (type) {
            case GREEN_SLIME:
                return new Animation<>(duration, slimeAtlas.findRegions(Assets.GREEN_SLIME_MOVING),
                        Animation.PlayMode.LOOP
                );
            case YELLOW_SLIME:
                return new Animation<>(duration, slimeAtlas.findRegions(Assets.YELLOW_SLIME_MOVING),
                        Animation.PlayMode.LOOP
                );

            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }

    public Animation<TextureRegion> getSlimeAttackingAnimation(EnemyManagerSystem.EnemyType type) {
        float duration = 0.05f;
        switch (type) {
            case GREEN_SLIME:
                return new Animation<>(duration, slimeAtlas.findRegions(Assets.GREEN_SLIME_ATTACKING),
                        Animation.PlayMode.LOOP
                );
            case YELLOW_SLIME:
                return new Animation<>(duration, slimeAtlas.findRegions(Assets.YELLOW_SLIME_ATTACKING),
                        Animation.PlayMode.LOOP
                );
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }

    public Animation<TextureRegion> getSlimeIdleAnimation(EnemyManagerSystem.EnemyType type) {
        float duration = 0.15f;
        switch (type) {
            case GREEN_SLIME:
                return new Animation<>(duration,
                        slimeAtlas.findRegions(Assets.GREEN_SLIME_MOVING).get(0)
                );
            case YELLOW_SLIME:
                return new Animation<>(duration,
                        slimeAtlas.findRegions(Assets.YELLOW_SLIME_MOVING).get(0)
                );
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }
}

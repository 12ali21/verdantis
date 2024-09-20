package io.github.verdantis.utils;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.verdantis.Assets;

public class AnimationFactory {
    private final TextureAtlas shootingAtlas;

    public AnimationFactory(Assets assets) {
        shootingAtlas = assets.canonShootingAtlas();

    }

    public Animation<TextureRegion> getShootingAnimation(Element element) {
        int numFrames = 10;
        float duration = 2f / numFrames;
        switch (element) {
            case FIRE:
                return new Animation<>(duration, shootingAtlas.findRegions(Assets.FIRE_SHOOTING), Animation.PlayMode.LOOP);
            case ICE    :
                return new Animation<>(duration, shootingAtlas.findRegions(Assets.ICE_SHOOTING), Animation.PlayMode.LOOP);
            case EARTH:
                return new Animation<>(duration, shootingAtlas.findRegions(Assets.GREEN_SHOOTING), Animation.PlayMode.LOOP);
            case AIR:
                return new Animation<>(duration, shootingAtlas.findRegions(Assets.AIR_SHOOTING), Animation.PlayMode.LOOP);
            default:
                throw new IllegalArgumentException("Invalid element: " + element);
        }
    }
}

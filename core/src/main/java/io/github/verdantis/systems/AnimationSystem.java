package io.github.verdantis.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.verdantis.components.AnimationComponent;
import io.github.verdantis.components.StateComponent;
import io.github.verdantis.components.TextureComponent;
import io.github.verdantis.utils.Mappers;

public class AnimationSystem extends IteratingSystem {

    public AnimationSystem() {
        super(Family.all(AnimationComponent.class, StateComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationComponent ani = Mappers.animation.get(entity);
        StateComponent state = Mappers.state.get(entity);

        if (ani.animations.containsKey(state.currentState.ordinal())) {
            TextureComponent texture = Mappers.texture.get(entity);
            texture.region =
                    ani.animations.get(state.currentState.ordinal()).getKeyFrame(state.time);
        }
        state.time += deltaTime;
    }
}

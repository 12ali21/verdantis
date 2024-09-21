package io.github.verdantis.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.github.verdantis.components.MovementComponent;
import io.github.verdantis.components.TransformComponent;
import io.github.verdantis.components.WindComponent;
import io.github.verdantis.utils.Constants;
import io.github.verdantis.utils.Mappers;

public class WindSystem extends IteratingSystem {

    public WindSystem() {
        super(Family.all(WindComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        WindComponent wind = Mappers.wind.get(entity);
        MovementComponent movement = Mappers.movement.get(entity);
        TransformComponent transform = Mappers.transform.get(entity);

        // update timer
        wind.windTimer += deltaTime;

        // calculate wind power
        float power = wind.windPower;
        float penalty = 3f;
        if (transform.position.y > Constants.WORLD_HEIGHT - 1f) {
            power /= penalty;
        }


        float horizontalPower = wind.windPower * 1.2f;
        // change lane
        if (wind.laneOffset != 0) {
            float currentLane = transform.position.x - Constants.PADDING_LEFT;
            if (!wind.startedLaneChange) {
                wind.startedLaneChange = true;
                wind.originalLane = MathUtils.round(currentLane);
            }
            float abs = currentLane - (wind.originalLane + wind.laneOffset);
            abs = abs < 0 ? -abs : abs;
            if (abs < 0.01f) {
                transform.position.x = wind.originalLane + wind.laneOffset + Constants.PADDING_LEFT;

                wind.laneOffset = 0;
                wind.startedLaneChange = false;
                movement.velocity.x = 0;
                movement.acceleration.x = 0;
            } else {
                movement.acceleration.x = wind.laneOffset * horizontalPower;
            }
        }

        // check if wind duration and lane change is over
        if (wind.laneOffset == 0 && wind.windTimer >= wind.windDuration) {
            movement.acceleration.set(wind.originalAcceleration);
            entity.remove(WindComponent.class);
            return;
        }

        // first time wind is applied
        if (!wind.winded) {
            wind.originalAcceleration.set(movement.acceleration);
            wind.winded = true;
        }
        // apply wind force
        if (movement.velocity.y < wind.maxWindSpeed) {
            movement.acceleration.add(0, power * deltaTime);
        }
    }
}

package io.github.verdantis.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

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
        MovementComponent velocity = Mappers.movement.get(entity);
        TransformComponent transform = Mappers.transform.get(entity);

        float power = wind.windPower;
        float penalty = 3f;
        if (transform.position.y > Constants.WORLD_HEIGHT - 1f) {
            power /= penalty;
        }

        wind.windTimer += deltaTime;
        if (wind.windTimer >= wind.windDuration) {
            velocity.acceleration.set(wind.originalAcceleration);
            entity.remove(WindComponent.class);
            return;
        } else if (!wind.winded) {
            wind.originalAcceleration.set(velocity.acceleration);
            wind.winded = true;
        }
        // apply wind force
        if (velocity.velocity.y < wind.maxWindSpeed) {
            velocity.acceleration.add(0, power * deltaTime);
        }
    }
}

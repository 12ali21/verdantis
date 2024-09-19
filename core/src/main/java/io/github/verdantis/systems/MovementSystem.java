package io.github.verdantis.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import io.github.verdantis.components.TransformComponent;
import io.github.verdantis.components.MovementComponent;
import io.github.verdantis.utils.Mappers;

public class MovementSystem extends IteratingSystem {
    private final Vector2 tmp2 = new Vector2();
    private static final int MAXIMUM_DRAG = 100;

    public MovementSystem() {
        super(Family.all(MovementComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transform = Mappers.transform.get(entity);
        MovementComponent movement = Mappers.movement.get(entity);

        // apply drag
        if (movement.drag >= MAXIMUM_DRAG) {
            movement.velocity.setZero();
        } else {
            float drag = movement.drag * movement.velocity.len();
            tmp2.set(movement.velocity).scl(-drag);
            movement.velocity.add(tmp2);

            // apply acceleration
            tmp2.set(movement.acceleration).scl(deltaTime);
            movement.velocity.add(tmp2);
        }
        // apply velocity
        tmp2.set(movement.velocity).scl(deltaTime);
        transform.position.add(tmp2);
    }
}

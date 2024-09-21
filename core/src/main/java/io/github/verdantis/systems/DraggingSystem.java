package io.github.verdantis.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import io.github.verdantis.components.DraggableComponent;
import io.github.verdantis.GameState;
import io.github.verdantis.components.TransformComponent;
import io.github.verdantis.utils.Mappers;

public class DraggingSystem extends IteratingSystem {

    private final InputSystem inputSystem;

    public DraggingSystem(InputSystem inputSystem) {
        super(Family.all(DraggableComponent.class).get());
        this.inputSystem = inputSystem;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        DraggableComponent draggableComponent = Mappers.draggable.get(entity);
        TransformComponent transformComponent = Mappers.transform.get(entity);
        if (draggableComponent.isDragging) {
            if (inputSystem.isMouseDown()) {
                Vector2 mousePosition = inputSystem.getMousePositionInWorld();
                transformComponent.setCenter(mousePosition.x, mousePosition.y);
            } else {
                draggableComponent.isDragging = false;
                inputSystem.setCurrentState(InputSystem.InputState.DEFAULT);
            }
        }
    }
}

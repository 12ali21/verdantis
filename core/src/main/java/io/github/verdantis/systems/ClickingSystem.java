package io.github.verdantis.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.verdantis.components.ClickableComponent;
import io.github.verdantis.components.TransformComponent;
import io.github.verdantis.utils.Mappers;

public class ClickingSystem extends IteratingSystem {
    private final InputSystem inputSystem;
    private Rectangle tmpRect = new Rectangle();

    public ClickingSystem(InputSystem inputSystem) {
        super(Family.all(ClickableComponent.class).get());
        this.inputSystem = inputSystem;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transformComponent = Mappers.transform.get(entity);
        ClickableComponent clickableComponent = Mappers.clickable.get(entity);
        Vector2 clickedPos = inputSystem.getClickedPositionInWorld();
        boolean isClicked = inputSystem.isMouseDown();

        if (Mappers.elements.get(entity) != null) {
            System.out.println(transformComponent.getRect() + " | " + clickedPos);
        }
        if (transformComponent.getRect().contains(clickedPos)) {
            clickableComponent.clicked = isClicked;
        } else {
            clickableComponent.clicked = false;
        }
    }

}

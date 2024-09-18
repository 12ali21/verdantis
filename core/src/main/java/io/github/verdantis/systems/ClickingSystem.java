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

        tmpRect.x = transformComponent.position.x;
        tmpRect.y = transformComponent.position.y;
        tmpRect.width = transformComponent.width;
        tmpRect.height = transformComponent.height;
        tmpRect = scaleRectangle(tmpRect, 1.5f);
        if (tmpRect.contains(clickedPos)) {
            clickableComponent.clicked = isClicked;
        } else {
            clickableComponent.clicked = false;
        }
    }

    private Rectangle scaleRectangle(Rectangle rect, float scl) {
        float w = rect.width * scl;
        float h = rect.height * scl;
        float x = rect.x + (rect.width - w) / 2;
        float y = rect.y + (rect.height - h) / 2;
        return new Rectangle(x, y, w, h);
    }
}

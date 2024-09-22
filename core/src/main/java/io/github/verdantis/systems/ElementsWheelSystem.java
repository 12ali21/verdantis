package io.github.verdantis.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;

import io.github.verdantis.Assets;
import io.github.verdantis.components.ClickableComponent;
import io.github.verdantis.components.ElementsComponent;
import io.github.verdantis.components.TextureComponent;
import io.github.verdantis.components.TileComponent;
import io.github.verdantis.components.TransformComponent;
import io.github.verdantis.utils.DrawingPriorities;
import io.github.verdantis.utils.Element;
import io.github.verdantis.utils.Mappers;
import io.github.verdantis.utils.Utils;

public class ElementsWheelSystem extends IteratingSystem {
    private final Assets assets;
    private final InputSystem inputSystem;
    private final UIManager uiManager;
    private boolean dragging;
    private int lastElementIndex = 1;

    public ElementsWheelSystem(Assets assets, InputSystem inputSystem, UIManager uiManager) {
        super(Family.all(ElementsComponent.class).get());
        this.assets = assets;
        this.inputSystem = inputSystem;
        this.uiManager = uiManager;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ElementsComponent elements = Mappers.elements.get(entity);
        TransformComponent transform = Mappers.transform.get(entity);

        if (elements.dragged) {
            Vector2 mousePos = inputSystem.getMousePositionInWorld();
            transform.setCenter(mousePos);
            if (!inputSystem.isMouseDown()) {
                // find tile
                boolean elementApplied = false;
                ImmutableArray<Entity> tiles =
                        getEngine().getEntitiesFor(Family.all(TileComponent.class).get());
                for (Entity tile : tiles) {
                    TransformComponent tileTransform = Mappers.transform.get(tile);
                    TileComponent tileComponent = Mappers.tile.get(tile);

                    if (tileTransform.getRect().contains(transform.getCenter())) {
                        if (tileComponent.plantable) {
                            // apply a random elements to tile
                            tileComponent.setElement(getElement());
                            assets.manager.get(Assets.APPLY_SPELL_SFX, Sound.class).play();
                            elementApplied = true;
                            break;
                        }
                    }
                }

                // destroy entity
                if (!elementApplied) {
                    uiManager.changeSoulAmount(elements.soulCost);
                }
                getEngine().removeEntity(entity);
                dragging = false;
            }
        } else if (!dragging){
            TextureComponent texture = Mappers.texture.get(entity);
            if (uiManager.getSoulAmount() < elements.soulCost) {
                texture.region = assets.spritesAtlas().findRegion(Assets.ELEMENTS_DISABLED);
                return;
            } else {
                texture.region = assets.spritesAtlas().findRegion(Assets.ELEMENTS_WHEEL);
            }

            ClickableComponent clickable = Mappers.clickable.get(entity);
            if (clickable != null && clickable.clicked) {
                dragging = true;
                clickable.clicked = false;
                Entity draggedEntity = Utils.createEntity(getEngine(),
                        assets.spritesAtlas().findRegion(Assets.ELEMENTS_WHEEL), transform.position.x,
                        transform.position.y,
                        DrawingPriorities.ELEMENTS
                );

                ElementsComponent draggedElements = new ElementsComponent();
                draggedElements.dragged = true;
                draggedElements.soulCost = elements.soulCost;
                draggedEntity.add(draggedElements);

                getEngine().addEntity(draggedEntity);
                uiManager.changeSoulAmount(-elements.soulCost);
            }
        }
    }

    private Element getElement() {
        Element[] elements = Element.values();
        lastElementIndex++;
        if (lastElementIndex >= elements.length) {
            lastElementIndex = 1;
        }
        return elements[lastElementIndex];
    }
}

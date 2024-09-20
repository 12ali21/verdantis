package io.github.verdantis.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;

import io.github.verdantis.components.AnimationComponent;
import io.github.verdantis.components.CanonComponent;
import io.github.verdantis.components.DraggableComponent;
import io.github.verdantis.components.HealthComponent;
import io.github.verdantis.components.PlantComponent;
import io.github.verdantis.components.StateComponent;
import io.github.verdantis.components.TileComponent;
import io.github.verdantis.components.TransformComponent;
import io.github.verdantis.utils.AnimationFactory;
import io.github.verdantis.utils.Element;
import io.github.verdantis.utils.Mappers;

public class PlantingSystem extends IteratingSystem {
    private final UIManager uiManager;
    private final AnimationFactory animationFactory;

    public PlantingSystem(UIManager uiManager, AnimationFactory animationFactory) {
        super(Family.all(PlantComponent.class).get());
        this.uiManager = uiManager;
        this.animationFactory = animationFactory;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transformComponent = Mappers.transform.get(entity);
        DraggableComponent draggableComponent = Mappers.draggable.get(entity);
        PlantComponent plantComponent = Mappers.plantable.get(entity);
        HealthComponent healthComponent = Mappers.health.get(entity);
        CanonComponent canonComponent = Mappers.canon.get(entity);


        if (!draggableComponent.isDragging && !plantComponent.isPlanted) {
            plant(entity, transformComponent, plantComponent, canonComponent);
        }

        if (plantComponent.isPlanted) {
            if (healthComponent.getHealth() <= 0) {
                Mappers.tile.get(plantComponent.occupyingTile).isOccupied = false;
                getEngine().removeEntity(entity);
            }
        }
    }

    private void plant(Entity entity, TransformComponent transformComponent,
            PlantComponent plantComponent,
            CanonComponent canonComponent
    ) {
        ImmutableArray<Entity> tilesArray =
                getEngine().getEntitiesFor(Family.all(TileComponent.class).get());
        for (Entity tile : tilesArray) {
            TileComponent tileComponent = Mappers.tile.get(tile);
            if (tileComponent.isOccupied) {
                continue;
            }
            TransformComponent tileTransform = Mappers.transform.get(tile);
            if (tileTransform.getRect().contains(transformComponent.getCenter())) {
                transformComponent.setCenter(tileTransform.getCenter());
                plantComponent.occupyingTile = tile;
                plantComponent.isPlanted = true;
                tileComponent.isOccupied = true;

                canonComponent.element = tileComponent.element;
                addAnimations(entity, canonComponent.element);
                break;
            }
        }

        // no tile found, remove the plant
        if (!plantComponent.isPlanted) {
            uiManager.changeSoulAmount(plantComponent.soulCost);
            getEngine().removeEntity(entity);
        }
    }

    private void addAnimations(Entity entity, Element element) {
        AnimationComponent animationComponent = new AnimationComponent();

        animationComponent.animations.put(StateComponent.States.SHOOTING.ordinal(),
                animationFactory.getShootingAnimation(element)
        );

        StateComponent stateComponent = new StateComponent();
        stateComponent.currentState = StateComponent.States.SHOOTING;

        entity.add(stateComponent);
        entity.add(animationComponent);
    }
}

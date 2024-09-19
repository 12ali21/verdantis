package io.github.verdantis.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.verdantis.UIManager;
import io.github.verdantis.components.CanonComponent;
import io.github.verdantis.components.ClickableComponent;
import io.github.verdantis.components.GameState;
import io.github.verdantis.components.DraggableComponent;
import io.github.verdantis.components.HealthComponent;
import io.github.verdantis.components.PlantComponent;
import io.github.verdantis.components.SeedComponent;
import io.github.verdantis.components.TextureComponent;
import io.github.verdantis.components.TransformComponent;
import io.github.verdantis.utils.DrawingPriorities;
import io.github.verdantis.utils.Mappers;
import io.github.verdantis.utils.Utils;

/**
 * Handles the logic for when a seed is clicked.
 */
public class SeedSystem extends IteratingSystem {

    private final GameState gameState;
    private final UIManager uiManager;

    public SeedSystem(GameState gameState, UIManager uiManager) {
        super(Family.all(SeedComponent.class).get());
        this.gameState = gameState;
        this.uiManager = uiManager;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (gameState.getState() == GameState.State.PLANTING) {
            return;
        }

        ClickableComponent clickableComponent = Mappers.clickable.get(entity);
        SeedComponent seedComponent = Mappers.seed.get(entity);

        if (clickableComponent.clicked) {
            clickableComponent.clicked = false;
            if (uiManager.getSoulAmount() < seedComponent.soulCost) {
                return;
            }
            uiManager.changeSoulAmount(-seedComponent.soulCost);

            TextureComponent textureComponent = Mappers.texture.get(entity);
            TransformComponent transformComponent = Mappers.transform.get(entity);

            Entity seedEntity = Utils.createEntity(getEngine(), textureComponent.region,
                    transformComponent.position.x, transformComponent.position.y,
                    seedComponent.plantWidth, seedComponent.plantHeight, DrawingPriorities.PLANTS
            );
            DraggableComponent draggableComponent = new DraggableComponent();
            draggableComponent.isDragging = true;
            seedEntity.add(draggableComponent);

            PlantComponent plantComponent = new PlantComponent();
            plantComponent.soulCost = seedComponent.soulCost;
            seedEntity.add(plantComponent);

            HealthComponent healthComponent = new HealthComponent();
            seedEntity.add(healthComponent);

            CanonComponent canonComponent = new CanonComponent();
            canonComponent.bulletOffset.set(0, 0.4f);
            canonComponent.bulletSpeed = 2f;
            canonComponent.bulletCooldown = 2f;
            seedEntity.add(canonComponent);

            getEngine().addEntity(seedEntity);

            gameState.changeState(GameState.State.PLANTING);
        }
    }
}

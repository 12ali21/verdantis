package io.github.verdantis.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import io.github.verdantis.Assets;
import io.github.verdantis.components.BulletComponent;
import io.github.verdantis.components.CanonComponent;
import io.github.verdantis.components.PlantComponent;
import io.github.verdantis.components.StateComponent;
import io.github.verdantis.components.TextureComponent;
import io.github.verdantis.components.TransformComponent;
import io.github.verdantis.components.MovementComponent;
import io.github.verdantis.utils.DrawingPriorities;
import io.github.verdantis.utils.Element;
import io.github.verdantis.utils.Mappers;
import io.github.verdantis.utils.Utils;

public class ShootingSystem extends IteratingSystem {
    private final Assets assets;
    private final static float BULLET_SIZE = 0.3f;

    public ShootingSystem(Assets assets) {
        super(Family.all(CanonComponent.class).get());
        this.assets = assets;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlantComponent plant = Mappers.plantable.get(entity);
        if (!plant.isPlanted) {
            return;
        }

        CanonComponent canon = Mappers.canon.get(entity);
        TransformComponent transformComponent = Mappers.transform.get(entity);
        TextureComponent textureComponent = Mappers.texture.get(entity);

        textureComponent.region = getCanonRegion(canon.element);

        // Shoot a bullet
        canon.bulletTimer += deltaTime;
        if (canon.bulletTimer >= canon.bulletCooldown) {
            canon.bulletTimer = 0;
            // reset animation
            StateComponent stateComponent = Mappers.state.get(entity);
            stateComponent.time = 0f;

            Vector2 canonCenter = transformComponent.getCenter();
            canonCenter.add(canon.bulletOffset);
            // Create a bullet entity
            Entity bulletEntity =
                    Utils.createEntityCenter(getEngine(), getBulletTexture(canon.element),
                            canonCenter.x, canonCenter.y,
                            BULLET_SIZE, BULLET_SIZE,
                            DrawingPriorities.BULLETS
                    );
            // Add bullet component
            BulletComponent bulletComponent = new BulletComponent();
            bulletComponent.damage = canon.bulletDamage;
            bulletComponent.bulletType = canon.element;
            bulletEntity.add(bulletComponent);

            // Add velocity component
            MovementComponent movementComponent = new MovementComponent();
            movementComponent.maxSpeed = canon.bulletSpeed;
            movementComponent.acceleration.set(0, 10f);

            bulletEntity.add(movementComponent);

            getEngine().addEntity(bulletEntity);
        }
    }

    private TextureRegion getBulletTexture(Element bulletType) {
        switch (bulletType) {
            case EARTH:
                return assets.spritesAtlas().findRegion(Assets.DIRT_BULLET);
            case FIRE:
                return assets.spritesAtlas().findRegion(Assets.FIRE_BULLET);
            case ICE:
                return assets.spritesAtlas().findRegion(Assets.ICE_BULLET);
            case AIR:
                return assets.spritesAtlas().findRegion(Assets.WIND_BULLET);
            default:
                return null;
        }
    }

    private TextureRegion getCanonRegion(Element element) {
        switch (element) {
            case EARTH:
                return assets.spritesAtlas().findRegion(Assets.GREEN_PLANT);
            case FIRE:
                return assets.spritesAtlas().findRegion(Assets.FIRE_PLANT);
            case ICE:
                return assets.spritesAtlas().findRegion(Assets.ICE_PLANT);
            case AIR:
                return assets.spritesAtlas().findRegion(Assets.AIR_PLANT);
            default:
                return null;
        }
    }
}

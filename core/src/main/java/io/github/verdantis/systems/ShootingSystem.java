package io.github.verdantis.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;

import io.github.verdantis.components.BulletComponent;
import io.github.verdantis.components.CanonComponent;
import io.github.verdantis.components.PlantComponent;
import io.github.verdantis.components.TransformComponent;
import io.github.verdantis.components.VelocityComponent;
import io.github.verdantis.utils.Mappers;
import io.github.verdantis.utils.Utils;

public class ShootingSystem extends IteratingSystem {
    private final TextureAtlas atlas;
    private final static float BULLET_SIZE = 0.3f;

    public ShootingSystem(TextureAtlas atlas) {
        super(Family.all(CanonComponent.class).get());
        this.atlas = atlas;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlantComponent plant = Mappers.plantable.get(entity);
        if (!plant.isPlanted) {
            return;
        }

        CanonComponent canon = Mappers.canon.get(entity);
        TransformComponent transformComponent = Mappers.transform.get(entity);
        // Shoot a bullet
        canon.bulletTimer -= deltaTime;
        if (canon.bulletTimer <= 0) {
            canon.bulletTimer = canon.bulletCooldown;
            Vector2 canonCenter = transformComponent.getCenter();
            canonCenter.add(canon.bulletOffset);
            // Create a bullet entity
            Entity bulletEntity =
                    Utils.createEntityCenter(getEngine(), getBulletTexture(canon.bulletType),
                            canonCenter.x, canonCenter.y,
                            BULLET_SIZE, BULLET_SIZE,
                            transformComponent.z + 1
                    );
            // Add bullet component
            BulletComponent bulletComponent = new BulletComponent();
            bulletComponent.damage = canon.bulletDamage;
            bulletComponent.bulletType = canon.bulletType;
            bulletEntity.add(bulletComponent);

            // Add velocity component
            VelocityComponent velocityComponent = new VelocityComponent();
            velocityComponent.velocity.set(0, canon.bulletSpeed);
            bulletEntity.add(velocityComponent);

            getEngine().addEntity(bulletEntity);
        }
    }

    private TextureRegion getBulletTexture(CanonComponent.BulletType bulletType) {
        switch (bulletType) {
            case EARTH:
                return atlas.findRegion("dirt_bullet");
            case FIRE:
                return atlas.findRegion("fire_bullet");
            case ICE:
                return atlas.findRegion("ice_bullet");
            case AIR:
                return atlas.findRegion("wind_bullet");
            default:
                return null;
        }
    }
}

package io.github.verdantis.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;

import io.github.verdantis.components.EnemyComponent;
import io.github.verdantis.components.VelocityComponent;
import io.github.verdantis.utils.Constants;
import io.github.verdantis.utils.Utils;

public class EnemyManagerSystem extends EntitySystem {
    private static final float ENEMY_SIZE = 0.7f;
    private final TextureAtlas atlas;
    private final static float ENEMY_SPAWN_TIME = 5f;
    private float timeSinceLastSpawn = 0f;
    private RandomXS128 random = new RandomXS128();
    private Vector2 tmp2 = new Vector2();

    public EnemyManagerSystem(TextureAtlas atlas) {
        this.atlas = atlas;
    }

    @Override
    public void update(float deltaTime) {
        timeSinceLastSpawn += deltaTime;
        if (timeSinceLastSpawn >= ENEMY_SPAWN_TIME) {
            timeSinceLastSpawn = 0f;
            spawnEnemy();
        }
    }

    private void spawnEnemy() {
        EnemyType enemyType = EnemyType.GREEN_SLIME; //temp

        // choose a random line to spawn
        int line = random.nextInt(Constants.NUM_LINES);
        float x = Constants.PADDING_LEFT + line + 0.5f;
        float y = Constants.WORLD_HEIGHT;


        Entity enemy = Utils.createEntityCenter(getEngine(), getEnemyRegion(enemyType), x, y, ENEMY_SIZE,
                ENEMY_SIZE, 3
        );
        EnemyComponent enemyComponent = new EnemyComponent();
        enemyComponent.health = 10f;
        enemyComponent.maxSpeed = 0.5f;
        enemy.add(enemyComponent);

        VelocityComponent velocityComponent = new VelocityComponent();
        velocityComponent.velocity.set(0, -enemyComponent.maxSpeed);
        enemy.add(velocityComponent);


        getEngine().addEntity(enemy);
    }

    private TextureRegion getEnemyRegion(EnemyType enemyType) {
        switch (enemyType) {
            case GREEN_SLIME:
                return atlas.findRegion("green_slime");
            case YELLOW_SLIME:
                return atlas.findRegion("yellow_slime");
            case RED_SLIME:
                return atlas.findRegion("red_slime");
            default:
                return null;
        }
    }

    public static enum EnemyType {
        GREEN_SLIME, YELLOW_SLIME, RED_SLIME
    }
}

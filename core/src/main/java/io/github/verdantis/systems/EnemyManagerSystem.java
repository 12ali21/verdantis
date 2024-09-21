package io.github.verdantis.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import io.github.verdantis.Assets;
import io.github.verdantis.GameState;
import io.github.verdantis.components.EnemyComponent;
import io.github.verdantis.components.HealthComponent;
import io.github.verdantis.components.MovementComponent;
import io.github.verdantis.components.TransformComponent;
import io.github.verdantis.utils.Constants;
import io.github.verdantis.utils.DrawingPriorities;
import io.github.verdantis.utils.Mappers;
import io.github.verdantis.utils.Utils;

public class EnemyManagerSystem extends EntitySystem {
    private static final float ENEMY_SIZE = 0.7f;
    private final Assets assets;
    private final GameState gameState;
    private final GameLevel level;
    private final Array<Phase> level1Phases;
    private RandomXS128 random = new RandomXS128();
    private Vector2 tmp2 = new Vector2();

    private boolean finishedLevel = false;

    private boolean debugSpawn = false;

    public EnemyManagerSystem(Assets assets, GameState gameState, GameLevel level) {
        this.assets = assets;
        this.gameState = gameState;
        this.level = level;

        level1Phases = new Array<>();
        level1Phases.add(new Phase(8f, EnemyType.GREEN_SLIME, 2));
        level1Phases.add(new Phase(10f, EnemyType.GREEN_SLIME, 0));
        level1Phases.add(new Phase(8f, EnemyType.GREEN_SLIME, 3));
        level1Phases.add(new Phase(8f, EnemyType.GREEN_SLIME, 1));
        level1Phases.add(new Phase(8f, EnemyType.GREEN_SLIME, 4));
        level1Phases.add(new Phase(6f, EnemyType.GREEN_SLIME, 0));
        level1Phases.add(new Phase(4f, EnemyType.GREEN_SLIME, 2));
        level1Phases.add(new Phase(6f, EnemyType.GREEN_SLIME, 0));
        level1Phases.add(new Phase(8f, EnemyType.GREEN_SLIME, 4));
        level1Phases.add(new Phase(4f, EnemyType.GREEN_SLIME, 3));
        level1Phases.add(new Phase(6f, EnemyType.GREEN_SLIME, -1));
        level1Phases.add(new Phase(5f, EnemyType.GREEN_SLIME, -1));
        level1Phases.add(new Phase(5f, EnemyType.GREEN_SLIME, -1));
        level1Phases.add(new Phase(5f, EnemyType.GREEN_SLIME, -1));
        level1Phases.add(new Phase(5f, EnemyType.GREEN_SLIME, -1));
    }

    @Override
    public void update(float deltaTime) {
        if (!debugSpawn) {
            debugSpawn = true;
            spawnEnemy(Constants.PADDING_LEFT + 2.5f, 4f);
        }

        if (gameState.getState() == GameState.State.DEFAULT && checkDefeat()) {
            gameState.changeState(GameState.State.DEFEAT);
            assets.manager.get(Assets.DEFEAT_SFX, Sound.class).play();
        }

        switch (level) {
            case LEVEL_1:
                if (!finishedLevel) {
                    updateLevel(level1Phases, deltaTime);
                }
                break;
            case LEVEL_2:
            case LEVEL_3:
        }
    }

    private boolean checkDefeat() {
        ImmutableArray<Entity> enemies =
                getEngine().getEntitiesFor(Family.all(EnemyComponent.class).get());
        for (Entity enemy : enemies) {
            TransformComponent enemyTransform = Mappers.transform.get(enemy);
            if (enemyTransform.position.y < 0) {
                return true;
            }
        }
        return false;
    }

    private void updateLevel(Array<Phase> phases, float deltaTime) {
        if (phases.size <= 0) {
            ImmutableArray<Entity> enemies =
                    getEngine().getEntitiesFor(Family.all(EnemyComponent.class).get());
            if (enemies.size() == 0) {
                finishedLevel = true;
                gameState.changeState(GameState.State.VICTORY);
                assets.manager.get(Assets.VICTORY_SFX, Sound.class).play();
            }
        } else {

            Phase currentPhase = phases.get(0);
            currentPhase.timer -= deltaTime;
            if (currentPhase.timer <= 0) {
                if (currentPhase.line < 0) {
                    spawnEnemy();
                } else {
                    spawnEnemy(Constants.PADDING_LEFT + currentPhase.line + 0.5f,
                            Constants.WORLD_HEIGHT
                    );
                }
                phases.removeIndex(0);
            }
        }
    }

    private void spawnEnemy() {
        // choose a random line to spawn
        int line = random.nextInt(Constants.NUM_LINES);
        float x = Constants.PADDING_LEFT + line + 0.5f;
        float y = Constants.WORLD_HEIGHT;
        spawnEnemy(x, y);
    }

    private void spawnEnemy(float x, float y) {
        EnemyType enemyType = EnemyType.GREEN_SLIME; //temp

        Entity enemy =
                Utils.createEntityCenter(getEngine(), getEnemyRegion(enemyType), x, y, ENEMY_SIZE,
                        ENEMY_SIZE, DrawingPriorities.ENEMIES
                );
        EnemyComponent enemyComponent = new EnemyComponent();

        enemyComponent.maxSpeed = 0.5f;
        enemy.add(enemyComponent);

        HealthComponent healthComponent = new HealthComponent();
        healthComponent.setHealth(10f);
        enemy.add(healthComponent);

        MovementComponent movementComponent = new MovementComponent();
        movementComponent.maxSpeed = enemyComponent.maxSpeed;
        movementComponent.acceleration.set(0, -0.5f);
        enemy.add(movementComponent);


        getEngine().addEntity(enemy);
    }

    private TextureRegion getEnemyRegion(EnemyType enemyType) {
        switch (enemyType) {
            case GREEN_SLIME:
                return assets.spritesAtlas().findRegion(Assets.GREEN_SLIME);
            case YELLOW_SLIME:
                return assets.spritesAtlas().findRegion(Assets.YELLOW_SLIME);
            case RED_SLIME:
                return assets.spritesAtlas().findRegion(Assets.RED_SLIME);
            default:
                return null;
        }
    }

    public enum EnemyType {
        GREEN_SLIME, YELLOW_SLIME, RED_SLIME
    }

    public enum GameLevel {
        LEVEL_1, LEVEL_2, LEVEL_3
    }

    private static class Phase {
        private float timer;
        private EnemyType enemyToSpawn;
        private final int line;

        public Phase(float timer, EnemyType enemyToSpawn, int line) {
            this.timer = timer;
            this.enemyToSpawn = enemyToSpawn;
            this.line = line;
        }
    }
}

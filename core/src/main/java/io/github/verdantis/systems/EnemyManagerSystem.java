package io.github.verdantis.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import io.github.verdantis.Assets;
import io.github.verdantis.GameState;
import io.github.verdantis.components.AnimationComponent;
import io.github.verdantis.components.EnemyComponent;
import io.github.verdantis.components.HealthComponent;
import io.github.verdantis.components.MovementComponent;
import io.github.verdantis.components.StateComponent;
import io.github.verdantis.components.TransformComponent;
import io.github.verdantis.utils.AnimationFactory;
import io.github.verdantis.utils.Constants;
import io.github.verdantis.utils.DrawingPriorities;
import io.github.verdantis.utils.Mappers;
import io.github.verdantis.utils.Utils;

public class EnemyManagerSystem extends EntitySystem {
    public static final float ENEMY_SCALE = 0.7f;
    private final Assets assets;
    private final GameState gameState;
    private final AnimationFactory animationFactory;
    private boolean endless = false;

    private final int level;
    private Array<Phase> level1Phases;
    private Array<Phase> level2Phases;
    private Array<Phase> level3Phases;

    private final RandomXS128 random = new RandomXS128();

    private boolean finishedLevel = false;
    private Phase currentPhase;

    public EnemyManagerSystem(Assets assets, GameState gameState, AnimationFactory animationFactory,
            int levelNum
    ) {
        this.assets = assets;
        this.gameState = gameState;
        this.animationFactory = animationFactory;
        this.level = levelNum;

        initLevel1Phases();
        initLevel2Phases();
        initLevel3Phases();

        if(levelNum == -1) {
            endless = true;
        }
    }

    private void initLevel1Phases() {
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


    private void initLevel2Phases() {
        level2Phases = new Array<>();

        level2Phases.add(new Phase(8f, EnemyType.GREEN_SLIME, 3));
        level2Phases.add(new Phase(8f, EnemyType.GREEN_SLIME, 1));
        level2Phases.add(new Phase(6f, EnemyType.GREEN_SLIME, 2));
        level2Phases.add(new Phase(6f, EnemyType.GREEN_SLIME, 0));
        level2Phases.add(new Phase(6f, EnemyType.GREEN_SLIME, 4));
        level2Phases.add(new Phase(6f, EnemyType.GREEN_SLIME, 0));
        level2Phases.add(new Phase(4f, EnemyType.GREEN_SLIME, 2));
        level2Phases.add(new Phase(6f, EnemyType.GREEN_SLIME, 0));
        level2Phases.add(new Phase(8f, EnemyType.YELLOW_SLIME, 4));
        level2Phases.add(new Phase(4f, EnemyType.GREEN_SLIME, 3));
        level2Phases.add(new Phase(6f, EnemyType.YELLOW_SLIME, -1));
        level2Phases.add(new Phase(5f, EnemyType.GREEN_SLIME, -1));
        level2Phases.add(new Phase(3f, EnemyType.YELLOW_SLIME, -1));
        level2Phases.add(new Phase(5f, EnemyType.GREEN_SLIME, -1));
        level2Phases.add(new Phase(5f, EnemyType.GREEN_SLIME, -1));
        level2Phases.add(new Phase(4f, EnemyType.GREEN_SLIME, -1));
        level2Phases.add(new Phase(4f, EnemyType.YELLOW_SLIME, -1));
        level2Phases.add(new Phase(3f, EnemyType.YELLOW_SLIME, -1));
        level2Phases.add(new Phase(3f, EnemyType.YELLOW_SLIME, -1));
        level2Phases.add(new Phase(4f, EnemyType.GREEN_SLIME, -1));
    }

    private void initLevel3Phases() {
        level3Phases = new Array<>();

        level3Phases.add(new Phase(7f, EnemyType.GREEN_SLIME, 3));
        level3Phases.add(new Phase(7f, EnemyType.GREEN_SLIME, 2));
        level3Phases.add(new Phase(5f, EnemyType.GREEN_SLIME, 4));
        level3Phases.add(new Phase(5f, EnemyType.YELLOW_SLIME, 0));
        level3Phases.add(new Phase(5f, EnemyType.GREEN_SLIME, 3));
        level3Phases.add(new Phase(4f, EnemyType.YELLOW_SLIME, 2));
        level3Phases.add(new Phase(5f, EnemyType.GREEN_SLIME, 4));
        level3Phases.add(new Phase(3f, EnemyType.YELLOW_SLIME, 1));
        level3Phases.add(new Phase(5f, EnemyType.GREEN_SLIME, 0));
        level3Phases.add(new Phase(6f, EnemyType.YELLOW_SLIME, 4));
        level3Phases.add(new Phase(4f, EnemyType.YELLOW_SLIME, 2));
        level3Phases.add(new Phase(4f, EnemyType.GREEN_SLIME, 3));
        level3Phases.add(new Phase(3f, EnemyType.YELLOW_SLIME, 0));
        level3Phases.add(new Phase(3f, EnemyType.YELLOW_SLIME, -1));
        level3Phases.add(new Phase(3f, EnemyType.YELLOW_SLIME, -1));
        level3Phases.add(new Phase(2f, EnemyType.YELLOW_SLIME, -1));
        level3Phases.add(new Phase(3f, EnemyType.GREEN_SLIME, -1));
        level3Phases.add(new Phase(2f, EnemyType.YELLOW_SLIME, -1));
        level3Phases.add(new Phase(3f, EnemyType.YELLOW_SLIME, -1));
        level3Phases.add(new Phase(2f, EnemyType.GREEN_SLIME, -1));

    }

    @Override
    public void update(float deltaTime) {
        if (gameState.getState() == GameState.State.DEFAULT && checkDefeat()) {
            gameState.changeState(GameState.State.DEFEAT);
            assets.manager.get(Assets.DEFEAT_SFX, Sound.class).play();
        }

        if (endless) {
            updateEndless(deltaTime);
            return;
        }

        switch (level) {
            case 1:
                if (!finishedLevel) {
                    updateLevel(level1Phases, deltaTime);
                }
                break;
            case 2:
                if (!finishedLevel) {
                    updateLevel(level2Phases, deltaTime);
                }
                break;

            case 3:
                if (!finishedLevel) {
                    updateLevel(level3Phases, deltaTime);
                }
                break;
        }

        if (finishedLevel) {
            gameState.changeState(GameState.State.VICTORY);
            assets.manager.get(Assets.VICTORY_SFX, Sound.class).play();
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


    private int phaseNum = 0;
    private void updateEndless(float deltaTime) {
        if (currentPhase == null || currentPhase.timer <= 0) {
            float yellowSlimeChance;
            if (phaseNum > 8) {
                yellowSlimeChance = MathUtils.log2(phaseNum - 8) / 6f;
                if (yellowSlimeChance > 0.8f) {
                    yellowSlimeChance = 0.8f;
                }
            } else {
                yellowSlimeChance = 0f;
            }

            float duration = (float) (10 * Math.exp(-0.05 * phaseNum));
            if (duration < 2f)
                duration = 2f;

            EnemyType type = random.nextFloat() < yellowSlimeChance ? EnemyType.YELLOW_SLIME :
                    EnemyType.GREEN_SLIME;

            currentPhase = new Phase(duration, type, -1);
            phaseNum++;
        }
        updatePhase(currentPhase, deltaTime);
    }

    private void updateLevel(Array<Phase> phases, float deltaTime) {
        if (phases.size <= 0) {
            ImmutableArray<Entity> enemies =
                    getEngine().getEntitiesFor(Family.all(EnemyComponent.class).get());
            if (enemies.size() == 0) {
                finishedLevel = true;
            }
        } else {
            currentPhase = phases.get(0);
            if (updatePhase(currentPhase, deltaTime))
                phases.removeIndex(0);
        }
    }

    private boolean updatePhase(Phase currentPhase, float deltaTime) {
        currentPhase.timer -= deltaTime;
        if (currentPhase.timer <= 0) {
            if (currentPhase.line < 0) {
                spawnEnemy(currentPhase.enemyToSpawn);
            } else {
                spawnEnemy(currentPhase.enemyToSpawn,
                        Constants.PADDING_LEFT + currentPhase.line + 0.5f,
                        Constants.WORLD_HEIGHT
                );
            }
            return true;
        }
        return false;
    }

    private void spawnEnemy(EnemyType type) {
        // choose a random line to spawn
        int line = random.nextInt(Constants.NUM_LINES);
        float x = Constants.PADDING_LEFT + line + 0.5f;
        float y = Constants.WORLD_HEIGHT;
        spawnEnemy(type, x, y);
    }

    private void spawnEnemy(EnemyType type, float x, float y) {
        Entity enemy =
                Utils.createEntityCenter(getEngine(), getEnemyRegion(type), x, y, 1f,
                        1f, DrawingPriorities.ENEMIES
                );
        Mappers.transform.get(enemy).rectScale = ENEMY_SCALE;
        Mappers.texture.get(enemy).textureScale = ENEMY_SCALE;

        EnemyComponent enemyComponent = new EnemyComponent();

        enemyComponent.maxSpeed = 0.5f;
        enemy.add(enemyComponent);

        HealthComponent healthComponent = new HealthComponent();
        if (type == EnemyType.GREEN_SLIME) {
            healthComponent.setHealth(10f);
            enemyComponent.damage = 1f;
        } else if (type == EnemyType.YELLOW_SLIME) {
            healthComponent.setHealth(20f);
            enemyComponent.damage = 1.5f;
        }
        enemy.add(healthComponent);

        MovementComponent movementComponent = new MovementComponent();
        movementComponent.maxSpeed = enemyComponent.maxSpeed;
        movementComponent.acceleration.set(0, -0.5f);
        enemy.add(movementComponent);

        StateComponent stateComponent = new StateComponent();
        stateComponent.currentState = StateComponent.States.MOVING;


        Animation<TextureRegion> idleAnimation = animationFactory.getSlimeIdleAnimation(type);
        Animation<TextureRegion> movingAnimation = animationFactory.getSlimeMovingAnimation(type);
        Animation<TextureRegion> attackingAnimation =
                animationFactory.getSlimeAttackingAnimation(type);


        AnimationComponent animationComponent = new AnimationComponent();
        animationComponent.animations.put(StateComponent.States.DEFAULT.ordinal(), idleAnimation);
        animationComponent.animations.put(StateComponent.States.MOVING.ordinal(), movingAnimation);
        animationComponent.animations.put(StateComponent.States.ATTACKING.ordinal(),
                attackingAnimation
        );


        enemy.add(stateComponent);
        enemy.add(animationComponent);

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

    private static class Phase {
        private float timer;
        private final EnemyType enemyToSpawn;
        private final int line;

        public Phase(float timer, EnemyType enemyToSpawn, int line) {
            this.timer = timer;
            this.enemyToSpawn = enemyToSpawn;
            this.line = line;
        }
    }
}

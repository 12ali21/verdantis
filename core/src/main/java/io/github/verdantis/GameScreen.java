package io.github.verdantis;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.RandomXS128;

import java.util.Random;

import io.github.verdantis.components.ClickableComponent;
import io.github.verdantis.components.GameState;
import io.github.verdantis.components.SeedComponent;
import io.github.verdantis.components.TileComponent;
import io.github.verdantis.components.TransformComponent;
import io.github.verdantis.systems.BulletSystem;
import io.github.verdantis.systems.ClickingSystem;
import io.github.verdantis.systems.DraggingSystem;
import io.github.verdantis.systems.EnemyManagerSystem;
import io.github.verdantis.systems.EnemySystem;
import io.github.verdantis.systems.FireDamageSystem;
import io.github.verdantis.systems.FreezingSystem;
import io.github.verdantis.systems.InputSystem;
import io.github.verdantis.systems.PlantingSystem;
import io.github.verdantis.systems.RenderingSystem;
import io.github.verdantis.systems.SeedSystem;
import io.github.verdantis.systems.ShootingSystem;
import io.github.verdantis.systems.MovementSystem;
import io.github.verdantis.systems.WindSystem;
import io.github.verdantis.utils.Constants;
import io.github.verdantis.utils.DrawingPriorities;
import io.github.verdantis.utils.Element;
import io.github.verdantis.utils.Utils;

public class GameScreen extends ScreenAdapter {
    private Engine engine;
    private final AssetManager assets;
    private TextureAtlas atlas;
    private GameState gameState;
    private final Random random = new RandomXS128();


    public GameScreen(AssetManager assets) {
        this.assets = assets;
    }

    @Override
    public void show() {
        atlas = new TextureAtlas(Gdx.files.internal("sprites.atlas"));
        gameState = new GameState();

        engine = new Engine();
        initializeSystems();

        createTree();
        createTiles(3);
        createSeedTray();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void initializeSystems() {
        RenderingSystem renderingSystem = new RenderingSystem();
        InputSystem inputSystem = new InputSystem(renderingSystem.getCamera());
        Gdx.input.setInputProcessor(inputSystem);
        UIManager uiManager = new UIManager(atlas, 2);
        ClickingSystem clickingSystem = new ClickingSystem(inputSystem);
        SeedSystem seedSystem = new SeedSystem(gameState, uiManager);
        DraggingSystem draggingSystem = new DraggingSystem(inputSystem, gameState);
        PlantingSystem plantingSystem = new PlantingSystem(uiManager);
        ShootingSystem shootingSystem = new ShootingSystem(atlas);
        MovementSystem movementSystem = new MovementSystem();
        BulletSystem bulletSystem = new BulletSystem();
        EnemyManagerSystem enemyManagerSystem = new EnemyManagerSystem(atlas);
        EnemySystem enemySystem = new EnemySystem(uiManager);
        // Element systems
        FireDamageSystem fireDamageSystem = new FireDamageSystem();
        FreezingSystem freezingSystem = new FreezingSystem();
        WindSystem windSystem = new WindSystem();

        engine.addSystem(renderingSystem);
        engine.addSystem(inputSystem);
        engine.addSystem(clickingSystem);
        engine.addSystem(seedSystem);
        engine.addSystem(draggingSystem);
        engine.addSystem(plantingSystem);
        engine.addSystem(shootingSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(bulletSystem);
        engine.addSystem(enemyManagerSystem);
        engine.addSystem(enemySystem);
        engine.addSystem(fireDamageSystem);
        engine.addSystem(freezingSystem);
        engine.addSystem(windSystem);
        engine.addSystem(uiManager);
    }

    private void createTree() {
        float width = Constants.WORLD_WIDTH * 1.3f;
        float height = width / 2f;

        Entity treeEntity = Utils.createEntityCenter(engine, atlas.findRegion("tree"),
                Constants.WORLD_WIDTH / 2f + 0.3f, 0.2f, width, height, DrawingPriorities.TREE
        );
        engine.addEntity(treeEntity);
    }

    private void createTiles(int numElements) {
        TextureRegion normalTile = atlas.findRegion("normal_tile");
        TextureRegion iceTile = atlas.findRegion("ice_tile");
        TextureRegion fireTile = atlas.findRegion("fire_tile");
        TextureRegion windTile = atlas.findRegion("wind_tile");
        if (numElements > Constants.NUM_LINES) {
            throw new IllegalArgumentException("Too many elements");
        }

        int[] randomLines = new int[Constants.NUM_LINES];
        for (int i = 0; i < Constants.NUM_LINES; i++) {
            randomLines[i] = i;
        }
        int[] randomLengths = new int[Constants.GREEN_LENGTH];
        for (int i = 0; i < Constants.GREEN_LENGTH; i++) {
            randomLengths[i] = i;
        }
        boolean[][] elementsPos = new boolean[Constants.NUM_LINES][Constants.GREEN_LENGTH];
        int lineAvailable = Constants.NUM_LINES;
        // find numElements random tiles from each column
        for (int i = 0; i < numElements; i++) {
            int randomLine = randomLines[random.nextInt(lineAvailable)];
            int randomLength = randomLengths[random.nextInt(randomLengths.length)];
            elementsPos[randomLine][randomLength] = true;
            randomLines[randomLine] = randomLines[lineAvailable - 1];
            lineAvailable--;
        }

        Element[] elements = new Element[numElements];
        for (int i = 0; i < numElements; i++) {
            int index = random.nextInt(3);
            elements[i] = Element.values()[index + 1]; // to ignore EARTH
        }


        createBackgrounds();

        TextureRegion thisRegion;
        Element thisElement;
        int elementIndex = 0;
        for (int i = 0; i < Constants.NUM_LINES; i++) {
            for (int j = 0; j < Constants.LINE_LENGTH; j++) {
                if (j < Constants.GREEN_LENGTH && elementsPos[i][j]) {
                    thisElement = elements[elementIndex];
                    switch (elements[elementIndex]) {
                        case ICE:
                            thisRegion = iceTile;
                            break;
                        case FIRE:
                            thisRegion = fireTile;
                            break;
                        case AIR:
                            thisRegion = windTile;
                            break;
                        default:
                            thisRegion = normalTile;
                    }
                    elementIndex++;
                } else {
                    thisElement = Element.EARTH;
                    thisRegion = normalTile;
                }

                Entity tileEntity =
                        Utils.createEntity(engine, thisRegion, Constants.PADDING_LEFT + i,
                                Constants.PADDING_BOTTOM + j, DrawingPriorities.TILES
                        );
                TileComponent tileComponent = new TileComponent();
                tileComponent.element = thisElement;
                if (j >= Constants.GREEN_LENGTH) {
                    tileComponent.isOccupied = true;
                }
                tileEntity.add(tileComponent);
                engine.addEntity(tileEntity);
            }
        }
    }

    private void createBackgrounds() {
        TextureRegion gray_bg = atlas.findRegion("gray_bg");
        TextureRegion green_bg = atlas.findRegion("green_bg");

        Entity entity = Utils.createEntity(engine, gray_bg, 0, 0, DrawingPriorities.BACKGROUND);
        entity.getComponent(TransformComponent.class)
                .setSize(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        engine.addEntity(entity);

        entity = Utils.createEntity(engine, green_bg, 0, 0, DrawingPriorities.BACKGROUND);
        entity.getComponent(TransformComponent.class)
                .setSize(Constants.WORLD_WIDTH, Constants.GREEN_LENGTH + Constants.PADDING_BOTTOM);
        engine.addEntity(entity);


    }

    private void createSeedTray() {
        TextureRegion bg_region = atlas.findRegion("normal_tile");
        TextureRegion plant_region = atlas.findRegion("green_plant");
        float bg_scale = 0.85f;
        float plant_scale = 0.6f;

        // find the bottom middle
        float middleX = Constants.WORLD_WIDTH / 2f;
        float middleY = .7f;

        Entity entity =
                Utils.createEntityCenter(engine, bg_region, middleX, middleY, bg_scale, bg_scale,
                        DrawingPriorities.SEED_TRAY
                );
        engine.addEntity(entity);

        entity = Utils.createEntityCenter(engine, plant_region, middleX, middleY, plant_scale,
                plant_scale, DrawingPriorities.SEED_TRAY + 1
        );
        ClickableComponent clickableComponent = new ClickableComponent();
        clickableComponent.containerScale = bg_scale / plant_scale;
        entity.add(clickableComponent);

        SeedComponent seedComponent = new SeedComponent();
        seedComponent.plantWidth = 0.8f;
        seedComponent.plantHeight = 0.8f;
        entity.add(seedComponent);

        engine.addEntity(entity);
    }

    @Override
    public void render(float delta) {
        engine.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        if (engine != null) {
            RenderingSystem system = engine.getSystem(RenderingSystem.class);
            if (system != null) system.resize(width, height);
        }
    }

    @Override
    public void dispose() {
    }
}

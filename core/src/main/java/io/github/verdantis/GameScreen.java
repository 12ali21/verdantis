package io.github.verdantis;

import static io.github.verdantis.systems.RenderingSystem.PPM;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.utils.viewport.FillViewport;

import java.util.Random;

import io.github.verdantis.components.ClickableComponent;
import io.github.verdantis.components.HealthComponent;
import io.github.verdantis.components.RootComponent;
import io.github.verdantis.components.SeedComponent;
import io.github.verdantis.components.TileComponent;
import io.github.verdantis.components.TransformComponent;
import io.github.verdantis.systems.AnimationSystem;
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
import io.github.verdantis.systems.RootsSystem;
import io.github.verdantis.systems.SeedSystem;
import io.github.verdantis.systems.ShootingSystem;
import io.github.verdantis.systems.MovementSystem;
import io.github.verdantis.systems.UIManager;
import io.github.verdantis.systems.WindSystem;
import io.github.verdantis.utils.AnimationFactory;
import io.github.verdantis.utils.Constants;
import io.github.verdantis.utils.DrawingPriorities;
import io.github.verdantis.utils.Element;
import io.github.verdantis.utils.Utils;

public class GameScreen extends ScreenAdapter {
    private Engine engine;
    private final Assets assets;
    private GameState gameState;
    private final Random random = new RandomXS128();
    private UIManager uiManager;


    public GameScreen(Assets assets) {
        this.assets = assets;
    }

    @Override
    public void show() {
        gameState = new GameState();
        gameState.registerCallback(this::handleGameStateChange);

        engine = new Engine();
        startGame();
    }

    private void startGame() {
        initializeSystems();

        createTree();
//        createRoots();
        createTiles(3);
        createSeedTray();

        Music music = assets.manager.get(Assets.GAME_MUSIC, Music.class);
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void handleGameStateChange(GameState.State state) {
        switch (state) {
            case RESTART:
                engine.removeAllEntities();
                engine.removeAllSystems();
                startGame();
                gameState.changeState(GameState.State.DEFAULT);
                break;
            case DEFEAT:
            case VICTORY:
                Music music = assets.manager.get(Assets.GAME_MUSIC, Music.class);
                music.stop();
                Gdx.input.setInputProcessor(uiManager.getStage());
                break;
            default:
                break;
        }
    }

    private void initializeSystems() {
        AnimationFactory animationFactory = new AnimationFactory(assets);

        RenderingSystem renderingSystem = new RenderingSystem();
        InputSystem inputSystem = new InputSystem(engine, gameState, renderingSystem.getCamera());
        Gdx.input.setInputProcessor(inputSystem);
        uiManager = new UIManager(assets, gameState, 2);
        ClickingSystem clickingSystem = new ClickingSystem(inputSystem);
        SeedSystem seedSystem = new SeedSystem(uiManager, inputSystem);
        DraggingSystem draggingSystem = new DraggingSystem(inputSystem);
        PlantingSystem plantingSystem = new PlantingSystem(uiManager, animationFactory, assets);
        ShootingSystem shootingSystem = new ShootingSystem(assets);
        MovementSystem movementSystem = new MovementSystem();
        BulletSystem bulletSystem = new BulletSystem(assets);
        EnemyManagerSystem enemyManagerSystem = new EnemyManagerSystem(assets, gameState, EnemyManagerSystem.GameLevel.LEVEL_1);
        EnemySystem enemySystem = new EnemySystem(uiManager, assets);
        // Element systems
        FireDamageSystem fireDamageSystem = new FireDamageSystem();
        FreezingSystem freezingSystem = new FreezingSystem();
        WindSystem windSystem = new WindSystem();
        RootsSystem rootsSystem = new RootsSystem(assets);
        AnimationSystem animationSystem = new AnimationSystem();

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
        engine.addSystem(rootsSystem);
        engine.addSystem(animationSystem);
    }

    private void createTree() {
        float width = Constants.WORLD_WIDTH * 1.3f;
        float height = width / 2f;

        Entity treeEntity =
                Utils.createEntityCenter(engine, assets.spritesAtlas().findRegion(Assets.TREE),
                        Constants.WORLD_WIDTH / 2f + 0.3f, 0.2f, width, height,
                        DrawingPriorities.TREE
                );
        engine.addEntity(treeEntity);
    }

    private void createRoots() {
        TextureRegion rootsRegion = getRootRegion();
        float width = 1;
        float height = Constants.WORLD_HEIGHT;

        for (int i = 0; i < Constants.NUM_LINES; i++) {
            Entity rootsEntity = Utils.createEntity(engine, rootsRegion,
                    Constants.PADDING_LEFT + i, 1.7f - height, width, height,
                    DrawingPriorities.ROOTS
            );

            HealthComponent healthComponent = new HealthComponent();
            healthComponent.setHealth(4);
            rootsEntity.add(healthComponent);

            RootComponent rootComponent = new RootComponent();
            rootsEntity.add(rootComponent);

            engine.addEntity(rootsEntity);
        }
    }

    // Create a long texture region made up of multiple root textures
    private TextureRegion getRootRegion() {
        TextureRegion endRegion = assets.spritesAtlas().findRegion(Assets.ROOT_END);
        TextureRegion middleRegion = assets.spritesAtlas().findRegion(Assets.ROOT_MIDDLE);

        // Set up a frame buffer to draw on
        FrameBuffer frameBuffer =
                new FrameBuffer(Pixmap.Format.RGBA8888, PPM, ((int) Constants.WORLD_HEIGHT) * PPM,
                        false
                );

        // Set up the viewport
        FillViewport viewport = new FillViewport(1, Constants.WORLD_HEIGHT);
        viewport.update(1, (int) Constants.WORLD_HEIGHT, true);
        SpriteBatch spriteBatch = new SpriteBatch();

        frameBuffer.begin();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        for (int i = 0; i < (int) Constants.WORLD_HEIGHT - 1; i++) {
            spriteBatch.draw(middleRegion, 0, i, 1, 1);
        }
        spriteBatch.draw(endRegion, 0, ((int) Constants.WORLD_HEIGHT) - 1f, 1, 1);

        spriteBatch.end();
        frameBuffer.end();
        spriteBatch.dispose();

        // Get the texture region from the frame buffer
        Texture combinedTexture = frameBuffer.getColorBufferTexture();
        TextureRegion combinedRegion =
                new TextureRegion(combinedTexture, 0, 0, combinedTexture.getWidth(),
                        (int) (combinedTexture.getHeight() - PPM * 0.65f)
                );
        combinedRegion.flip(false, true);

        return combinedRegion;
    }

    private void createTiles(int numElements) {
        TextureRegion normalTile = assets.spritesAtlas().findRegion(Assets.NORMAL_TILE);
        TextureRegion iceTile = assets.spritesAtlas().findRegion(Assets.ICE_TILE);
        TextureRegion fireTile = assets.spritesAtlas().findRegion(Assets.FIRE_TILE);
        TextureRegion windTile = assets.spritesAtlas().findRegion(Assets.WIND_TILE);
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
        TextureRegion gray_bg = assets.spritesAtlas().findRegion(Assets.GRAY_BG);
        TextureRegion green_bg = assets.spritesAtlas().findRegion(Assets.GREEN_BG);

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
        TextureRegion bg_region = assets.spritesAtlas().findRegion(Assets.NORMAL_TILE);
        TextureRegion plant_region = assets.spritesAtlas().findRegion(Assets.GREEN_PLANT);
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

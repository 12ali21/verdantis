package io.github.verdantis;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.verdantis.components.ClickableComponent;
import io.github.verdantis.components.GameState;
import io.github.verdantis.components.SeedComponent;
import io.github.verdantis.components.TileComponent;
import io.github.verdantis.components.TransformComponent;
import io.github.verdantis.systems.ClickingSystem;
import io.github.verdantis.systems.DraggingSystem;
import io.github.verdantis.systems.InputSystem;
import io.github.verdantis.systems.PlantingSystem;
import io.github.verdantis.systems.RenderingSystem;
import io.github.verdantis.systems.SeedSystem;
import io.github.verdantis.utils.Constants;
import io.github.verdantis.utils.Utils;

public class GameScreen extends ScreenAdapter {
    private Engine engine;
    private final AssetManager assets;
    private TextureAtlas atlas;
    private GameState gameState;


    public GameScreen(AssetManager assets) {
        this.assets = assets;
    }

    @Override
    public void show() {
        atlas = new TextureAtlas(Gdx.files.internal("sprites.atlas"));
        gameState = new GameState();

        engine = new Engine();
        RenderingSystem renderingSystem = new RenderingSystem();
        InputSystem inputSystem = new InputSystem(renderingSystem.getCamera());
        Gdx.input.setInputProcessor(inputSystem);
        ClickingSystem clickingSystem = new ClickingSystem(inputSystem);
        SeedSystem seedSystem = new SeedSystem(gameState);
        DraggingSystem draggingSystem = new DraggingSystem(inputSystem, gameState);
        PlantingSystem plantingSystem = new PlantingSystem();


        engine.addSystem(renderingSystem);
        engine.addSystem(inputSystem);
        engine.addSystem(clickingSystem);
        engine.addSystem(seedSystem);
        engine.addSystem(draggingSystem);
        engine.addSystem(plantingSystem);

        createTiles();
        createSeedTray();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void createTiles() {
        TextureRegion tile_region = atlas.findRegion("tile_border");

        int numLines = 5;
        int lineLength = 15;
        int greenLength = 3;

        float paddingLeft = (Constants.WORLD_WIDTH - numLines) / 2f;
        float paddingBottom = 1.5f;

        createBackgrounds(greenLength, paddingBottom);

        for (int i = 0; i < numLines; i++) {
            for (int j = 0; j < lineLength; j++) {
                Entity tileEntity = Utils.createEntity(engine, tile_region, paddingLeft + i,
                        paddingBottom + j, 0
                );
                TileComponent tileComponent = new TileComponent();
                if (j >= greenLength) {
                    tileComponent.isOccupied = true;
                }
                tileEntity.add(tileComponent);
                engine.addEntity(tileEntity);
            }
        }

    }

    private void createBackgrounds(int greenLength, float bottomPad) {
        TextureRegion gray_bg = atlas.findRegion("gray_bg");
        TextureRegion green_bg = atlas.findRegion("green_bg");

        Entity entity = Utils.createEntity(engine, gray_bg, 0,
                0, -2
        );
        entity.getComponent(TransformComponent.class).setSize(Constants.WORLD_WIDTH,
                Constants.WORLD_HEIGHT
        );
        engine.addEntity(entity);

        entity = Utils.createEntity(engine, green_bg, 0,
                0, -1
        );
        entity.getComponent(TransformComponent.class).setSize(Constants.WORLD_WIDTH,
                greenLength + bottomPad
        );
        engine.addEntity(entity);


    }

    private void createSeedTray() {
        TextureRegion bg_region = atlas.findRegion("tile_border");
        TextureRegion plant_region = atlas.findRegion("green_plant");
        float bg_scale = 0.85f;
        float plant_scale = 0.6f;

        // find the bottom middle
        float middleX = Constants.WORLD_WIDTH / 2f;
        float middleY = .7f;

        Entity entity = Utils.createEntity(engine, bg_region, 0, 0, 1);
        TransformComponent transform = entity.getComponent(TransformComponent.class);
        transform.setSize(bg_scale, bg_scale);
        transform.setCenter(middleX, middleY);

        engine.addEntity(entity);

        entity = Utils.createEntity(engine, plant_region, 0, 0, 2);
        transform = entity.getComponent(TransformComponent.class);
        transform.setSize(plant_scale, plant_scale);
        transform.setCenter(middleX, middleY);

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
        System.out.println(gameState.getState());
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

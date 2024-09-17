package io.github.verdantis;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.verdantis.components.TextureComponent;
import io.github.verdantis.components.TransformComponent;
import io.github.verdantis.systems.RenderingSystem;
import io.github.verdantis.utils.Constants;
import io.github.verdantis.utils.Mappers;

public class GameScreen extends ScreenAdapter {
    private Engine engine;
    private AssetManager assets;
    private TextureAtlas atlas;

    public GameScreen(AssetManager assets) {
        this.assets = assets;
    }

    @Override
    public void show() {
        atlas = new TextureAtlas(Gdx.files.internal("sprites.atlas"));

        engine = new Engine();
        engine.addSystem(new RenderingSystem());

        createTiles();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void createTiles() {
        TextureRegion tile_region = atlas.findRegion("tile_border");

        int numLines = 5;
        int lineLength = 15;
        int greenLength = 3;

        float paddingLeft = (Constants.WORLD_WIDTH - numLines) / 2f;
        float paddingBottom = 2.5f;

        createBackgrounds(greenLength, paddingBottom);

        for (int i = 0; i < numLines; i++) {
            for (int j = 0; j < lineLength; j++) {
                TextureComponent textureC = new TextureComponent();
                textureC.region = tile_region;

                TransformComponent transformC = new TransformComponent();
                transformC.position.set(paddingLeft + i + 0.5f, paddingBottom + j + 0.5f);
                transformC.z = 0;

                Entity tileEntity = engine.createEntity();
                tileEntity.add(textureC);
                tileEntity.add(transformC);
                engine.addEntity(tileEntity);
            }
        }

    }

    private void createBackgrounds(int greenLength, float bottomPad) {
        TextureRegion gray_bg = atlas.findRegion("gray_bg");
        TextureRegion green_bg = atlas.findRegion("green_bg");

        TextureComponent textureC = new TextureComponent();
        textureC.region = green_bg;
        TransformComponent transformC = new TransformComponent();
        transformC.position.set(Constants.WORLD_WIDTH / 2f, (greenLength + bottomPad) / 2f);
        transformC.scale.set(Constants.WORLD_WIDTH, greenLength + bottomPad);
        transformC.z = -1;
        Entity entity = engine.createEntity();
        entity.add(textureC);
        entity.add(transformC);
        engine.addEntity(entity);


        textureC = new TextureComponent();
        textureC.region = gray_bg;
        transformC = new TransformComponent();
        transformC.position.set(Constants.WORLD_WIDTH / 2f, Constants.WORLD_HEIGHT / 2f);
        transformC.z = -2;
        transformC.scale.set(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        entity = engine.createEntity();
        entity.add(textureC);
        entity.add(transformC);
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
            if (system != null)
                system.resize(width, height);
        }
    }

    @Override
    public void dispose() {
    }
}

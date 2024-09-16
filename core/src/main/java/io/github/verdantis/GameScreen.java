package io.github.verdantis;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import io.github.verdantis.components.TextureComponent;
import io.github.verdantis.components.TransformComponent;
import io.github.verdantis.systems.RenderingSystem;

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

        createTestEntity();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void createTestEntity() {
        Entity entity = engine.createEntity();

        TransformComponent transformC = new TransformComponent();
        transformC.position.set(2, 2);
        transformC.z = 1;
        TextureComponent textureC = new TextureComponent();
        textureC.region = atlas.findRegion("fire_plant");

        entity.add(transformC);
        entity.add(textureC);

        engine.addEntity(entity);

        Entity entity2 = engine.createEntity();

        TransformComponent transformC2 = new TransformComponent();
        transformC2.position.set(5f, 7.5f);
        transformC2.z = 0;
        transformC2.scale.set(10, 10);
        TextureComponent textureC2 = new TextureComponent();
        textureC2.region = atlas.findRegion("green_tile");

        entity2.add(transformC2);
        entity2.add(textureC2);

        engine.addEntity(entity2);
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

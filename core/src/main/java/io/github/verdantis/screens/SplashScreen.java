package io.github.verdantis.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.verdantis.Assets;
import io.github.verdantis.Main;

public class SplashScreen extends ScreenAdapter {
    private final Main main;
    private float timer = 1f;

    private final SpriteBatch batch = new SpriteBatch();
    private final Viewport viewport = new FitViewport(1080, 1920);
    private Texture texture;

    Assets assets;

    public SplashScreen(Main main) {
        this.main = main;
    }

    @Override
    public void show() {
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        texture = new Texture(Gdx.files.internal("libgdx.png"));
        assets = new Assets();
        assets.loadAssets();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1);

        batch.begin();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.draw(texture, viewport.getWorldWidth() / 2f - texture.getWidth() / 2f,
                viewport.getWorldHeight() / 2f - texture.getHeight() / 2f, texture.getWidth(), texture.getHeight());
        batch.end();

        timer -= delta;

        if (Gdx.input.justTouched()) {
            timer = 0;
        }
        if (timer <= 0) {
            if (assets.update()) {
                main.splashDone(assets);
            }
        }
    }

    @Override
    public void hide() {
        texture.dispose();
    }
}

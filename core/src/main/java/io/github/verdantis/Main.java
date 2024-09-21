package io.github.verdantis;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    private Assets assets;
    private Screen currentScreen;
    private boolean assetsLoaded = false;
    @Override
    public void create() {
        assets = new Assets();
        assets.loadAssets();
        currentScreen = new MenuScreen(() -> {
            currentScreen.dispose();
            currentScreen = new GameScreen(assets);
            currentScreen.show();
        }, assets);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0f, 0f, 0f, 1);
        float delta = Gdx.graphics.getDeltaTime();
        if (assets.update()) {
            if (!assetsLoaded) {
                currentScreen.show();
            }
            assetsLoaded = true;
            currentScreen.render(delta);
        } else {
            assetsLoaded = false;
        }
    }

    @Override
    public void resize(int width, int height) {
        if (currentScreen != null) {
            currentScreen.resize(width, height);
        }
    }

    @Override
    public void dispose() {
        assets.dispose();
    }
}

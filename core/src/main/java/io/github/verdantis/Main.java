package io.github.verdantis;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends Game {

    private Assets assets;

    @Override
    public void create() {
        setScreen(new SplashScreen(this));
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        screen.render(delta);
    }

    public void splashDone(Assets assets) {
        this.assets = assets;
        setScreen(new MenuScreen(this, assets));
    }

    @Override
    public void dispose() {
        if (assets != null)
            assets.dispose();
        super.dispose();
    }
}

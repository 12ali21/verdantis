package io.github.verdantis.screens;

import static io.github.verdantis.systems.UIManager.getBackground;
import static io.github.verdantis.systems.UIManager.getLabelStyle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.verdantis.Assets;

public class MenuScreen extends ScreenAdapter {

    private final Game game;
    private final Assets assets;
    private Stage stage;
    private Music music;

    public MenuScreen(Game game, Assets assets) {
        this.game = game;
        this.assets = assets;
    }

    @Override
    public void show() {
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        float w = 1080;
        float h = w * 16f / 9;

        stage = new Stage(new FitViewport(w, h));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table(skin);
        table.setFillParent(true);
        stage.addActor(table);

        Label gameName = new Label("Verdantis", getLabelStyle(150, "cinzel_black"));
        table.add(gameName).padBottom(480).row();

        table.defaults()
                .space(10f)
                .uniform()
                .fill()
                .maxWidth(512)
                .height(96)
                .growX();


        Label playLabel = new Label("Play", getLabelStyle(64, "cinzel_bold"));
        Button playButton = new Button(skin);
        playButton.add(playLabel);
        table.add(playButton).padBottom(20).row();

        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                music.stop();
                game.setScreen(new GameScreen(game, assets, false));
            }
        });

        Label endlessLabel = new Label("Endless", getLabelStyle(64, "cinzel_bold"));
        Button endlessButton = new Button(skin);
        endlessButton.add(endlessLabel);
        table.add(endlessButton).padBottom(20).row();

        endlessButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                music.stop();
                game.setScreen(new GameScreen(game, assets, true));
            }
        });

        Label creditsLabel = new Label("Credits", getLabelStyle(64, "cinzel_bold"));
        Button creditsButton = new Button(skin);
        creditsButton.add(creditsLabel);
        table.add(creditsButton).padBottom(20).row();

        creditsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new CreditsScreen(game, assets));
            }
        });


        table.defaults().reset();

        music = assets.manager.get(Assets.MAIN_MENU_MUSIC, Music.class);
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(new Color(0x007f09ff));
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        if (stage == null) {
            return;
        }
        stage.getViewport().update(width, height, true);
    }

    public InputProcessor getInputProcessor() {
        return stage;
    }

    @Override
    public void dispose() {
        stage.dispose();
        music.stop();
    }
}

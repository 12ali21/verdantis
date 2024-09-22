package io.github.verdantis.screens;

import static io.github.verdantis.systems.UIManager.getLabelStyle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.verdantis.Assets;

public class CreditsScreen extends ScreenAdapter {
    private final Game game;
    private final Assets assets;
    private Stage stage;

    public CreditsScreen(Game game, Assets assets) {
        this.game = game;
        this.assets = assets;
    }

    @Override
    public void show() {
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        stage = new Stage(new FitViewport(1080/2f, 1920/2f));
        Gdx.input.setInputProcessor(stage);

        Table top = new Table();
        top.setFillParent(true);
        stage.addActor(top);

        Label gameName = new Label("Verdantis", getLabelStyle(24, "cinzel_black"));
        top.add(gameName).expand().maxHeight(60).top().pad(64).row();

        String assetUsed = Gdx.files.internal("attributions.txt").readString();

        Label credits = new Label("Developed And Designed by: \n\n"
                + "Ali (vexpl) \n\n\n\n"
                + "Assets used: \n\n"
                + assetUsed, getLabelStyle(12, "cinzel_bold"));
        credits.setWrap(true);
        top.add(credits).grow().top().pad(64).row();

        Label backLabel = new Label("Back", getLabelStyle(16, "cinzel_bold"));
        Button button = new Button(skin);
        button.add(backLabel);
        top.add(button);


        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
                    game.setScreen(new MenuScreen(game, assets));
                    return true;
                }
                return false;
            }
        });
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game, assets));
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1);

        stage.act();
        stage.draw();
    }
}

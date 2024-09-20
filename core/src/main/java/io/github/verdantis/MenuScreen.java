package io.github.verdantis;

import static io.github.verdantis.systems.UIManager.getBackground;
import static io.github.verdantis.systems.UIManager.getLabelStyle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MenuScreen extends ScreenAdapter {

    private final Runnable onPlay;
    private Stage stage;

    public MenuScreen(Runnable onPlay) {
        this.onPlay = onPlay;
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
        table.setBackground(getBackground(new Color(0x007f09ff)));
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
                onPlay.run();
            }
        });


        table.defaults().reset();
    }


    @Override
    public void render(float delta) {
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
    }
}

package io.github.verdantis.screens;

import static io.github.verdantis.systems.UIManager.getLabelStyle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.verdantis.Assets;

public class MenuScreen extends ScreenAdapter {

    private final Game game;
    private final Assets assets;
    private Stage stage;
    private Stack stack;
    private Music music;
    private Table howToPlayTable;
    private Skin skin;
    private Table top;

    public MenuScreen(Game game, Assets assets) {
        this.game = game;
        this.assets = assets;
    }

    @Override
    public void show() {
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        float w = 1080;
        float h = w * 16f / 9;

        stage = new Stage(new FitViewport(w, h));
        Gdx.input.setInputProcessor(stage);

        stack = new Stack();
        stack.setFillParent(true);
        stage.addActor(stack);

        Texture bg = new Texture(Gdx.files.internal("tree_bg.png"));
        Image background = new Image(bg);
        background.setScaling(Scaling.fill);
        stack.add(background);

        top = new Table(skin);
        stack.add(top);

        Label gameName = new Label("Verdantis", getLabelStyle(150, "cinzel_black"));
        top.add(gameName).padBottom(480).row();

        top.defaults()
                .space(10f)
                .uniform()
                .fill()
                .maxWidth(512)
                .height(96)
                .growX();


        Label playLabel = new Label("Play", getLabelStyle(64, "cinzel_bold"));
        Button playButton = new Button(skin);
        playButton.add(playLabel);
        top.add(playButton).padBottom(20).row();

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
        top.add(endlessButton).padBottom(20).row();

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
        top.add(creditsButton).padBottom(20).row();

        creditsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new CreditsScreen(game, assets));
            }
        });

        Label howToPlayLabel = new Label("How to play", getLabelStyle(64, "cinzel_bold"));
        Button howToPlayButton = new Button(skin);
        howToPlayButton.add(howToPlayLabel);
        top.add(howToPlayButton).padBottom(20).row();

        howToPlayButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showHowToPlay(true);
            }
        });


        top.defaults().reset();

        music = assets.manager.get(Assets.MAIN_MENU_MUSIC, Music.class);
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();
    }


    private void showHowToPlay(boolean show) {
        if (show) {
            top.setVisible(false);
            howToPlayTable = howToPlayTable();
            stack.add(howToPlayTable);
        } else {
            top.setVisible(true);
            howToPlayTable.remove();
        }
    }
    private Table howToPlayTable() {
        Table table = new Table();
        table.defaults().space(10f).fill().pad(0, 64, 20, 64).expandX();

        Label howToPlayLabel = new Label("How to Play", getLabelStyle(64, "cinzel_bold"));
        table.add(howToPlayLabel).row();

        Label howToPlayText = new Label("Drag the canon to a tile to plant it. " +
                "The canon costs 1 soul. \n" +
                "Depending on the tile, the plant could receive elemental powers, use this to your advantage. " +
                "Defend the ancient tree from the invaders.",
                getLabelStyle(32, "cinzel_bold")
        );
        howToPlayText.setWrap(true);

        table.add(howToPlayText).row();

        Label elementMechanicsLabel =
                new Label("Use the elements wheel to apply new elements to tiles. It costs 5 souls",
                        getLabelStyle(32, "cinzel_bold")
                );
        elementMechanicsLabel.setWrap(true);

        table.add(elementMechanicsLabel).row();

        TextureRegion region = assets.spritesAtlas().findRegion(Assets.ELEMENTS_WHEEL);
        Image image = new Image(region);
        image.setScaling(Scaling.fit);
        table.add(image).row();

        Label goodLuckLabel = new Label("Good Luck!", getLabelStyle(64, "cinzel_bold"));
        table.add(goodLuckLabel).row();

        Label backLabel = new Label("Back", getLabelStyle(64, "cinzel_bold"));
        Button backButton = new Button(skin);
        backButton.add(backLabel);
        table.add(backButton).size(512, 128).row();

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showHowToPlay(false);
            }
        });

        return table;
    }

    @Override
    public void render(float delta) {
//        ScreenUtils.clear(new Color(0x007f09ff));
        ScreenUtils.clear(Color.BLACK);
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

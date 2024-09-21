package io.github.verdantis.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.verdantis.Assets;
import io.github.verdantis.GameState;
import io.github.verdantis.utils.UpdatesWhenPaused;

public class UIManager extends EntitySystem implements UpdatesWhenPaused {
    private final Table pauseTable;
    private final Stack stack;
    private final GameState gameState;
    private final Stage stage;
    private Table table;
    private Image soulIcon;
    private Label soulLabel;
    private int soulAmount;

    private Skin skin;

    public UIManager(Assets assets, GameState gameState, int initialSoulAmount) {
        this.gameState = gameState;
        // register victory and defeat listeners
        gameState.registerCallback(this::handleGameStateChange);

        // Set up stage and table layout
        float w = 1080;
        float h = w * 16f / 9;
        stage = new Stage(new FitViewport(w, h));
        stack = new Stack();
        stack.setFillParent(true);
        stage.addActor(stack);

        table = new Table();
        table.setFillParent(true);
        table.bottom().left();
        stack.add(table);
        pauseTable = initPauseTable();

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        soulIcon = new Image(assets.spritesAtlas().findRegion(Assets.SOUL_ORB));

        soulAmount = initialSoulAmount;
        soulLabel = new Label("" + soulAmount, getLabelStyle(48, "cinzel_bold"));


        table.add(soulIcon).size(84, 84).padLeft(10).padTop(10);
        table.add(soulLabel).padLeft(10).padTop(10);

    }

    private void handleGameStateChange(GameState.State state) {
        switch (state) {
            case VICTORY:
                showVictory();
                break;
            case DEFEAT:
                showDefeat();
                break;
            default:
                break;
        }
    }

    private void showVictory() {
        stack.add(initVictoryTable());
    }

    private void showDefeat() {
        stack.add(initDefeatTable());
    }

    private Table initVictoryTable() {
        Table table = new Table();
        table.setBackground(getBackground(new Color(0.1f, 0.1f, 0.1f, 0.5f)));
        table.setFillParent(true);
        table.center();

        Label label = new Label("Victory", getLabelStyle(72, "cinzel_bold"));
        label.setColor(Color.GREEN);
        table.add(label);

        return table;
    }

    private Table initDefeatTable() {
        Table table = new Table();
        table.setBackground(getBackground(new Color(0.1f, 0.1f, 0.1f, 0.5f)));
        table.setFillParent(true);
        table.center();

        Label defeatLabel = new Label("Defeat", getLabelStyle(72, "cinzel_bold"));
        defeatLabel.setColor(Color.RED);
        table.add(defeatLabel).row();

        Label restartLabel = new Label("Restart Level", getLabelStyle(36, "cinzel_bold"));
        Button restartButton = new Button(skin);
        restartButton.add(restartLabel);
        table.add(restartButton).fill().maxWidth(512).height(96).growX();

        restartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameState.changeState(GameState.State.RESTART);
            }
        });
        return table;
    }

    private Table initPauseTable() {
        Table pauseTable = new Table();
        pauseTable.setBackground(getBackground(new Color(0.1f, 0.1f, 0.1f, 0.5f)));
        pauseTable.setFillParent(true);
        pauseTable.center();

        Label pauseLabel = new Label("Paused", getLabelStyle(72, "cinzel_bold"));
        pauseTable.add(pauseLabel);

        return pauseTable;
    }

    public static Label.LabelStyle getLabelStyle(int fontSize, String fontName) {
        FreeTypeFontGenerator generator =
                new FreeTypeFontGenerator(Gdx.files.internal("ui/" + fontName + ".ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = fontSize;
        BitmapFont font = generator.generateFont(parameter);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        return labelStyle;
    }

    public static Drawable getBackground(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        return new TextureRegionDrawable(texture);
    }

    @Override
    public void update(float deltaTime) {
        stage.act(deltaTime);
        stage.draw();
    }

    public void changeSoulAmount(int amount) {
        soulAmount += amount;
        soulLabel.setText(String.valueOf(soulAmount));
    }

    public int getSoulAmount() {
        return soulAmount;
    }

    @Override
    public void onPauseChange(boolean paused) {
        if (paused) {
            stack.add(pauseTable);
        } else {
            pauseTable.remove();
        }
    }

    public Stage getStage() {
        return stage;
    }
}


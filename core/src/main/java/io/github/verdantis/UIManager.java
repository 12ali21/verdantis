package io.github.verdantis;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class UIManager extends EntitySystem {
    private Stage stage;
    private Table table;
    private Image soulIcon;
    private Label soulLabel;
    private int soulAmount;

    private Skin skin;

    public UIManager(TextureAtlas atlas, int initialSoulAmount) {
        // Set up stage and table layout
        stage = new Stage(new ScreenViewport());
        table = new Table();
        table.setFillParent(true);
        table.bottom().left();

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        soulIcon = new Image(atlas.findRegion("soul_orb"));

        soulAmount = initialSoulAmount;
        soulLabel = new Label("" + soulAmount, skin);

        table.add(soulIcon).size(32, 32).padLeft(10).padTop(10);
        table.add(soulLabel).padLeft(10).padTop(10);

        stage.addActor(table);
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
}


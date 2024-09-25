package io.github.verdantis.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Comparator;
import java.util.Map;

import io.github.verdantis.components.TextureComponent;
import io.github.verdantis.components.TransformComponent;
import io.github.verdantis.utils.Constants;
import io.github.verdantis.utils.Mappers;
import io.github.verdantis.utils.UpdatesWhenPaused;

public class RenderingSystem extends SortedIteratingSystem implements UpdatesWhenPaused {
    public static final int PPM = 256; // Pixel per meters
    private final Camera camera;
    private final FitViewport viewport;
    private final SpriteBatch batch;

    public RenderingSystem() {
        super(Family.all(TextureComponent.class, TransformComponent.class).get(), (e1, e2) -> {
            int z1 = Mappers.transform.get(e1).z;
            int z2 = Mappers.transform.get(e2).z;
            return z1 - z2;
        });
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        camera = viewport.getCamera();
        batch = new SpriteBatch();
    }

    @Override
    public void update(float deltaTime) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        super.update(deltaTime);
        batch.end();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transform = Mappers.transform.get(entity);
        TextureComponent textureComponent = Mappers.texture.get(entity);
        TextureRegion region = textureComponent.region;
        if (region == null) {
            return;
        }
        float width = transform.width;
        float height = transform.height;

        float originX = width / 2;
        float originY = height / 2;

        if (textureComponent.color != Color.WHITE) {
            batch.setColor(textureComponent.color);
        }
        batch.draw(region, transform.position.x, transform.position.y,
                originX, originY,
                width, height,
                textureComponent.textureScale, textureComponent.textureScale, transform.rotation
        );

        // reset batch color
        batch.setColor(Color.WHITE);
    }

    public void resize(int w, int h) {
        viewport.update(w, h, true);
    }

    public Camera getCamera() {
        return camera;
    }

    @Override
    public void onPauseChange(boolean paused) {
        // nothing
    }

    public FitViewport getViewport() {
        return viewport;
    }
}

package io.github.verdantis.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.Camera;
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
import io.github.verdantis.utils.Mappers;

public class RenderingSystem extends SortedIteratingSystem {
    private static final int PPM = 256; // Pixel per meters
    private final Camera camera;
    private final Viewport viewport;
    private final SpriteBatch batch;

    public RenderingSystem() {
        super(Family.all(TextureComponent.class, TransformComponent.class).get(), (e1, e2) -> {
            int z1 = Mappers.transform.get(e1).z;
            int z2 = Mappers.transform.get(e2).z;
            return z1 - z2;
        });
        viewport = new FitViewport(9, 16);
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
        TextureRegion region = Mappers.texture.get(entity).region;
        if (region == null) {
            return;
        }
        float width = region.getRegionWidth();
        float height = region.getRegionHeight();

        float originX = width / 2;
        float originY = height / 2;
        batch.draw(region, transform.position.x - originX, transform.position.y - originY,
            originX, originY,
            width, height,
            transform.scale.x / PPM, transform.scale.y / PPM, transform.rotation);
    }

    public void resize(int w, int h) {
        viewport.update(w, h, true);
    }

    public Camera getCamera() {
        return camera;
    }
}

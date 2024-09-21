package io.github.verdantis.utils;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.github.verdantis.components.TextureComponent;
import io.github.verdantis.components.TransformComponent;

public class Utils {

    public static Entity createEntityCenter(Engine engine, TextureRegion region, float x, float y,
            int z
    ) {
        Entity entity = engine.createEntity();
        TextureComponent textureComponent = new TextureComponent();
        textureComponent.region = region;
        TransformComponent transformComponent = new TransformComponent();
        transformComponent.setCenter(x, y);
        transformComponent.z = z;
        entity.add(textureComponent);
        entity.add(transformComponent);
        return entity;
    }

    public static Entity createEntityCenter(Engine engine, TextureRegion region, float x, float y,
            float width, float height, int z
    ) {
        Entity entity = engine.createEntity();
        TextureComponent textureComponent = new TextureComponent();
        textureComponent.region = region;
        TransformComponent transformComponent = new TransformComponent();
        transformComponent.setSize(width, height);
        transformComponent.setCenter(x, y);
        transformComponent.z = z;
        entity.add(textureComponent);
        entity.add(transformComponent);
        return entity;
    }

    public static Entity createEntity(Engine engine, TextureRegion region, float x, float y, int z
    ) {
        Entity entity = engine.createEntity();
        TextureComponent textureComponent = new TextureComponent();
        textureComponent.region = region;
        TransformComponent transformComponent = new TransformComponent();
        transformComponent.position.set(x, y);
        transformComponent.z = z;
        entity.add(textureComponent);
        entity.add(transformComponent);
        return entity;
    }

    public static Entity createEntity(Engine engine, TextureRegion region, float x, float y,
            float width, float height, int z
    ) {
        Entity entity = engine.createEntity();
        TextureComponent textureComponent = new TextureComponent();
        textureComponent.region = region;
        TransformComponent transformComponent = new TransformComponent();
        transformComponent.setSize(width, height);
        transformComponent.position.set(x, y);
        transformComponent.z = z;
        entity.add(textureComponent);
        entity.add(transformComponent);
        return entity;
    }

    public static Element getRandomElement(boolean includeEarth) {
        Element[] elements = Element.values();
        int start = includeEarth ? 0 : 1;
        return elements[MathUtils.random(start, elements.length - 1)];
    }

}

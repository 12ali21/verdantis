package io.github.verdantis.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class TransformComponent implements Component {
    public final Vector2 position = new Vector2();
    public float width = 1, height = 1;
    public float rotation = 0f;
    public int z;

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public void setCenter(float x, float y) {
        position.set(x - width / 2, y - height / 2);
    }
}

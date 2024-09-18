package io.github.verdantis.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class TransformComponent implements Component {
    private static Rectangle rect = new Rectangle();
    private static Vector2 tmp2 = new Vector2();
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
    public void setCenter(Vector2 center) {
        setCenter(center.x, center.y);
    }

    public Rectangle getRect() {
        rect.set(position.x, position.y, width, height);
        return rect;
    }

    public Vector2 getCenter() {
        return tmp2.set(position.x + width / 2, position.y + height / 2);
    }
}

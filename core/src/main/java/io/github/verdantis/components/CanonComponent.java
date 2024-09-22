package io.github.verdantis.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

import io.github.verdantis.utils.Element;

public class CanonComponent implements Component {
    private Element element = Element.EARTH;
    public float bulletSpeed = 1f;
    public float bulletDamage = 1f;
    public float bulletCooldown = 1f;
    public float bulletTimer = 0f;

    public Vector2 bulletOffset = new Vector2();

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
        switch (element) {
            case ICE:
                this.bulletDamage = 1.2f;
                break;

            case FIRE:
                this.bulletDamage = 1.5f;
                break;

            default:
                this.bulletDamage = 1f;
        }
    }
}

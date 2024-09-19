package io.github.verdantis.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

import io.github.verdantis.utils.Element;

public class CanonComponent implements Component {
    public Element bulletType = Element.EARTH;
    public float bulletSpeed = 1f;
    public float bulletDamage = 1f;
    public float bulletCooldown = 1f;
    public float bulletTimer = 1f;

    public Vector2 bulletOffset = new Vector2();
}

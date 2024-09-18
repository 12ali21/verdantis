package io.github.verdantis.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class CanonComponent implements Component {
    public BulletType bulletType = BulletType.EARTH;
    public float bulletSpeed = 1f;
    public float bulletDamage = 1f;
    public float bulletCooldown = 1f;
    public float bulletTimer = 1f;

    public Vector2 bulletOffset = new Vector2();

    public static enum BulletType {
        EARTH, FIRE, ICE, AIR
    }
}

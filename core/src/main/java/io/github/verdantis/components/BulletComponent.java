package io.github.verdantis.components;

import com.badlogic.ashley.core.Component;

public class BulletComponent implements Component {
    public float damage = 1f;
    public CanonComponent.BulletType bulletType = CanonComponent.BulletType.EARTH;
}

package io.github.verdantis.components;

import com.badlogic.ashley.core.Component;

import io.github.verdantis.utils.Element;

public class BulletComponent implements Component {
    public float damage = 1f;
    public Element bulletType = Element.EARTH;
}

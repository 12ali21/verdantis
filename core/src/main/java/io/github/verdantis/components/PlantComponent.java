package io.github.verdantis.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class PlantComponent implements Component {
    public Entity occupyingTile = null;
    public boolean isPlanted = false;
    public float health = 1f;
    public int soulCost = 1;
}

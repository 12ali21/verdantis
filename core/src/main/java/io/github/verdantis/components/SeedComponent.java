package io.github.verdantis.components;

import com.badlogic.ashley.core.Component;

/**
 * represents a seed entity on the seed tray.
 */
public class SeedComponent implements Component {
    public float plantWidth = 1f, plantHeight = 1f;
    public int soulCost = 1;
}

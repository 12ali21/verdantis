package io.github.verdantis.components;

import com.badlogic.ashley.core.Component;

import io.github.verdantis.utils.Element;

public class TileComponent implements Component {
    public boolean isOccupied = false;
    public Element element;
}

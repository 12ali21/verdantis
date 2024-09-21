package io.github.verdantis.components;

import com.badlogic.ashley.core.Component;

import io.github.verdantis.utils.Element;

public class TileComponent implements Component {
    public boolean plantable = true;
    public boolean isOccupied = false;
    public boolean elementChanged = false;
    private Element element;

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
        elementChanged = true;
    }
}

package io.github.verdantis.utils;

import com.badlogic.ashley.core.ComponentMapper;

import io.github.verdantis.components.ClickableComponent;
import io.github.verdantis.components.DraggableComponent;
import io.github.verdantis.components.TextureComponent;
import io.github.verdantis.components.TransformComponent;

public class Mappers {
    public static final ComponentMapper<TextureComponent> texture =
            ComponentMapper.getFor(TextureComponent.class);
    public static final ComponentMapper<TransformComponent> transform =
            ComponentMapper.getFor(TransformComponent.class);
    public static final ComponentMapper<ClickableComponent> clickable = ComponentMapper.getFor(
            ClickableComponent.class);
    public static final ComponentMapper<DraggableComponent> draggable = ComponentMapper.getFor(
            DraggableComponent.class);
}

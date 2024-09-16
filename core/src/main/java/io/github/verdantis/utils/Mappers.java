package io.github.verdantis.utils;

import com.badlogic.ashley.core.ComponentMapper;

import io.github.verdantis.components.TextureComponent;
import io.github.verdantis.components.TransformComponent;

public class Mappers {
    public static final ComponentMapper<TextureComponent> texture = ComponentMapper.getFor(TextureComponent.class);
    public static final ComponentMapper<TransformComponent> transform = ComponentMapper.getFor(TransformComponent.class);
}

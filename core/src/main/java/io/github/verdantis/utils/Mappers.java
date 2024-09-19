package io.github.verdantis.utils;

import com.badlogic.ashley.core.ComponentMapper;

import io.github.verdantis.components.BulletComponent;
import io.github.verdantis.components.CanonComponent;
import io.github.verdantis.components.ClickableComponent;
import io.github.verdantis.components.DraggableComponent;
import io.github.verdantis.components.EnemyComponent;
import io.github.verdantis.components.FreezingComponent;
import io.github.verdantis.components.OnFireComponent;
import io.github.verdantis.components.PlantComponent;
import io.github.verdantis.components.SeedComponent;
import io.github.verdantis.components.TextureComponent;
import io.github.verdantis.components.TileComponent;
import io.github.verdantis.components.TransformComponent;
import io.github.verdantis.components.MovementComponent;
import io.github.verdantis.components.WindComponent;

public class Mappers {
    public static final ComponentMapper<TextureComponent> texture =
            ComponentMapper.getFor(TextureComponent.class);
    public static final ComponentMapper<TransformComponent> transform =
            ComponentMapper.getFor(TransformComponent.class);
    public static final ComponentMapper<ClickableComponent> clickable = ComponentMapper.getFor(
            ClickableComponent.class);
    public static final ComponentMapper<DraggableComponent> draggable = ComponentMapper.getFor(
            DraggableComponent.class);

    public static final ComponentMapper<PlantComponent> plantable = ComponentMapper.getFor(
            PlantComponent.class);

    public static final ComponentMapper<TileComponent> tile = ComponentMapper.getFor(
            TileComponent.class);

    public static final ComponentMapper<SeedComponent> seed = ComponentMapper.getFor(
            SeedComponent.class);

    public static final ComponentMapper<CanonComponent> canon = ComponentMapper.getFor(
            CanonComponent.class);

    public static final ComponentMapper<MovementComponent> movement = ComponentMapper.getFor(
            MovementComponent.class);

    public static final ComponentMapper<EnemyComponent> enemy = ComponentMapper.getFor(
            EnemyComponent.class);

    public static final ComponentMapper<BulletComponent> bullet = ComponentMapper.getFor(
            BulletComponent.class);


    public static final ComponentMapper<PlantComponent> plant = ComponentMapper.getFor(
            PlantComponent.class);


    public static final ComponentMapper<OnFireComponent> onFire = ComponentMapper.getFor(
            OnFireComponent.class);

    public static final ComponentMapper<FreezingComponent> freezing = ComponentMapper.getFor(
            FreezingComponent.class);

    public static final ComponentMapper<WindComponent> wind = ComponentMapper.getFor(
            WindComponent.class);
}

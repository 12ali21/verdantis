package io.github.verdantis.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.verdantis.Assets;
import io.github.verdantis.components.TextureComponent;
import io.github.verdantis.components.TileComponent;
import io.github.verdantis.utils.Mappers;

public class TileSystem extends IteratingSystem {

    private TextureRegion iceTile;
    private TextureRegion fireTile;
    private TextureRegion windTile;
    private TextureRegion normalTile;

    public TileSystem(Assets assets) {
        super(Family.all(TileComponent.class).get());

        TextureAtlas atlas = assets.spritesAtlas();
        iceTile = atlas.findRegion(Assets.ICE_TILE);
        fireTile = atlas.findRegion(Assets.FIRE_TILE);
        windTile = atlas.findRegion(Assets.WIND_TILE);
        normalTile = atlas.findRegion(Assets.NORMAL_TILE);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TileComponent tileComponent = Mappers.tile.get(entity);
        if (tileComponent.elementChanged) {
            TextureComponent textureComponent = Mappers.texture.get(entity);
            switch (tileComponent.getElement()) {
                case ICE:
                    textureComponent.region = iceTile;
                    break;
                case FIRE:
                    textureComponent.region = fireTile;
                    break;
                case AIR:
                    textureComponent.region = windTile;
                    break;
                default:
                    textureComponent.region = normalTile;
            }
            tileComponent.elementChanged = false;
        }
    }
}

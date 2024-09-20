package io.github.verdantis;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable {
    public static final String AIR_PLANT = "air_plant";
    public static final String DIRT_BULLET = "dirt_bullet";
    public static final String FIRE_BULLET = "fire_bullet";
    public static final String FIRE_PLANT = "fire_plant";
    public static final String FIRE_TILE = "fire_tile";
    public static final String GRAY_BG = "gray_bg";
    public static final String GREEN_BG = "green_bg";
    public static final String GREEN_PLANT = "green_plant";
    public static final String GREEN_SLIME = "green_slime";
    public static final String ICE_BULLET = "ice_bullet";
    public static final String ICE_PLANT = "ice_plant";
    public static final String ICE_TILE = "ice_tile";
    public static final String NORMAL_TILE = "normal_tile";
    public static final String PARTICLE = "particle";
    public static final String RED_SLIME = "red_slime";
    public static final String ROOT_END = "root_end";
    public static final String ROOT_MIDDLE = "root_middle";
    public static final String SOUL_ORB = "soul_orb";
    public static final String TREE = "tree";
    public static final String WIND_BULLET = "wind_bullet";
    public static final String WIND_TILE = "wind_tile";
    public static final String YELLOW_SLIME = "yellow_slime";
    public static final String FIRE_SHOOTING = "fire_shoot";
    public static final String GREEN_SHOOTING = "green_shoot";
    public static final String AIR_SHOOTING = "air_shoot";
    public static final String ICE_SHOOTING = "ice_shoot";


    private static final String SPRITES_ATLAS = "sprites.atlas";
    private static final String CANON_SHOOTING_ATLAS = "animations/canon_shooting.atlas";

    private final AssetManager assetManager;
    public Assets() {
        assetManager = new AssetManager();
    }


    public void loadAssets() {
        assetManager.load(SPRITES_ATLAS, TextureAtlas.class);
        assetManager.load(CANON_SHOOTING_ATLAS, TextureAtlas.class);
    }

    public boolean update() {
        return assetManager.update();
    }

    public TextureAtlas spritesAtlas() {
        return assetManager.get(SPRITES_ATLAS);
    }

    public TextureAtlas canonShootingAtlas() {
        return assetManager.get(CANON_SHOOTING_ATLAS);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}

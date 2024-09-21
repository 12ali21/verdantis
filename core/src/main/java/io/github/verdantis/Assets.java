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

    // animations
    public static final String FIRE_SHOOTING = "fire_shoot";
    public static final String GREEN_SHOOTING = "green_shoot";
    public static final String AIR_SHOOTING = "air_shoot";
    public static final String ICE_SHOOTING = "ice_shoot";
    public static final String GUST_EFFECT = "gust";
    public static final String FIRE_EFFECT = "on_fire";
    public static final String TRAPPED_EFFECT = "root_trapped";

    public static final String FIRE_SFX = "audio/sfx/fire.wav";
    public static final String FREEZE_SFX = "audio/sfx/freeze.wav";
    public static final String WIND_SFX = "audio/sfx/wind.wav";
    public static final String PLANTING_SFX = "audio/sfx/planting.wav";
    public static final String ROOTS_EXPANDING_SFX = "audio/sfx/roots_expanding.wav";
    public static final String SLIME_DEATH_SFX = "audio/sfx/slime_death.wav";
    public static final String SLIME_DAMAGE_SFX = "audio/sfx/slime_damage.wav";
    public static final String SLIME_ATTACK_SFX = "audio/sfx/slime_attack.wav";
    public static final String VICTORY_SFX = "audio/sfx/victory.wav";
    public static final String DEFEAT_SFX = "audio/sfx/game_over.wav";

    public static final String MAIN_MENU_MUSIC = "audio/music/dark_clouds.mp3";
    public static final String GAME_MUSIC = "audio/music/constance.mp3";

    private static final String SPRITES_ATLAS = "sprites.atlas";
    private static final String CANON_SHOOTING_ATLAS = "animations/canon_shooting.atlas";
    private static final String EFFECTS_ATLAS = "animations/effects.atlas";

    public final AssetManager manager;
    public Assets() {
        manager = new AssetManager();
    }


    public void loadAssets() {
        manager.load(SPRITES_ATLAS, TextureAtlas.class);
        manager.load(CANON_SHOOTING_ATLAS, TextureAtlas.class);
        manager.load(EFFECTS_ATLAS, TextureAtlas.class);

        // load sfx and music
        manager.load(FIRE_SFX, com.badlogic.gdx.audio.Sound.class);
        manager.load(FREEZE_SFX, com.badlogic.gdx.audio.Sound.class);
        manager.load(WIND_SFX, com.badlogic.gdx.audio.Sound.class);
        manager.load(PLANTING_SFX, com.badlogic.gdx.audio.Sound.class);
        manager.load(ROOTS_EXPANDING_SFX, com.badlogic.gdx.audio.Sound.class);
        manager.load(SLIME_DEATH_SFX, com.badlogic.gdx.audio.Sound.class);
        manager.load(SLIME_DAMAGE_SFX, com.badlogic.gdx.audio.Sound.class);
        manager.load(SLIME_ATTACK_SFX, com.badlogic.gdx.audio.Sound.class);
        manager.load(VICTORY_SFX, com.badlogic.gdx.audio.Sound.class);
        manager.load(DEFEAT_SFX, com.badlogic.gdx.audio.Sound.class);

        manager.load(MAIN_MENU_MUSIC, com.badlogic.gdx.audio.Music.class);
        manager.load(GAME_MUSIC, com.badlogic.gdx.audio.Music.class);
    }

    public boolean update() {
        return manager.update();
    }

    public TextureAtlas spritesAtlas() {
        return manager.get(SPRITES_ATLAS);
    }

    public TextureAtlas canonShootingAtlas() {
        return manager.get(CANON_SHOOTING_ATLAS);
    }

    public TextureAtlas effectsAtlas() {
        return manager.get(EFFECTS_ATLAS);
    }

    @Override
    public void dispose() {
        manager.dispose();
    }
}

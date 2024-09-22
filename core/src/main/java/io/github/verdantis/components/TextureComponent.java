package io.github.verdantis.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureComponent implements Component {
    public static final Color FIRE_COLOR = new Color(1f, 1f, 1f, 0.2f);
    public static final Color BLUE_FIRE_COLOR = new Color(0.15f, 0.2f, 0.6f, 0.3f);
    public static final Color FROZEN_COLOR = new Color(0x23b0ffff);;
    public static final Color WIND_COLOR = new Color(1, 1, 1, 0.5f);;

    public TextureRegion region = null;
    public Color color = Color.WHITE;
    public float textureScale = 1f;
}

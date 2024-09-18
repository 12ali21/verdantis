package io.github.verdantis.utils;

public class Constants {


    public static final float WORLD_WIDTH = 6f;
    public static final float WORLD_HEIGHT = WORLD_WIDTH * 16 / 9;
    public static final int NUM_LINES = 5;
    public static final float PADDING_BOTTOM = 1.5f;
    public static final int LINE_LENGTH = (int) (WORLD_HEIGHT - PADDING_BOTTOM + 1f);

    public static final float PADDING_LEFT = (Constants.WORLD_WIDTH - Constants.NUM_LINES) / 2f;
    public static final int GREEN_LENGTH = 3;

}

package com.hirish.frogger.config;

public final class GameConfig {
    public static final double WORLD_WIDTH = 600;
    public static final double WORLD_HEIGHT = 800;

    public static final double FROG_START_X = 300;
    public static final double FROG_START_Y = 706.4666666;
    public static final double FROG_MOVE_X = 42.666664;
    public static final double FROG_MOVE_Y = 53.3333332;
    public static final double FROG_MAX_Y = 734;

    public static final double WATER_ZONE_BOTTOM_Y = 413;

    public static final int HOME_SLOT_COUNT = 5;
    public static final double HOME_SLOT_Y = 96;
    public static final double HOME_SLOT_SIZE = 60;

    public static final double SCORE_Y = 10;
    public static final double SCORE_RIGHT_X = 572;
    public static final double SCORE_DIGIT_SPACING = 24;
    public static final double SCORE_DIGIT_SIZE = 22;

    public static final int FORWARD_MOVE_BONUS = 10;
    public static final int HOME_BONUS = 50;
    public static final int DEATH_PENALTY = 50;

    private GameConfig() {
    }
}

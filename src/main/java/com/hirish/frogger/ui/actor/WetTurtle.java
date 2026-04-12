package com.hirish.frogger.ui.actor;

import com.hirish.frogger.config.GameAssets;

import javafx.scene.image.Image;

public final class WetTurtle extends LaneActor {
    private final Image[] frames;
    private boolean sunk;

    public WetTurtle(double x, double y, double speed, double width, double height) {
        super(GameAssets.image("/assets/images/TurtleAnimation2Wet.png", width, height), x, y, speed);
        frames = new Image[] {
                GameAssets.image("/assets/images/TurtleAnimation2Wet.png", width, height),
                GameAssets.image("/assets/images/TurtleAnimation1.png", width, height),
                GameAssets.image("/assets/images/TurtleAnimation3Wet.png", width, height),
                GameAssets.image("/assets/images/TurtleAnimation4Wet.png", width, height)
        };
    }

    @Override
    protected void updateAnimation(long now) {
        int frameIndex = (int) ((now / 900_000_000L) % frames.length);
        sunk = frameIndex == frames.length - 1;
        setImage(frames[frameIndex]);
    }

    public boolean isSunk() {
        return sunk;
    }
}

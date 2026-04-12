package com.hirish.frogger.ui.actor;

import com.hirish.frogger.config.GameAssets;

import javafx.scene.image.Image;

public class Turtle extends LaneActor {
    private final Image[] frames;

    public Turtle(double x, double y, double speed, double width, double height) {
        super(GameAssets.image("/assets/images/TurtleAnimation2.png", width, height), x, y, speed);
        frames = new Image[] {
                GameAssets.image("/assets/images/TurtleAnimation2.png", width, height),
                GameAssets.image("/assets/images/TurtleAnimation1.png", width, height),
                GameAssets.image("/assets/images/TurtleAnimation3.png", width, height)
        };
    }

    @Override
    protected void updateAnimation(long now) {
        setImage(frames[(int) ((now / 900_000_000L) % frames.length)]);
    }
}

package com.hirish.frogger.ui.actor;

import com.hirish.frogger.config.GameConfig;
import com.hirish.frogger.logic.HorizontalLoop;

import javafx.scene.image.Image;

public abstract class LaneActor extends Actor {
    private final double speed;

    protected LaneActor(Image initialImage, double x, double y, double speed) {
        this.speed = speed;
        setImage(initialImage);
        setX(x);
        setY(y);
    }

    @Override
    public void act(long now) {
        updateAnimation(now);
        setX(HorizontalLoop.advance(
                getX(),
                getBoundsInLocal().getWidth(),
                speed,
                GameConfig.WORLD_WIDTH));
    }

    public double getSpeed() {
        return speed;
    }

    protected void updateAnimation(long now) {
    }
}

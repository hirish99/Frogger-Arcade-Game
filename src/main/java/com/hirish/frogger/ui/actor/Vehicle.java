package com.hirish.frogger.ui.actor;

import com.hirish.frogger.config.GameAssets;

public final class Vehicle extends LaneActor {
    public Vehicle(String imagePath, double x, double y, double speed, double width, double height) {
        super(GameAssets.image(imagePath, width, height), x, y, speed);
    }
}

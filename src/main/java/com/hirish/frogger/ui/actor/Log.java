package com.hirish.frogger.ui.actor;

import com.hirish.frogger.config.GameAssets;

public final class Log extends LaneActor {
    public Log(String imagePath, double size, double x, double y, double speed) {
        super(GameAssets.image(imagePath, size, size), x, y, speed);
    }
}

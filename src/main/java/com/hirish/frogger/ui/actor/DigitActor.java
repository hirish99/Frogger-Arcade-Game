package com.hirish.frogger.ui.actor;

import com.hirish.frogger.config.GameAssets;

public final class DigitActor extends Actor {
    public DigitActor(int digit, double size, double x, double y) {
        setImage(GameAssets.image("/assets/images/" + digit + ".png", size, size));
        setX(x);
        setY(y);
    }

    @Override
    public void act(long now) {
    }
}

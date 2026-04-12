package com.hirish.frogger.ui.actor;

import com.hirish.frogger.config.GameAssets;
import com.hirish.frogger.config.GameConfig;

public final class HomeSlot extends Actor {
    private boolean activated;

    public HomeSlot(double x, double y) {
        setX(x);
        setY(y);
        setImage(GameAssets.image("/assets/images/End.png", GameConfig.HOME_SLOT_SIZE, GameConfig.HOME_SLOT_SIZE));
    }

    @Override
    public void act(long now) {
    }

    public boolean activate() {
        if (activated) {
            return false;
        }

        activated = true;
        setImage(GameAssets.image(
                "/assets/images/FrogEnd.png",
                GameConfig.HOME_SLOT_SIZE,
                GameConfig.HOME_SLOT_SIZE));
        return true;
    }
}

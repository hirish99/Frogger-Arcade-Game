package com.hirish.frogger.ui.actor;

import javafx.scene.image.Image;

public final class BackgroundImageActor extends Actor {
    public BackgroundImageActor(Image image) {
        setImage(image);
    }

    @Override
    public void act(long now) {
    }
}

package com.hirish.frogger.config;

import java.net.URL;

import javafx.scene.image.Image;

public final class GameAssets {
    public static final String BACKGROUND = "/assets/images/iKogsKW.png";
    public static final String THEME_AUDIO = "/assets/audio/frogger-theme-loop.mp3";

    private GameAssets() {
    }

    public static Image image(String resourcePath, double width, double height) {
        return image(resourcePath, width, height, true);
    }

    public static Image image(String resourcePath, double width, double height, boolean preserveRatio) {
        return new Image(resource(resourcePath), width, height, preserveRatio, false);
    }

    public static String resource(String resourcePath) {
        URL resource = GameAssets.class.getResource(resourcePath);
        if (resource == null) {
            throw new IllegalStateException("Missing resource: " + resourcePath);
        }
        return resource.toExternalForm();
    }
}

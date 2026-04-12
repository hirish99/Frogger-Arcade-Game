package com.hirish.frogger;

import com.hirish.frogger.config.GameConfig;
import com.hirish.frogger.ui.GameWorld;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class FroggerApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        GameWorld gameWorld = new GameWorld();
        Scene scene = new Scene(gameWorld, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);

        stage.setTitle("Frogger Arcade Game");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        gameWorld.requestFocus();
        gameWorld.startMusic();
        gameWorld.start();
    }
}

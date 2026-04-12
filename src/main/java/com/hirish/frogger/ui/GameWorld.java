package com.hirish.frogger.ui;

import java.util.ArrayList;
import java.util.List;

import com.hirish.frogger.config.GameAssets;
import com.hirish.frogger.config.GameConfig;
import com.hirish.frogger.logic.GameState;
import com.hirish.frogger.ui.actor.BackgroundImageActor;
import com.hirish.frogger.ui.actor.DigitActor;
import com.hirish.frogger.ui.actor.Frog;
import com.hirish.frogger.ui.actor.HomeSlot;
import com.hirish.frogger.ui.actor.Log;
import com.hirish.frogger.ui.actor.Turtle;
import com.hirish.frogger.ui.actor.Vehicle;
import com.hirish.frogger.ui.actor.WetTurtle;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public final class GameWorld extends World {
    private static final int DIVIDER_SOURCE_Y = 265;
    private static final int DIVIDER_SOURCE_HEIGHT = 30;
    private static final double DIVIDER_CLEAR_Y = 388;
    private static final double DIVIDER_CLEAR_HEIGHT = 52;
    private static final double DIVIDER_TARGET_Y = 425;
    private static final double DIVIDER_TARGET_HEIGHT = 44;
    private static final Color WATER_BACKGROUND = Color.web("#15156d");

    private final GameState gameState = new GameState(GameConfig.HOME_SLOT_COUNT);
    private final List<DigitActor> scoreDigits = new ArrayList<>();
    private final Frog frog = new Frog(gameState);

    private MediaPlayer soundtrack;
    private boolean winShown;

    public GameWorld() {
        setFocusTraversable(true);
        setPrefSize(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        buildWorld();
        renderScore(gameState.getScore());
    }

    @Override
    public void act(long now) {
        if (gameState.consumeScoreChanged()) {
            renderScore(gameState.getScore());
        }

        if (!winShown && gameState.isGameWon()) {
            winShown = true;
            stopMusic();
            stop();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("You Have Won The Game!");
            alert.setHeaderText("Your Score: " + gameState.getScore());
            alert.setContentText("All five home slots are filled.");
            alert.show();
        }
    }

    public void startMusic() {
        try {
            Media soundtrackMedia = new Media(GameAssets.resource(GameAssets.THEME_AUDIO));
            soundtrack = new MediaPlayer(soundtrackMedia);
            soundtrack.setCycleCount(MediaPlayer.INDEFINITE);
            soundtrack.play();
        } catch (RuntimeException exception) {
            soundtrack = null;
        }
    }

    public void stopMusic() {
        if (soundtrack != null) {
            soundtrack.stop();
        }
    }

    private void buildWorld() {
        add(new BackgroundImageActor(GameAssets.image(
                GameAssets.BACKGROUND,
                GameConfig.WORLD_WIDTH,
                GameConfig.WORLD_HEIGHT,
                false)));
        addWaterRoadDividerOverlay();

        addRiverActors();
        addHomeSlots();
        addRoadActors();
        add(frog);
    }

    private void addRiverActors() {
        add(new Log("/assets/images/log3.png", 150, 0, 166, 0.75));
        add(new Log("/assets/images/log3.png", 150, 220, 166, 0.75));
        add(new Log("/assets/images/log3.png", 150, 440, 166, 0.75));
        add(new Log("/assets/images/logs.png", 300, 0, 276, -2));
        add(new Log("/assets/images/logs.png", 300, 400, 276, -2));
        add(new Log("/assets/images/log3.png", 150, 50, 329, 0.75));
        add(new Log("/assets/images/log3.png", 150, 270, 329, 0.75));
        add(new Log("/assets/images/log3.png", 150, 490, 329, 0.75));

        add(new Turtle(500, 376, -1, 130, 130));
        add(new Turtle(300, 376, -1, 130, 130));
        add(new WetTurtle(700, 376, -1, 130, 130));
        add(new WetTurtle(600, 217, -1, 130, 130));
        add(new WetTurtle(400, 217, -1, 130, 130));
        add(new WetTurtle(200, 217, -1, 130, 130));
    }

    private void addHomeSlots() {
        int[] positions = {13, 141, 269, 397, 525};
        for (int x : positions) {
            add(new HomeSlot(x, GameConfig.HOME_SLOT_Y));
        }
    }

    private void addRoadActors() {
        add(new Vehicle("/assets/images/truck1Right.png", 0, 649, 1, 120, 120));
        add(new Vehicle("/assets/images/truck1Right.png", 300, 649, 1, 120, 120));
        add(new Vehicle("/assets/images/truck1Right.png", 600, 649, 1, 120, 120));

        add(new Vehicle("/assets/images/car1Left.png", 100, 597, -1, 50, 50));
        add(new Vehicle("/assets/images/car1Left.png", 250, 597, -1, 50, 50));
        add(new Vehicle("/assets/images/car1Left.png", 400, 597, -1, 50, 50));
        add(new Vehicle("/assets/images/car1Left.png", 550, 597, -1, 50, 50));

        add(new Vehicle("/assets/images/truck2Right.png", 0, 540, 1, 200, 200));
        add(new Vehicle("/assets/images/truck2Right.png", 500, 540, 1, 200, 200));
        add(new Vehicle("/assets/images/car1Left.png", 500, 490, -5, 50, 50));
    }

    private void renderScore(int score) {
        for (DigitActor digit : scoreDigits) {
            remove(digit);
        }
        scoreDigits.clear();

        String scoreText = String.format("%04d", score);
        for (int index = 0; index < scoreText.length(); index++) {
            int digit = scoreText.charAt(index) - '0';
            double x = GameConfig.SCORE_RIGHT_X
                    - (scoreText.length() - 1 - index) * GameConfig.SCORE_DIGIT_SPACING;
            DigitActor digitActor = new DigitActor(digit, GameConfig.SCORE_DIGIT_SIZE, x, GameConfig.SCORE_Y);
            scoreDigits.add(digitActor);
            add(digitActor);
        }
    }

    private void addWaterRoadDividerOverlay() {
        Rectangle clearOriginalDivider = new Rectangle(
                0,
                DIVIDER_CLEAR_Y,
                GameConfig.WORLD_WIDTH,
                DIVIDER_CLEAR_HEIGHT);
        clearOriginalDivider.setFill(WATER_BACKGROUND);
        getChildren().add(clearOriginalDivider);

        Image rawBackground = new Image(GameAssets.resource(GameAssets.BACKGROUND));
        WritableImage dividerStrip = new WritableImage(
                rawBackground.getPixelReader(),
                0,
                DIVIDER_SOURCE_Y,
                (int) rawBackground.getWidth(),
                DIVIDER_SOURCE_HEIGHT);

        ImageView dividerView = new ImageView(dividerStrip);
        dividerView.setFitWidth(GameConfig.WORLD_WIDTH);
        dividerView.setFitHeight(DIVIDER_TARGET_HEIGHT);
        dividerView.setPreserveRatio(false);
        dividerView.setSmooth(false);
        dividerView.setY(DIVIDER_TARGET_Y);
        getChildren().add(dividerView);
    }
}

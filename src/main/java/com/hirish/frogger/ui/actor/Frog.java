package com.hirish.frogger.ui.actor;

import java.util.EnumMap;
import java.util.Map;

import com.hirish.frogger.config.GameAssets;
import com.hirish.frogger.config.GameConfig;
import com.hirish.frogger.logic.GameState;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public final class Frog extends Actor {
    private static final double SPRITE_SIZE = 40;
    private static final long DEATH_FRAME_DURATION_NANOS = 120_000_000L;

    private final Map<Direction, Image> idleImages = new EnumMap<>(Direction.class);
    private final Map<Direction, Image> jumpImages = new EnumMap<>(Direction.class);
    private final Image[] carDeathFrames;
    private final Image[] waterDeathFrames;
    private final GameState gameState;

    private Direction facing = Direction.UP;
    private DeathType deathType = DeathType.NONE;
    private boolean controlsLocked;
    private int deathFrameIndex;
    private long lastDeathFrameTime;

    public Frog(GameState gameState) {
        this.gameState = gameState;
        loadImages();
        carDeathFrames = loadCarDeathFrames();
        waterDeathFrames = loadWaterDeathFrames();
        setImage(idleImages.get(Direction.UP));
        resetPosition();
        setOnKeyPressed(this::handleKeyPressed);
        setOnKeyReleased(this::handleKeyReleased);
    }

    @Override
    public void act(long now) {
        if (deathType != DeathType.NONE) {
            advanceDeathAnimation(now);
            return;
        }

        clampToWorld();

        if (getY() < 0 || getY() > GameConfig.FROG_MAX_Y) {
            resetPosition();
            return;
        }

        if (!getIntersectingObjects(Vehicle.class).isEmpty()) {
            beginDeath(DeathType.CAR, now);
            return;
        }

        HomeSlot homeSlot = getOneIntersectingObject(HomeSlot.class);
        if (homeSlot != null) {
            gameState.registerHomeReached(homeSlot.activate());
            resetAfterRound();
            return;
        }

        Log log = getOneIntersectingObject(Log.class);
        if (log != null) {
            move(log.getSpeed(), 0);
            clampToWorld();
            return;
        }

        Turtle turtle = getOneIntersectingObject(Turtle.class);
        if (turtle != null) {
            move(turtle.getSpeed(), 0);
            clampToWorld();
            return;
        }

        WetTurtle wetTurtle = getOneIntersectingObject(WetTurtle.class);
        if (wetTurtle != null) {
            if (wetTurtle.isSunk()) {
                beginDeath(DeathType.WATER, now);
            } else {
                move(wetTurtle.getSpeed(), 0);
                clampToWorld();
            }
            return;
        }

        if (getY() < GameConfig.WATER_ZONE_BOTTOM_Y) {
            beginDeath(DeathType.WATER, now);
        }
    }

    private void handleKeyPressed(KeyEvent event) {
        if (controlsLocked) {
            return;
        }

        Direction direction = toDirection(event.getCode());
        if (direction == null) {
            return;
        }

        facing = direction;
        applyMovement(direction);
        setImage(jumpImages.get(direction));

        if (direction == Direction.UP) {
            gameState.registerForwardProgress(getY());
        }
    }

    private void handleKeyReleased(KeyEvent event) {
        if (controlsLocked) {
            return;
        }

        Direction direction = toDirection(event.getCode());
        if (direction == null) {
            return;
        }

        facing = direction;
        setImage(idleImages.get(direction));
    }

    private void applyMovement(Direction direction) {
        switch (direction) {
            case UP:
                move(0, -GameConfig.FROG_MOVE_Y);
                break;
            case DOWN:
                move(0, GameConfig.FROG_MOVE_Y);
                break;
            case LEFT:
                move(-GameConfig.FROG_MOVE_X, 0);
                break;
            case RIGHT:
                move(GameConfig.FROG_MOVE_X, 0);
                break;
            default:
                break;
        }
    }

    private void beginDeath(DeathType type, long now) {
        if (deathType != DeathType.NONE) {
            return;
        }

        deathType = type;
        controlsLocked = true;
        deathFrameIndex = 0;
        lastDeathFrameTime = now;
        setImage(activeDeathFrames()[deathFrameIndex]);
    }

    private void advanceDeathAnimation(long now) {
        if (now - lastDeathFrameTime < DEATH_FRAME_DURATION_NANOS) {
            return;
        }

        lastDeathFrameTime = now;
        deathFrameIndex++;

        Image[] frames = activeDeathFrames();
        if (deathFrameIndex < frames.length) {
            setImage(frames[deathFrameIndex]);
            return;
        }

        gameState.registerDeath();
        resetAfterRound();
    }

    private Image[] activeDeathFrames() {
        return deathType == DeathType.CAR ? carDeathFrames : waterDeathFrames;
    }

    private void resetAfterRound() {
        deathType = DeathType.NONE;
        controlsLocked = false;
        deathFrameIndex = 0;
        facing = Direction.UP;
        setImage(idleImages.get(Direction.UP));
        resetPosition();
    }

    private void resetPosition() {
        setX(GameConfig.FROG_START_X);
        setY(GameConfig.FROG_START_Y);
    }

    private void clampToWorld() {
        double maxX = GameConfig.WORLD_WIDTH - getBoundsInLocal().getWidth();
        setX(Math.max(0, Math.min(maxX, getX())));
    }

    private void loadImages() {
        idleImages.put(Direction.UP, GameAssets.image("/assets/images/froggerUp.png", SPRITE_SIZE, SPRITE_SIZE));
        idleImages.put(Direction.LEFT, GameAssets.image("/assets/images/froggerLeft.png", SPRITE_SIZE, SPRITE_SIZE));
        idleImages.put(Direction.DOWN, GameAssets.image("/assets/images/froggerDown.png", SPRITE_SIZE, SPRITE_SIZE));
        idleImages.put(Direction.RIGHT, GameAssets.image("/assets/images/froggerRight.png", SPRITE_SIZE, SPRITE_SIZE));

        jumpImages.put(Direction.UP, GameAssets.image("/assets/images/froggerUpJump.png", SPRITE_SIZE, SPRITE_SIZE));
        jumpImages.put(Direction.LEFT, GameAssets.image("/assets/images/froggerLeftJump.png", SPRITE_SIZE, SPRITE_SIZE));
        jumpImages.put(Direction.DOWN, GameAssets.image("/assets/images/froggerDownJump.png", SPRITE_SIZE, SPRITE_SIZE));
        jumpImages.put(Direction.RIGHT, GameAssets.image("/assets/images/froggerRightJump.png", SPRITE_SIZE, SPRITE_SIZE));

    }

    private Image[] loadCarDeathFrames() {
        return new Image[] {
                GameAssets.image("/assets/images/cardeath1.png", SPRITE_SIZE, SPRITE_SIZE),
                GameAssets.image("/assets/images/cardeath2.png", SPRITE_SIZE, SPRITE_SIZE),
                GameAssets.image("/assets/images/cardeath3.png", SPRITE_SIZE, SPRITE_SIZE)
        };
    }

    private Image[] loadWaterDeathFrames() {
        return new Image[] {
                GameAssets.image("/assets/images/waterdeath1.png", SPRITE_SIZE, SPRITE_SIZE),
                GameAssets.image("/assets/images/waterdeath2.png", SPRITE_SIZE, SPRITE_SIZE),
                GameAssets.image("/assets/images/waterdeath3.png", SPRITE_SIZE, SPRITE_SIZE),
                GameAssets.image("/assets/images/waterdeath4.png", SPRITE_SIZE, SPRITE_SIZE)
        };
    }

    private Direction toDirection(KeyCode keyCode) {
        switch (keyCode) {
            case W:
            case UP:
                return Direction.UP;
            case A:
            case LEFT:
                return Direction.LEFT;
            case S:
            case DOWN:
                return Direction.DOWN;
            case D:
            case RIGHT:
                return Direction.RIGHT;
            default:
                return null;
        }
    }

    private enum Direction {
        UP,
        LEFT,
        DOWN,
        RIGHT
    }

    private enum DeathType {
        NONE,
        CAR,
        WATER
    }
}

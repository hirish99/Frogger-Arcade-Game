package com.hirish.frogger.ui;

import java.util.ArrayList;
import java.util.List;

import com.hirish.frogger.ui.actor.Actor;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public abstract class World extends Pane {
    private AnimationTimer timer;

    protected World() {
        sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                installSceneHandlers(newScene);
            }
        });
    }

    public final void start() {
        if (timer == null) {
            createTimer();
        }
        timer.start();
    }

    public final void stop() {
        if (timer != null) {
            timer.stop();
        }
    }

    public final void add(Actor actor) {
        getChildren().add(actor);
    }

    public final void remove(Actor actor) {
        getChildren().remove(actor);
    }

    public final <A extends Actor> List<A> getObjects(Class<A> type) {
        List<A> actors = new ArrayList<>();
        for (Node node : getChildren()) {
            if (type.isInstance(node)) {
                actors.add(type.cast(node));
            }
        }
        return actors;
    }

    public abstract void act(long now);

    private void createTimer() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                act(now);
                for (Actor actor : getObjects(Actor.class)) {
                    actor.act(now);
                }
            }
        };
    }

    private void installSceneHandlers(Scene scene) {
        scene.setOnKeyPressed(event -> dispatch(event, true));
        scene.setOnKeyReleased(event -> dispatch(event, false));
    }

    private void dispatch(KeyEvent event, boolean pressed) {
        EventHandler<? super KeyEvent> worldHandler = pressed ? getOnKeyPressed() : getOnKeyReleased();
        if (worldHandler != null) {
            worldHandler.handle(event);
        }

        for (Actor actor : getObjects(Actor.class)) {
            EventHandler<? super KeyEvent> actorHandler = pressed ? actor.getOnKeyPressed() : actor.getOnKeyReleased();
            if (actorHandler != null) {
                actorHandler.handle(event);
            }
        }
    }
}

package com.hirish.frogger.ui.actor;

import java.util.ArrayList;
import java.util.List;

import com.hirish.frogger.ui.World;

import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;

public abstract class Actor extends ImageView {
    public void move(double dx, double dy) {
        setX(getX() + dx);
        setY(getY() + dy);
    }

    public World getWorld() {
        return (World) getParent();
    }

    public <A extends Actor> List<A> getIntersectingObjects(Class<A> type) {
        List<A> matches = new ArrayList<>();
        Bounds actorBounds = getBoundsInParent();
        for (A actor : getWorld().getObjects(type)) {
            if (actor != this && actor.getBoundsInParent().intersects(actorBounds)) {
                matches.add(actor);
            }
        }
        return matches;
    }

    public <A extends Actor> A getOneIntersectingObject(Class<A> type) {
        for (A actor : getWorld().getObjects(type)) {
            if (actor != this && actor.getBoundsInParent().intersects(getBoundsInParent())) {
                return actor;
            }
        }
        return null;
    }

    public abstract void act(long now);
}

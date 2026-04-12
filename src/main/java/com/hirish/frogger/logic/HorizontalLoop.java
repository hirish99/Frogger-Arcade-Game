package com.hirish.frogger.logic;

public final class HorizontalLoop {
    private HorizontalLoop() {
    }

    public static double advance(double currentX, double spriteWidth, double speed, double worldWidth) {
        double nextX = currentX + speed;

        if (speed > 0 && nextX > worldWidth) {
            return -spriteWidth;
        }

        if (speed < 0 && nextX < -spriteWidth) {
            return worldWidth;
        }

        return nextX;
    }
}

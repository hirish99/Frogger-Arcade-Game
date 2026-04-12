package com.hirish.frogger.logic;

import com.hirish.frogger.testing.TestSupport;

public final class HorizontalLoopTest {
    private HorizontalLoopTest() {
    }

    public static void run() {
        wrapsMovingRightActorsToTheLeftEdge();
        wrapsMovingLeftActorsToTheRightEdge();
        keepsActorsMovingWhenTheyRemainOnScreen();
    }

    private static void wrapsMovingRightActorsToTheLeftEdge() {
        double nextX = HorizontalLoop.advance(599, 120, 2, 600);
        TestSupport.assertEquals(-120, nextX, 0, "Right-moving actors should wrap to the left side once they leave the board.");
    }

    private static void wrapsMovingLeftActorsToTheRightEdge() {
        double nextX = HorizontalLoop.advance(-121, 120, -2, 600);
        TestSupport.assertEquals(600, nextX, 0, "Left-moving actors should wrap to the right side once they leave the board.");
    }

    private static void keepsActorsMovingWhenTheyRemainOnScreen() {
        double nextX = HorizontalLoop.advance(100, 120, 2, 600);
        TestSupport.assertEquals(102, nextX, 0, "Actors that are still on screen should continue moving normally.");
    }
}

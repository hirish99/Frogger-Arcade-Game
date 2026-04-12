package com.hirish.frogger.testing;

import com.hirish.frogger.logic.GameStateTest;
import com.hirish.frogger.logic.HorizontalLoopTest;

public final class LogicTestRunner {
    private LogicTestRunner() {
    }

    public static void main(String[] args) {
        GameStateTest.run();
        HorizontalLoopTest.run();
        System.out.println("All Frogger logic tests passed.");
    }
}

package com.hirish.frogger.logic;

import com.hirish.frogger.testing.TestSupport;

public final class GameStateTest {
    private GameStateTest() {
    }

    public static void run() {
        forwardProgressOnlyScoresWhenFrogMovesHigher();
        deathPenaltyResetsProgressWithoutDroppingBelowZero();
        homeSlotsOnlyScoreOnce();
        fillingAllHomesWinsTheGame();
        scoreChangeFlagIsConsumable();
    }

    private static void forwardProgressOnlyScoresWhenFrogMovesHigher() {
        GameState state = new GameState(5);

        state.registerForwardProgress(700);
        state.registerForwardProgress(650);
        state.registerForwardProgress(675);

        TestSupport.assertEquals(20, state.getScore(), "Forward progress should award 10 points per new best row.");
    }

    private static void deathPenaltyResetsProgressWithoutDroppingBelowZero() {
        GameState state = new GameState(5);

        state.registerForwardProgress(700);
        state.registerForwardProgress(650);
        state.registerDeath();

        TestSupport.assertEquals(0, state.getScore(), "Death should deduct the score penalty but never go below zero.");
        TestSupport.assertEquals(
                Double.POSITIVE_INFINITY,
                state.getHighestYThisRun(),
                0,
                "Death should reset the tracked highest row for the next run.");
    }

    private static void homeSlotsOnlyScoreOnce() {
        GameState state = new GameState(5);

        state.registerHomeReached(true);
        state.registerHomeReached(false);

        TestSupport.assertEquals(50, state.getScore(), "Only a newly filled home should award bonus points.");
        TestSupport.assertEquals(1, state.getHomesFilled(), "Only unique home slots should count toward the win condition.");
    }

    private static void fillingAllHomesWinsTheGame() {
        GameState state = new GameState(2);

        state.registerHomeReached(true);
        TestSupport.assertFalse(state.isGameWon(), "The game should continue until every home slot is filled.");

        state.registerHomeReached(true);
        TestSupport.assertTrue(state.isGameWon(), "The game should end after the final home slot is filled.");
    }

    private static void scoreChangeFlagIsConsumable() {
        GameState state = new GameState(5);

        state.registerForwardProgress(700);

        TestSupport.assertTrue(state.consumeScoreChanged(), "A score update should mark the score as dirty.");
        TestSupport.assertFalse(state.consumeScoreChanged(), "Reading the dirty flag should clear it.");
    }
}

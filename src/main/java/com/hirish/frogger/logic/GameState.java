package com.hirish.frogger.logic;

import com.hirish.frogger.config.GameConfig;

public final class GameState {
    private final int totalHomeSlots;

    private int score;
    private int homesFilled;
    private double highestYThisRun = Double.POSITIVE_INFINITY;
    private boolean scoreChanged;

    public GameState(int totalHomeSlots) {
        this.totalHomeSlots = totalHomeSlots;
    }

    public void registerForwardProgress(double frogY) {
        if (frogY < highestYThisRun) {
            highestYThisRun = frogY;
            updateScore(score + GameConfig.FORWARD_MOVE_BONUS);
        }
    }

    public void registerDeath() {
        updateScore(Math.max(0, score - GameConfig.DEATH_PENALTY));
        resetRunProgress();
    }

    public void registerHomeReached(boolean newlyFilled) {
        if (newlyFilled) {
            homesFilled++;
            updateScore(score + GameConfig.HOME_BONUS);
        }
        resetRunProgress();
    }

    public int getScore() {
        return score;
    }

    public int getHomesFilled() {
        return homesFilled;
    }

    public boolean isGameWon() {
        return homesFilled >= totalHomeSlots;
    }

    public boolean consumeScoreChanged() {
        boolean currentValue = scoreChanged;
        scoreChanged = false;
        return currentValue;
    }

    void resetRunProgress() {
        highestYThisRun = Double.POSITIVE_INFINITY;
    }

    double getHighestYThisRun() {
        return highestYThisRun;
    }

    private void updateScore(int newScore) {
        if (score != newScore) {
            score = newScore;
            scoreChanged = true;
        }
    }
}

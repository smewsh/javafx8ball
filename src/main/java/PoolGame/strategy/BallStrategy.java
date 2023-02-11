package PoolGame.strategy;

/**
 * Default ball strategy.
 */
public class BallStrategy extends PocketStrategy {
    /**
     * Creates a new ball strategy.
     */
    public BallStrategy() {
        this.lives = 1;
        this.score = 0;
    }

    public BallStrategy(int lives, int score) {
        this.lives = lives;
        this.score = score;
        this.name = "Other";
    }

    public void reset() {
        this.lives = 1;
    }
}

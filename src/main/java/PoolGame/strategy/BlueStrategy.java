package PoolGame.strategy;

/**
 * Ball strategy for blue balls.
 */
public class BlueStrategy extends PocketStrategy {
    /** Creates a new blue strategy. */
    public BlueStrategy() {
        this.lives = 2;
        this.score = 5;
        this.name = "Blue";
    }

    public void reset() {
        this.lives = 2;
    }
}

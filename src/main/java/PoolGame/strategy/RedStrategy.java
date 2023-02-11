package PoolGame.strategy;

/**
 * Ball strategy for red balls.
 */
public class RedStrategy extends PocketStrategy {
    /**
     * Creates a new ball strategy.
     */
    public RedStrategy() {
        this.lives = 1;
        this.score = 1;
        this.name = "Red";
    }

    public void reset() {
        this.lives = 1;
    }
}

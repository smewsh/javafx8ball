package PoolGame.strategy;

/**
 * Ball strategy for yellow balls.
 */
public class YellowStrategy extends PocketStrategy {
    /**
     * Creates a new ball strategy.
     */
    public YellowStrategy() {
        this.lives = 1;
        this.score = 2;
        this.name = "Yellow";
    }

    public void reset() {
        this.lives = 1;
    }
}

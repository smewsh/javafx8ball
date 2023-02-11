package PoolGame.strategy;

/**
 * Ball strategy for black balls.
 */
public class BlackStrategy extends PocketStrategy {
    /**
     * Creates a new ball strategy.
     */
    public BlackStrategy() {
        this.lives = 1;
        this.score = 7;
        this.name = "Black";
    }

    public void reset() {
        this.lives = 1;
    }
}

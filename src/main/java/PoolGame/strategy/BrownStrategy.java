package PoolGame.strategy;

/**
 * Ball strategy for brown balls.
 */
public class BrownStrategy extends PocketStrategy {
    /**
     * Creates a new ball strategy.
     */
    public BrownStrategy() {
        this.lives = 1;
        this.score = 4;
        this.name = "Brown";
    }

    public void reset() {
        this.lives = 1;
    }
}

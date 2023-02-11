package PoolGame.strategy;


/**
 * Ball strategy for orange balls.
 */
public class OrangeStrategy extends PocketStrategy {
    /**
     * Creates a new ball strategy.
     */
    public OrangeStrategy() {
        this.lives = 1;
        this.score = 8;
        this.name = "Orange";
    }

    public void reset() {
        this.lives = 1;
    }
}

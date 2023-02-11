package PoolGame.strategy;

/**
 * Ball strategy for green balls.
 */
public class GreenStrategy extends PocketStrategy {
    /** Creates a new blue strategy. */
    public GreenStrategy() {
        this.lives = 2;
        this.score = 3;
        this.name = "Green";
    }

    public void reset() {
        this.lives = 2;
    }
}

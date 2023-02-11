package PoolGame.strategy;

/**
 * Ball strategy for purple balls.
 */
public class PurpleStrategy extends PocketStrategy {
    /** Creates a new blue strategy. */
    public PurpleStrategy() {
        this.lives = 2;
        this.score = 6;
        this.name = "Purple";
    }

    public void reset() {
        this.lives = 2;
    }
}

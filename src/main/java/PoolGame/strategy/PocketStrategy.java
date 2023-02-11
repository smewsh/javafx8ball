package PoolGame.strategy;

/** Holds strategy for when balls enter a pocket. */
public abstract class PocketStrategy {
    /** Number of lives the ball has. */
    protected int lives;
    protected int score;
    protected String name;

    /**
     * Removes a life from the ball and determines if ball should be active.
     * 
     * @return true if ball should be removed, false otherwise.
     */
    public boolean remove() {
        this.lives--;

        if (this.lives == 0) {
            return true;
        }
        return false;
    }

    public void forceRemove() {
        this.lives = 0;
    }

    /**
     * Gets the score of the ball.
     * 
     * @return the score of the ball.
     */
    public int getScore() {
        return this.score;
    }

    public int getLives() {
        return this.lives;
    }

    public int getMaxScore() {
        return this.score*this.lives;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Resets the ball to its original state.
     */
    public abstract void reset();
}

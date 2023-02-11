package PoolGame.objects;
import PoolGame.observer.BallSubscriber;

import PoolGame.Config;
import PoolGame.strategy.*;
import javafx.scene.paint.Paint;



/** Holds information for all ball-related objects. */
public class Ball implements BallSubscriber{

    private Paint colour;
    private double xPosition;
    private double yPosition;
    private double startX;
    private double startY;
    private double xVelocity;
    private double yVelocity;
    private double mass;
    private double radius;
    private boolean isCue;
    private boolean isActive;
    private PocketStrategy strategy;

    private final double MAXVEL = 20;

    /**
     * Constructor for a ball object.
     */
    public Ball(String colour, double xPosition, double yPosition, double xVelocity, double yVelocity, double mass,
            boolean isCue, PocketStrategy strategy) {
        this.colour = Paint.valueOf(colour);
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.startX = xPosition;
        this.startY = yPosition;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.mass = mass;
        this.radius = 10;
        this.isCue = isCue;
        this.isActive = true;
        this.strategy = strategy;
    }

    /**
     * Deep copy constructor for a ball object.
     * @param ball the ball to copy
     */
    public Ball(Ball ball) {
        this.colour = ball.getColour();
        this.xPosition = ball.getRawXPos();
        this.yPosition = ball.getRawYPos();
        this.startX = ball.getStartXPos();
        this.startY = ball.getStartYPos();
        this.xVelocity = ball.getxVel();
        this.yVelocity = ball.getyVel();
        this.mass = ball.getMass();
        this.radius = ball.getRadius();
        this.isCue = ball.isCue();
        this.isActive = ball.isActive();
        this.strategy = new BallStrategy(ball.getStrategy().getLives(),ball.getStrategy().getScore());
    }

    /**
     * Updates ball position per tick.
     */
    public void tick() {
        xPosition += xVelocity;
        yPosition += yVelocity;
    }

    /**
     * Resets ball position, velocity, and activity.
     */
    public void reset() {
        resetPosition();
        isActive = true;
        strategy.reset();
    }

    /**
     * Resets ball position and velocity.
     */
    public void resetPosition() {
        xPosition = startX;
        yPosition = startY;
        xVelocity = 0;
        yVelocity = 0;
    }

    /**
     * Removes ball from play.
     * 
     * @return true if ball is successfully removed
     */
    public boolean remove() {
        if (strategy.remove()) {
            isActive = false;
            return true;
        } else {
            resetPosition();
            return false;
        }
    }

    /**
     * Removes ball from play.
     * Ignoring strategy and lives.
     */
    public void forceRemove(){
        strategy.forceRemove();
        isActive = false;
    }

    /**
     * Sets x-axis velocity of ball.
     * 
     * @param xVelocity of ball.
     */
    public void setxVel(double xVelocity) {
        if (xVelocity > MAXVEL) {
            this.xVelocity = MAXVEL;
        } else if (xVelocity < -MAXVEL) {
            this.xVelocity = -MAXVEL;
        } else {
            this.xVelocity = xVelocity;
        }
    }

    /**
     * Sets y-axis velocity of ball.
     * 
     * @param yVelocity of ball.
     */
    public void setyVel(double yVelocity) {
        if (yVelocity > MAXVEL) {
            this.yVelocity = MAXVEL;
        } else if (yVelocity < -MAXVEL) {
            this.yVelocity = -MAXVEL;
        } else {
            this.yVelocity = yVelocity;
        }
    }

    /**
     * Sets x-axis position of ball.
     * 
     * @param xPosition of ball.
     */
    public void setxPos(double xPosition) {
        this.xPosition = xPosition;
    }

    /**
     * Sets y-axis position of ball.
     * 
     * @param yPosition of ball.
     */
    public void setyPos(double yPosition) {
        this.yPosition = yPosition;
    }

    /**
     * Getter method for radius of ball.
     * 
     * @return radius length.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Getter method for x-position of ball.
     * 
     * @return x position.
     */
    public double getxPos() {
        return xPosition + Config.getTableBuffer();
    }

    /**
     * Getter method for y-position of ball.
     * 
     * @return y position.
     */
    public double getyPos() {
        return yPosition + Config.getTableBuffer();
    }

    /**
     * Gets x-position of ball without table buffer.
     */
    public double getRawXPos() {
        return xPosition;
    }

    /**
     * Gets y-position of ball without table buffer.
     */
    public double getRawYPos() {
        return yPosition;
    }

    /**
     * Getter method for starting x-position of ball.
     * 
     * @return starting x position.
     */
    public double getStartXPos() {
        return startX;
    }

    /**
     * Getter method for starting y-position of ball.
     * 
     * @return starting y position.
     */
    public double getStartYPos() {
        return startY;
    }

    /**
     * Getter method for starting mass of ball.
     * 
     * @return mass.
     */
    public double getMass() {
        return mass;
    }

    /**
     * Getter method for colour of ball.
     * 
     * @return colour.
     */
    public Paint getColour() {
        return colour;
    }

    /**
     * Gets colour of ball as a hex code string.
     * @return colour as hex code string
     */
    public String getColourString() {
        return colour.toString();
    }

    /**
     * Getter method for x-axis velocity of ball.
     * 
     * @return x velocity.
     */
    public double getxVel() {
        return xVelocity;
    }

    /**
     * Getter method for y-axis velocity of ball.
     * 
     * @return y velocity.
     */
    public double getyVel() {
        return yVelocity;
    }

    /**
     * Getter method for whether ball is cue ball.
     * 
     * @return true if ball is cue ball.
     */
    public boolean isCue() {
        return isCue;
    }

    /**
     * Getter method for whether ball is active.
     * 
     * @return true if ball is active.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Getter for strategy
     * 
     * @return strategy 
     */

    public PocketStrategy getStrategy() {
        return strategy;
    }
}

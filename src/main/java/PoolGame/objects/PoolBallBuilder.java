package PoolGame.objects;

import PoolGame.strategy.PocketStrategy;
import PoolGame.strategy.PurpleStrategy;
import PoolGame.strategy.RedStrategy;
import PoolGame.strategy.YellowStrategy;
import PoolGame.strategy.BallStrategy;
import PoolGame.strategy.BlackStrategy;
import PoolGame.strategy.BlueStrategy;
import PoolGame.strategy.BrownStrategy;
import PoolGame.strategy.GreenStrategy;
import PoolGame.strategy.OrangeStrategy;

/** Builds pool balls. */
public class PoolBallBuilder implements BallBuilder {
    // Required Parameters
    private String colour;
    private double xPosition;
    private double yPosition;
    private double xVelocity;
    private double yVelocity;
    private double mass;

    // Variable Parameters
    private boolean isCue = false;
    public PocketStrategy strategy;

    @Override
    public void setColour(String colour) {
        this.colour = colour;
    };

    @Override
    public void setxPos(double xPosition) {
        this.xPosition = xPosition;
    };

    @Override
    public void setyPos(double yPosition) {
        this.yPosition = yPosition;
    };

    @Override
    public void setxVel(double xVelocity) {
        this.xVelocity = xVelocity;
    };

    @Override
    public void setyVel(double yVelocity) {
        this.yVelocity = yVelocity;
    };

    @Override
    public void setMass(double mass) {
        this.mass = mass;
    };

    /**
     * Builds the ball.
     * Determines strategy based on colour.`
     * 
     * @return ball
     */
    public Ball build() {
        if (colour.equals("white")) {
            isCue = true;
            strategy = new BallStrategy();
        } else if (colour.equals("blue")) {
            strategy = new BlueStrategy();
        } else if (colour.equals("orange")) {
            strategy = new OrangeStrategy();
        } else if (colour.equals("red")) {
            strategy = new RedStrategy();
        } else if (colour.equals("yellow")) {
            strategy = new YellowStrategy();    
        } else if (colour.equals("green")) {
            strategy = new GreenStrategy();
        } else if (colour.equals("brown")) {
            strategy = new BrownStrategy();
        } else if (colour.equals("black")) {
            strategy = new BlackStrategy();
        } else if(colour.equals("purple")) {
            strategy = new PurpleStrategy();      
        } else {
            strategy = new BallStrategy();
        }

        return new Ball(colour, xPosition, yPosition, xVelocity, yVelocity, mass, isCue, strategy);
    }
}

package PoolGame.memento;

import PoolGame.GameManager;
import PoolGame.objects.*;

import java.util.ArrayList;


/**
 * Memento class for the game manager
 */
public class GameManagerState {
    private ArrayList<Ball> balls;
    private int score;
    private double time;

    /**
     * Constructor
     * @param gameManager the game manager to save
     */
    public GameManagerState(GameManager gameManager) {
        this.balls = new ArrayList<Ball>();
        for (Ball ball : gameManager.getBalls()) {
            gameManager.getBallPublisherRegistry().assignBallSubscriber(ball);
            this.balls.add(new Ball(ball));
        }
        this.score = gameManager.getScore();//Integer.valueOf(gameManager.getScore());
        this.time = gameManager.getTime();//Double.valueOf(gameManager.getTime());
    }

    public ArrayList<Ball> getBalls() {
        return this.balls;
    }

    public int getScore() {
        return this.score;
    }

    public double getTime() {
        return this.time;
    }
}
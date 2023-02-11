package PoolGame.memento;

import java.util.Stack;

import PoolGame.GameManager;

/**
 * Caretaker class for the game manager memento
 */
public class GameManagerCaretaker {

    private Stack<GameManagerState> states;
    private GameManager gameManager;

    /**
     * Constructor
     * @param gameManager
     */
    public GameManagerCaretaker(GameManager gameManager) {
        this.gameManager = gameManager;
        this.states = new Stack<GameManagerState>();
    }

    /**
     * Save the current state of the game manager
     */
    public void saveState() {
        this.states.push(gameManager.saveState());
    }

    /**
     * Restore the previous state of the game manager
     */
    public void restoreState() {
        if (!isEmpty()) {
            gameManager.restoreState(this.states.pop());
        }
    }

    /**
     * Check if the stack is empty
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return this.states.isEmpty();
    }
}
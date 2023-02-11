package PoolGame.observer;

import java.util.ArrayList;

/**
 * Concrete implementation of a ball publisher
 * This class is used to notify subscribers of commands to balls.
 */
public class BallPublisher {
    private ArrayList<BallSubscriber> subscribers = new ArrayList<BallSubscriber>();

    /**
     * Add a subscriber to the list of subscribers
     * @param subscriber the subscriber to add
     */
    public void subscribe(BallSubscriber subscriber) {
        this.subscribers.add(subscriber);
    }

    /**
     * Remove a subscriber from the list of subscribers
     * @param subscriber the subscriber to remove
     */
    public void unsubscribe(BallSubscriber subscriber) {
        this.subscribers.remove(subscriber);
    }

    /**
     * Unsubscribe all subscribers
     */
    public void unsubscribeAll() {
        this.subscribers.clear();
    }

    /**
     * Notify all subscribers to remove the ball
     */
    public void notifySubscribersToRemove() {
        for (BallSubscriber subscriber : this.subscribers) {
            subscriber.forceRemove();
        }
        
    }


    
}

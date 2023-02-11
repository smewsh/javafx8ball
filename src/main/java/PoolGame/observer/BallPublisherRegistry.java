package PoolGame.observer;

import java.util.HashMap;

/**
 * Registry of ball publishers
 */
public class BallPublisherRegistry {
    private HashMap<String, BallPublisher> publishers;

    /**
     * Constructor
     * instantiates the publishers hashmap
     */
    public BallPublisherRegistry() {
        this.publishers = new HashMap<String, BallPublisher>();
    }

    /**
     * Adds a publisher to the registry
     * @param name the name of the publisher
     * @param publisher the publisher to add
     */
    public void addPublisher(String name, BallPublisher publisher) {
        this.publishers.put(name, publisher);
    }

    /**
     * Gets a publisher from the registry
     * @param name the name of the publisher
     * @return the publisher
     */
    public BallPublisher getPublisher(String name) {
        return this.publishers.get(name);
    }

    /**
     * Gets all publishers from the registry
     * @return the hashmap of publishers
     */
    public HashMap<String, BallPublisher> getPublishers() {
        return this.publishers;
    }

    /**
     * Assigns a subscriber to a publisher
     * If the matching publisher does not exist, it is created
     * @param subscriber the subscriber to assign
     */
    public void assignBallSubscriber(BallSubscriber subscriber) {
        if (subscriber.getColourString().equals("0xffffffff")) {
            return; //ignore cue ball
        }
        if (this.publishers.containsKey(subscriber.getColourString())) {
            this.publishers.get(subscriber.getColourString()).subscribe(subscriber);
        } else {
            BallPublisher publisher = new BallPublisher();
            publisher.subscribe(subscriber);
            this.publishers.put(subscriber.getColourString(), publisher);
        }
    }
}

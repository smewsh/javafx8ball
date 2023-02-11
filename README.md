To run this app, you must have Gradle set up.

Then, execute `gradle run` inside this folder.

##### Implemented Features:

- Custom config files (pockets + more coloured balls)
- Difficulty level selection
- Time indicator
- Score indicator
- Undo feature
- Cheat feature

#### Design Patterns Used:

##### Factory Design Pattern

- Used for creating PocketReader objects for reading pocket data from config.
- Found in 'src/main/java/PoolGame/config'
- Associated files:
	- PocketReader.java as the concrete product
	- PocketReaderFactory.java as the concrete factory
	- Reader.java as the abstract product
	- ReaderFactory.java as the abstract factory

##### Builder Design Pattern

- Used for building and configuring custom Pockets for the pool table.
- Found in 'src/main/java/PoolGame/objects'
- Associated files:
    - PocketBuilder.java as the abstract builder
    - Pocket.java as the concrete product
    - PoolPocketBuilder.java as the concrete builder.
    - PocketReader.java (in 'src/main/java/PoolGame/config') as the Director


##### Memento Design Pattern
- Used for provision of the undo function; saving the game state, and rolling back.
- Found in 'src/main/java/PoolGame/memento'
- Associated files:
  - GameManager.java (in 'src/main/java/PoolGame') as the originator
  - GameManagerCaretaker.java as the caretaker
  - GameManagerState.java as the memento

##### Observer Design Pattern
- Used for telling the balls to remove themselves for the Cheat function of the game.
- Found in 'src/main/java/PoolGame/observer'
- Associated files:
	- Ball.java (in 'src/main/java/PoolGame/objects') as the concrete subscriber
	- GameManager (in 'src/main/java/PoolGame') as the client
	- BallPublisher.java as the publisher
	- BallSubscriber.java as the registry
	and
	- BallPublisherRegistry
	However this one is not explicitly part of the design pattern.

#### How to select difficulty:
You will be prompted to select a difficulty level when you start the game.
However, you can also change the difficulty level mid-game by clicking Game -> Change Difficulty

#### How to undo:
After any action in the game, you can undo it by clicking Cheat -> Undo

#### How to Cheat:
You can remove all balls of a particular colour by clicking Cheat -> Remove Colour, and selecting the colour you want to remove.

#### How to reset game:
You can reset the game by clicking Game -> Reset Game

#### How to get Javadoc:

There is already a generated javadoc, found in 'build/docs/javadoc', then accessed by opening 	'index.html' in a web browser.

Alternatively, 

You can get the Javadoc by running `gradle javadoc` inside this folder.
Then, open the index.html file in the 'build/docs/javadoc' folder.
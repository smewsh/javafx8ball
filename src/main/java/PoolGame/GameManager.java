package PoolGame;

import PoolGame.objects.*;
import PoolGame.config.*;
import PoolGame.memento.GameManagerCaretaker;
import PoolGame.memento.GameManagerState;
import PoolGame.observer.BallPublisherRegistry;
import java.util.ArrayList;

import javafx.geometry.Point2D;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.shape.Line;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.paint.Paint;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.Pane;
import javafx.scene.control.*;
import javafx.util.Duration;
import javafx.util.Pair;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
 

/**
 * Controls the game interface; drawing objects, handling logic and collisions.
 */
public class GameManager {
    private Table table;
    private MenuBar menuBar;
    private ArrayList<Ball> balls = new ArrayList<Ball>();
    private Line cue;
    private Line visibleCue;
    private boolean cueSet = false;
    private boolean cueActive = false;
    private boolean winFlag = false;
    private int score = 0;
    private double time = 0;
    public boolean restart = false;
    private GameManagerCaretaker caretaker = new GameManagerCaretaker(this);
    private BallPublisherRegistry ballPublisherRegistry = new BallPublisherRegistry();

    private final double TABLEBUFFER = Config.getTableBuffer();
    private final double TABLEEDGE = Config.getTableEdge();
    private final double FORCEFACTOR = 0.1;

    private Scene scene;
    private GraphicsContext gc;

    /**
     * Initialises timeline and cycle count.
     */
    public void run() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(17),
                t -> this.draw()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Changes difficulty of game.
     */
    public void changeDifficulty() {
        String configPath = App.checkConfig(App.askForDifficulty());
        //Reads for table data and saves to GameManager
        ReaderFactory tableFactory = new TableReaderFactory();
        Reader tableReader = tableFactory.buildReader();
        tableReader.parse(configPath, this);

        //Reads for ball data and saves to GameManager
        ReaderFactory ballFactory = new BallReaderFactory();
        Reader ballReader = ballFactory.buildReader();
        ballReader.parse(configPath, this);
        
        //Reads for pocket data and saves to GameManager
        ReaderFactory pocketFactory = new PocketReaderFactory();
        Reader pocketReader = pocketFactory.buildReader();
        pocketReader.parse(configPath, this);

        reset();
    }

    /**
     * Saves the current state of the game
     * @return the current state of the game
     */
    public GameManagerState saveState() {
        return new GameManagerState(this);
    }

    /**
     * Restores the state of the game to the given state
     * @param state the state to restore to
     */
    public void restoreState(GameManagerState state) {
        this.balls = state.getBalls();
        this.score = state.getScore();
        this.time = state.getTime();
    }



    /**
     * Builds GameManager properties such as initialising pane, canvas,
     * graphicscontext, and setting events related to clicks.
     */
    public void buildManager() {
        Pane pane = new Pane();
        setClickEvents(pane);
        this.scene = new Scene(pane, table.getxLength() + TABLEBUFFER * 2, table.getyLength() + TABLEBUFFER * 2);
        Canvas canvas = new Canvas(table.getxLength() + TABLEBUFFER * 2, table.getyLength() + TABLEBUFFER * 2);
        gc = canvas.getGraphicsContext2D();
        pane.getChildren().add(canvas);

    
        this.menuBar = createMenuBar();
        pane.getChildren().add(this.menuBar);

        reset();
    }

    /**
     * Creates the menu bar for the game.
     * @return the menu bar
     */
    public MenuBar createMenuBar() {
        Menu menuCheat = new Menu("Cheat");
        MenuItem menuCheatUndo = new MenuItem("Undo");
        Menu menuCheatRemoveColour = new Menu("Remove Colour:");


        menuCheat.getItems().addAll(menuCheatUndo, menuCheatRemoveColour);

        menuCheatRemoveColour.getItems().clear();
        for (String key : this.getBallPublisherRegistry().getPublishers().keySet()) {
            MenuItem instItem = new MenuItem();
            Circle instGraphic = new Circle(10);
            instGraphic.setFill(Paint.valueOf(key));
            instItem.setGraphic(instGraphic);
            instItem.setOnAction(e -> {
                caretaker.saveState();
                this.getBallPublisherRegistry().getPublisher(key).notifySubscribersToRemove();
            });
            menuCheatRemoveColour.getItems().add(instItem);
        }



        Menu menuGame = new Menu("Game");
        MenuItem menuGameDifficulty = new MenuItem("Change Difficulty");
        MenuItem menuGameReset = new MenuItem("Reset Game");

        menuGame.getItems().addAll(menuGameDifficulty, menuGameReset);

        menuGameDifficulty.setOnAction(e -> {
           changeDifficulty();
           menuCheatRemoveColour.getItems().clear();
           for (String key : this.getBallPublisherRegistry().getPublishers().keySet()) {
               MenuItem instItem = new MenuItem();
               Circle instGraphic = new Circle(10);
               instGraphic.setFill(Paint.valueOf(key));
               instItem.setGraphic(instGraphic);
               instItem.setOnAction(f -> {
                   caretaker.saveState();
                   this.getBallPublisherRegistry().getPublisher(key).notifySubscribersToRemove();
               });
               menuCheatRemoveColour.getItems().add(instItem);
            }
        });

        menuGameReset.setOnAction(e -> {
            reset();
        });

        menuCheatUndo.setOnAction(e -> {
            caretaker.restoreState();
        });
  
        // create a menubar
        MenuBar mb = new MenuBar();
        //mb.setUseSystemMenuBar(true);
        // add menu to menubar
        
        mb.getMenus().add(menuGame);
        mb.getMenus().add(menuCheat);
        
        return mb;
        
    }

    /**
     * Draws all relevant items - table, cue, balls, pockets - onto Canvas.
     * Used Exercise 6 as reference.
     */
    private void draw() {
        tick();

        if (!winFlag) {
            this.time += 0.017/2;
        }
        String timeString = String.format("%02d:%02d", Math.floorDiv((int)this.time,60),(int) this.time%60);
        
        

        // Fill in background
        gc.setFill(Paint.valueOf("white"));
        gc.fillRect(0, 0, table.getxLength() + TABLEBUFFER * 2, table.getyLength() + TABLEBUFFER * 2);

        gc.setFill(Paint.valueOf("black"));
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.fillText(timeString, table.getxLength()-TABLEBUFFER, TABLEBUFFER/2);

        gc.setFill(Paint.valueOf("black"));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("Score: "+this.score, (table.getxLength()+TABLEBUFFER*2)/2, TABLEBUFFER/2);

        // Fill in edges
        gc.setFill(Paint.valueOf("brown"));
        gc.fillRect(TABLEBUFFER - TABLEEDGE, TABLEBUFFER - TABLEEDGE, table.getxLength() + TABLEEDGE * 2,
                table.getyLength() + TABLEEDGE * 2);

        // Fill in Table
        gc.setFill(table.getColour());
        gc.fillRect(TABLEBUFFER, TABLEBUFFER, table.getxLength(), table.getyLength());

        // Fill in Pockets
        for (Pocket pocket : table.getPockets()) {
            gc.setFill(Paint.valueOf("black"));
            gc.fillOval(pocket.getxPos() - pocket.getRadius(), pocket.getyPos() - pocket.getRadius(),
                    pocket.getRadius() * 2, pocket.getRadius() * 2);
        }

        // Cue is now invisible tehe
        if (this.cue != null && cueActive) {
            //gc.strokeLine(cue.getStartX(), cue.getStartY(), cue.getEndX(), cue.getEndY());
            
            gc.setStroke(Color.SADDLEBROWN);
            gc.setLineWidth(6);
            gc.strokeLine(visibleCue.getStartX(),visibleCue.getStartY(),visibleCue.getEndX(),visibleCue.getEndY());

            gc.setFill(Color.SEASHELL);
            gc.fillOval(visibleCue.getStartX()-5,visibleCue.getStartY()-5,10,10);
            gc.setFill(Color.BLACK);
            gc.fillOval(visibleCue.getEndX()-3, visibleCue.getEndY()-3, 6,6);
        }

        for (Ball ball : balls) {
            if (ball.isActive()) {
                gc.setFill(ball.getColour());
                gc.fillOval(ball.getxPos() - ball.getRadius(),
                        ball.getyPos() - ball.getRadius(),
                        ball.getRadius() * 2,
                        ball.getRadius() * 2);
            }

        }

        // Win
        if (winFlag) {
            gc.setStroke(Paint.valueOf("white"));
            gc.setFont(new Font("Impact", 80));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.strokeText("Win and bye", table.getxLength() / 2 + TABLEBUFFER,
                    table.getyLength() / 2 + TABLEBUFFER);
            gc.setFont( new Font("Courier", 15));
        }

    }

    /**
     * Updates positions of all balls, handles logic related to collisions.
     * Used Exercise 6 as reference.
     */
    public void tick() {

        // Update time
        


        boolean gameInProgress = false;

        for (Ball ball : balls) {
            if (ball.isActive() && !ball.isCue()) {
                gameInProgress = true;
                break;
            }
        }

        if (!gameInProgress) {
            winFlag = true;
        }

        for (Ball ball : balls) {
            ball.tick();

            if (ball.isCue() && cueSet) {
                caretaker.saveState(); //before moving cue ball, save state
                hitBall(ball);
            }

            double width = table.getxLength();
            double height = table.getyLength();

            // Check if ball landed in pocket
            for (Pocket pocket : table.getPockets()) {
                if (pocket.isInPocket(ball) && ball.isActive()) {
                    score += ball.getStrategy().getScore();

                    if (ball.isCue()) {
                        this.reset();
                    } else {
                        if (ball.remove()) {
                            
                        } else {
                            // Check if when ball is removed, any other balls are present in its space. (If
                            // another ball is present, blue ball is removed)
                            for (Ball otherBall : balls) {
                                double deltaX = ball.getxPos() - otherBall.getxPos();
                                double deltaY = ball.getyPos() - otherBall.getyPos();
                                double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                                if (otherBall != ball && otherBall.isActive() && distance < 10) {
                                    ball.remove();
                                }
                            }
                        }
                    }
                    break;
                }
            }

            // Handle the edges (balls don't get a choice here)
            if (ball.getxPos() + ball.getRadius() > width + TABLEBUFFER) {
                ball.setxPos(width - ball.getRadius());
                ball.setxVel(ball.getxVel() * -1);
            }
            if (ball.getxPos() - ball.getRadius() < TABLEBUFFER) {
                ball.setxPos(ball.getRadius());
                ball.setxVel(ball.getxVel() * -1);
            }
            if (ball.getyPos() + ball.getRadius() > height + TABLEBUFFER) {
                ball.setyPos(height - ball.getRadius());
                ball.setyVel(ball.getyVel() * -1);
            }
            if (ball.getyPos() - ball.getRadius() < TABLEBUFFER) {
                ball.setyPos(ball.getRadius());
                ball.setyVel(ball.getyVel() * -1);
            }

            // Apply table friction
            double friction = table.getFriction();
            ball.setxVel(ball.getxVel() * friction);
            ball.setyVel(ball.getyVel() * friction);

            // Check ball collisions
            for (Ball ballB : balls) {
                if (checkCollision(ball, ballB)) {
                    Point2D ballPos = new Point2D(ball.getxPos(), ball.getyPos());
                    Point2D ballBPos = new Point2D(ballB.getxPos(), ballB.getyPos());
                    Point2D ballVel = new Point2D(ball.getxVel(), ball.getyVel());
                    Point2D ballBVel = new Point2D(ballB.getxVel(), ballB.getyVel());
                    Pair<Point2D, Point2D> changes = calculateCollision(ballPos, ballVel, ball.getMass(), ballBPos,
                            ballBVel, ballB.getMass(), false);
                    calculateChanges(changes, ball, ballB);
                }
            }
        }
    }

    /**
     * Resets the game.
     */
    public void reset() {
        for (Ball ball : balls) {
            ball.reset();
        }
        this.time = 0;
        this.score = 0;
        this.winFlag = false;
        this.menuBar = createMenuBar();

    }

    /**
     * Gets the ball publisher registry.
     * @return the ball publisher registry
     */
    public BallPublisherRegistry getBallPublisherRegistry() {
        return this.ballPublisherRegistry;
    }

    /**
     * @return scene.
     */
    public Scene getScene() {
        return this.scene;
    }

    /**
     * Sets the table of the game.
     * 
     * @param table
     */
    public void setTable(Table table) {
        this.table = table;
    }

    /**
     * @return table
     */
    public Table getTable() {
        return this.table;
    }

    /**
     * Gets the balls in the game.
     * @return the list of balls
     */
    public ArrayList<Ball> getBalls() {
        return this.balls;
    }

    /**
     * Gets the time.
     * @return the time
     */
    public double getTime() {
        return this.time;
    }

    /**
     * Gets the score.
     * @return the score
     */
    public int getScore() {
        return this.score;
    }



    /**
     * Sets the balls of the game.
     * 
     * @param balls
     */
    public void setBalls(ArrayList<Ball> balls) {
        this.balls = balls;
    }

    /**
     * Hits the ball with the cue, distance of the cue indicates the strength of the
     * strike.
     * 
     * @param ball
     */
    private void hitBall(Ball ball) {
        double deltaX = ball.getxPos() - cue.getStartX();
        double deltaY = ball.getyPos() - cue.getStartY();
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        // Check that start of cue is within cue ball
        if (distance < ball.getRadius()) {
            // Collide ball with cue
            double hitxVel = (cue.getStartX() - cue.getEndX()) * FORCEFACTOR;
            double hityVel = (cue.getStartY() - cue.getEndY()) * FORCEFACTOR;

            ball.setxVel(hitxVel);
            ball.setyVel(hityVel);
        }

        cueSet = false;

    }

    /**
     * Changes values of balls based on collision (if ball is null ignore it)
     * 
     * @param changes
     * @param ballA
     * @param ballB
     */
    private void calculateChanges(Pair<Point2D, Point2D> changes, Ball ballA, Ball ballB) {
        ballA.setxVel(changes.getKey().getX());
        ballA.setyVel(changes.getKey().getY());
        if (ballB != null) {
            ballB.setxVel(changes.getValue().getX());
            ballB.setyVel(changes.getValue().getY());
        }
    }

    /**
     * Sets the cue to be drawn on click, and manages cue actions
     * 
     * @param pane
     */
    private void setClickEvents(Pane pane) {
        pane.setOnMousePressed(event -> {
            cue = new Line(event.getX(), event.getY(), event.getX(), event.getY());

            
            
            visibleCue = new Line();
            
            cueSet = false;
            cueActive = true;
        });

        pane.setOnMouseDragged(event -> {
            cue.setEndX(event.getX());
            cue.setEndY(event.getY());
            int cueLength = 200;
            double cueAngle = Math.atan2(cue.getEndY() - cue.getStartY(), cue.getEndX() - cue.getStartX());
            visibleCue.setStartX(event.getX());
            visibleCue.setStartY(event.getY());
            double cueX = cue.getEndX() + Math.cos(cueAngle) * cueLength;
            double cueY = cue.getEndY() + Math.sin(cueAngle) * cueLength;
            visibleCue.setEndX(cueX);
            visibleCue.setEndY(cueY);
        });

        pane.setOnMouseReleased(event -> {
            cueSet = true;
            cueActive = false;
        });
    }

    /**
     * Checks if two balls are colliding.
     * Used Exercise 6 as reference.
     * 
     * @param ballA
     * @param ballB
     * @return true if colliding, false otherwise
     */
    private boolean checkCollision(Ball ballA, Ball ballB) {
        if (ballA == ballB) {
            return false;
        }

        return Math.abs(ballA.getxPos() - ballB.getxPos()) < ballA.getRadius() + ballB.getRadius() &&
                Math.abs(ballA.getyPos() - ballB.getyPos()) < ballA.getRadius() + ballB.getRadius();
    }

    /**
     * Collision function adapted from assignment, using physics algorithm:
     * http://www.gamasutra.com/view/feature/3015/pool_hall_lessons_fast_accurate_.php?page=3
     *
     * @param positionA The coordinates of the centre of ball A
     * @param velocityA The delta x,y vector of ball A (how much it moves per tick)
     * @param massA     The mass of ball A (for the moment this should always be the
     *                  same as ball B)
     * @param positionB The coordinates of the centre of ball B
     * @param velocityB The delta x,y vector of ball B (how much it moves per tick)
     * @param massB     The mass of ball B (for the moment this should always be the
     *                  same as ball A)
     *
     * @return A Pair in which the first (key) Point2D is the new
     *         delta x,y vector for ball A, and the second (value) Point2D is the
     *         new delta x,y vector for ball B.
     */
    public static Pair<Point2D, Point2D> calculateCollision(Point2D positionA, Point2D velocityA, double massA,
            Point2D positionB, Point2D velocityB, double massB, boolean isCue) {

        // Find the angle of the collision - basically where is ball B relative to ball
        // A. We aren't concerned with
        // distance here, so we reduce it to unit (1) size with normalize() - this
        // allows for arbitrary radii
        Point2D collisionVector = positionA.subtract(positionB);
        collisionVector = collisionVector.normalize();

        // Here we determine how 'direct' or 'glancing' the collision was for each ball
        double vA = collisionVector.dotProduct(velocityA);
        double vB = collisionVector.dotProduct(velocityB);

        // If you don't detect the collision at just the right time, balls might collide
        // again before they leave
        // each others' collision detection area, and bounce twice.
        // This stops these secondary collisions by detecting
        // whether a ball has already begun moving away from its pair, and returns the
        // original velocities
        if (vB <= 0 && vA >= 0 && !isCue) {
            return new Pair<>(velocityA, velocityB);
        }

        // This is the optimisation function described in the gamasutra link. Rather
        // than handling the full quadratic
        // (which as we have discovered allowed for sneaky typos)
        // this is a much simpler - and faster - way of obtaining the same results.
        double optimizedP = (2.0 * (vA - vB)) / (massA + massB);

        // Now we apply that calculated function to the pair of balls to obtain their
        // final velocities
        Point2D velAPrime = velocityA.subtract(collisionVector.multiply(optimizedP).multiply(massB));
        Point2D velBPrime = velocityB.add(collisionVector.multiply(optimizedP).multiply(massA));

        return new Pair<>(velAPrime, velBPrime);
    }
}

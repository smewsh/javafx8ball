package PoolGame;

import PoolGame.config.*;

import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;
import java.util.Optional;
import javafx.scene.control.ChoiceDialog;



/** Main application entry point. */
public class App extends Application {
    GameManager gameManager;
    Stage stage;
    /**
     * @param args First argument is the path to the config file
     */
    public static void main(String[] args) {
        launch(args);

    }


    @Override
    /**
     * Starts the application.
     * 
     * @param primaryStage The primary stage for the application.
     */
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        // READ IN CONFIG
        GameManager gameManager = new GameManager();
        String configPath = checkConfig(askForDifficulty());

        //Reads for table data and saves to GameManager
        ReaderFactory tableFactory = new TableReaderFactory();
        Reader tableReader = tableFactory.buildReader();
        tableReader.parse(configPath, gameManager);

        //Reads for ball data and saves to GameManager
        ReaderFactory ballFactory = new BallReaderFactory();
        Reader ballReader = ballFactory.buildReader();
        ballReader.parse(configPath, gameManager);
        
        //Reads for pocket data and saves to GameManager
        ReaderFactory pocketFactory = new PocketReaderFactory();
        Reader pocketReader = pocketFactory.buildReader();
        pocketReader.parse(configPath, gameManager);

        gameManager.buildManager();


        // START GAME MANAGER
        gameManager.run();
        primaryStage.setTitle("Pool");
        primaryStage.setScene(gameManager.getScene());
        primaryStage.show();
        gameManager.run();
    }


    /**
     * Associates a given difficulty with a config file.
     * 
     * @param choice The difficulty chosen by the user.
     * @return config path.
     */
    protected static String checkConfig(String choice) {
        String configPath;
        if (choice.toLowerCase().equals("hard")) {
            configPath = "src/main/resources/config_hard.json";
        } else if (choice.toLowerCase().equals("easy")) {
            configPath = "src/main/resources/config_easy.json";
        } else {
            configPath = "src/main/resources/config_normal.json";
        }
        return configPath;
    }

    /**
     * Asks the user for the difficulty of the game via a prompt.
     * @return the difficulty of the game.
     */
    protected static String askForDifficulty() {
        List<String> choices = List.of("Easy", "Normal", "Hard");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Normal", choices);
        dialog.setTitle("Difficulty");
        dialog.setHeaderText("Select Difficulty");
        dialog.setContentText("Choose your difficulty:");

        Optional<String> result = dialog.showAndWait();
        
        if (result.isPresent()){
            return result.get();
        }
        return "Normal";
    }
}

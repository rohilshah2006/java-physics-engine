package flipracer;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

import engine.*;

public class FlipracerGame extends Application {
	private final int origTimeRemaining = 120;
	private Label timerLabel;
	private int timeRemaining = origTimeRemaining; // originally 120
    private Timeline timeline;
    private List<FinishLineFlag> finishLineFlags = new ArrayList<>();
    
    private boolean[] unlockedLevels = {true, false, false}; // Level 1 is unlocked by default
    private boolean[] completedLevels = {false, false, false}; // None of the levels are completed by default
    
	int levelWidth = 1440;
	int levelHeight = 845;
	
	private Stage primaryStage;
	private static FlipracerGame instance;
	
	private FlipracerWorld world;
	private FlipracerWorld currentWorld;
    private Button level2Button;
    private Button level3Button;
    private MediaPlayer mediaPlayer;
    private FlipracerWorld test;
    private BackgroundMusic bgMusic;
    
    private Image menuBackground = new Image("flipracer_resources/mainmenubackground.png", levelWidth, levelHeight, false, false);
    private Image level1background = new Image("flipracer_resources/level1background.jpg", levelWidth, levelHeight, false, false);
    private Image level2background = new Image("flipracer_resources/level2background.jpg", levelWidth, levelHeight, false, false);
    private Image level3background = new Image("flipracer_resources/level3background.jpg", levelWidth, levelHeight, false, false);
    
    private boolean timerEnded = false;
    
    protected int currentLevel = 0;
    
    	
    @Override
    public void start(Stage stage) {
    	this.primaryStage = stage;
    	instance = this; 
    	
        primaryStage.setTitle("FlipRacer Game");
        primaryStage.setResizable(false);
        showStartMenu();

        primaryStage.show();        
    }
    
    public static FlipracerGame getInstance() {
        return instance;
    }
    
    private void fadeToBlackAndBack(Node currentRoot, Node newRoot, double fadeInDuration, double fadeOutDuration, Runnable onFinished) {
        Rectangle fadeRect = new Rectangle(levelWidth, levelHeight, Color.BLACK);
        fadeRect.setOpacity(0);
        ((Pane) currentRoot).getChildren().add(fadeRect);

        FadeTransition fadeToBlack = new FadeTransition(Duration.seconds(fadeInDuration), fadeRect);
        fadeToBlack.setFromValue(0);
        fadeToBlack.setToValue(1);
        fadeToBlack.setOnFinished(event -> {
            onFinished.run();
            ((Pane) currentRoot).getChildren().remove(fadeRect); // Remove from old scene

            // Add fade rectangle to new root and fade out
            ((Pane) newRoot).getChildren().add(fadeRect);
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(fadeOutDuration), fadeRect);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(event2 -> ((Pane) newRoot).getChildren().remove(fadeRect));
            fadeOut.play();
        });

        fadeToBlack.play();
    }
    
    private void updateButtonLockState(Button button, boolean isLocked) {
        if (isLocked) {
        	button.setDisable(true);
            ImageView lockIcon = new ImageView(ImageCache.getImage("flipracer_resources/lock.png"));
            lockIcon.setFitWidth(20); // Set width for lock icon
            lockIcon.setFitHeight(20);
            button.setGraphic(lockIcon);
        } else {
            button.setDisable(false); // Remove lock icon if unlocked
            button.setGraphic(null);
        }
    }
    
    public void completeLevel(int currentLevel) {
    	completedLevels[currentLevel] = true; 	
    	switch (currentLevel) {
        case 0:
        	unlockedLevels[1] = true;
            updateButtonLockState(level2Button, false);
            break;
        case 1:
        	unlockedLevels[2] = true;
        	updateButtonLockState(level3Button, false);
        
    	}

    }
    
    private void startLevel(int level) {
    	currentLevel = level;
        resetTimer();
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: SKYBLUE;");
        currentWorld = new FlipracerWorld();
        currentWorld.setFlipracerGame(this);
        root.setCenter(currentWorld);

        Scene scene = new Scene(root, levelWidth, levelHeight);
        primaryStage.setScene(scene);
        startTimer();
        currentWorld.startLevel(level); // Call startLevel on the currentWorld instance
    }
    
    private void showStartMenu() { 	
    	BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(menuBackground, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
    	
    	Button startButton = new Button("Start game");
    	Button howToPlayButton = new Button("How to play");
    	
    	VBox buttonBox = new VBox(10, startButton, howToPlayButton);
        buttonBox.setAlignment(Pos.CENTER);
        
        StackPane startPane = new StackPane(buttonBox);
        startPane.setBackground(new Background(backgroundImage));
        Scene startScene = new Scene(startPane, levelWidth, levelHeight, Color.SKYBLUE);

        startButton.setOnAction(event -> showLevelSelector());
        howToPlayButton.setOnAction(event -> showHowToPlay());

        primaryStage.setScene(startScene);
    }
    
    private void showHowToPlay() {
    	BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(menuBackground, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        
        Image howToPlayImage = new Image("flipracer_resources/HowToPlay.png");
        ImageView imageView = new ImageView(howToPlayImage);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(levelWidth - 200);  
        imageView.setFitHeight(levelHeight - 200);
        
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> showStartMenu());
        
        VBox vbox = new VBox(10, imageView, backButton);
        vbox.setAlignment(Pos.CENTER);

        StackPane howToPlayPane = new StackPane(vbox);
        howToPlayPane.setBackground(new Background(backgroundImage));
        Scene howToPlayScene = new Scene(howToPlayPane, levelWidth, levelHeight, Color.SKYBLUE);

        primaryStage.setScene(howToPlayScene);
    }
    
    public void unlockNextLevel() {
        for (int i = 0; i < unlockedLevels.length; i++) {
            if (!unlockedLevels[i]) {
                unlockedLevels[i] = true;
                break;
            }
        }
        showLevelSelector(); // Refresh the level selector menu
    }
    
    public List<FinishLineFlag> getObjects(Class<FinishLineFlag> cls) {
        List<FinishLineFlag> objects = new ArrayList<>();
        for (FinishLineFlag flag : finishLineFlags) {
            if (cls.isInstance(flag)) {
                objects.add(flag);
            }
        }
        return objects;
    }
    
    void showLevelSelector() {
    	Button level1Button = new Button("Level 1");
    	level2Button = new Button("Level 2"); // Initialize level2Button here
    	level3Button = new Button("Level 3");
    	
    	StackPane level1Pane = new StackPane(level1Button);
        level1Pane.setStyle("-fx-background-color: SKYBLUE;");
        level1Button.setOnAction(event -> {   
        	playLevelSelectSound();
        	startLevel1();
        });
        
        StackPane level2Pane = new StackPane(level2Button);
        level2Pane.setStyle("-fx-background-color: SKYBLUE;");        
        level2Button.setOnAction(event -> {
            if (unlockedLevels[1]) {
            	playLevelSelectSound();
                startLevel2();
            }
        });
        
        
        StackPane level3Pane = new StackPane(level3Button);     
        //StackPane level3Pane = createLevelButton("Level 3", 2);
        level3Pane.setStyle("-fx-background-color: SKYBLUE;");
        level3Button.setOnAction(event -> {
            if (unlockedLevels[2]) {
            	playLevelSelectSound();
                startLevel3();
            }
        });
        
        
        updateButtonLockState(level2Button, !unlockedLevels[1]);
        updateButtonLockState(level3Button, !unlockedLevels[2]);
        
        
        
        VBox layout = new VBox(10);
        layout.getChildren().addAll(level1Pane, level2Pane, level3Pane);
        layout.setAlignment(Pos.CENTER); // Center the buttons
        layout.setStyle("-fx-background-color: SKYBLUE;");

        StackPane levelPane = new StackPane(layout);
        levelPane.setStyle("-fx-background-color: SKYBLUE;");
        Scene levelSelectorScene = new Scene(levelPane, levelWidth, levelHeight, Color.SKYBLUE);
        
        

        fadeToBlackAndBack(primaryStage.getScene().getRoot(), levelPane, 0.5, 0.5, () -> {
            primaryStage.setScene(levelSelectorScene);
        });
        
    }
    
    private void playLevelSelectSound() {
        String soundPath = Paths.get("src/LevelSelect.mp3").toUri().toString();
        Media sound = new Media(soundPath);
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.dispose(); 
        }); 
    }
    
    public BackgroundMusic getBackgroundMusic() {
    	return bgMusic;
    }
    
    private void startLevel1() {
    	currentLevel = 1;
    	
    	BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(level1background, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
    	
        BorderPane root = new BorderPane();
    	root.setBackground(new Background(backgroundImage));
        test = new FlipracerWorld();
        test.setFlipracerGame(this);
        root.setCenter(test);
        test.handleLoading();
        test.moveObjectToTop(test.getPlayerBall());
        
        CheckpointFlag firstCheckpoint = test.getObjects(CheckpointFlag.class).get(0);
		
		test.getPlayerBall().setX(firstCheckpoint.getX());
		test.getPlayerBall().setY(firstCheckpoint.getY());
        
        timerLabel = new Label();
        timerLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");
        BorderPane.setAlignment(timerLabel, Pos.TOP_RIGHT);
        root.setTop(timerLabel);
        
        StackPane topContainer = new StackPane(); 
        addReturnButton(topContainer); 
        topContainer.getChildren().add(timerLabel);
        StackPane.setAlignment(timerLabel, Pos.TOP_RIGHT);
        root.setTop(topContainer);
        
        resetTimer();

        Scene gameScene = new Scene(root, levelWidth, levelHeight, Color.SKYBLUE);
        
        bgMusic = new BackgroundMusic();
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> bgMusic.playbg1());
        delay.play();

        fadeToBlackAndBack(primaryStage.getScene().getRoot(), root, 0.5, 0.5, () -> {
            primaryStage.setScene(gameScene);
            test.start();
        });
    }
    
    private void startLevel2() {
    	currentLevel = 2;
    	if (unlockedLevels[1]) {
    		
    		BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
            BackgroundImage backgroundImage = new BackgroundImage(level2background, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
    		
    		BorderPane root = new BorderPane();
    		root.setBackground(new Background(backgroundImage));
    		test = new FlipracerWorld();
    		test.setFlipracerGame(this);
    		root.setCenter(test);
    		test.handleLoading();
    		test.moveObjectToTop(test.getPlayerBall());
    		
    		CheckpointFlag firstCheckpoint = test.getObjects(CheckpointFlag.class).get(0);
    		
    		test.getPlayerBall().setX(firstCheckpoint.getX());
    		test.getPlayerBall().setY(firstCheckpoint.getY());
        
    		timerLabel = new Label();
    		timerLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");
    		BorderPane.setAlignment(timerLabel, Pos.TOP_RIGHT);
    		root.setTop(timerLabel);
    		
    		StackPane topContainer = new StackPane(); 
            addReturnButton(topContainer); 
            topContainer.getChildren().add(timerLabel);
            StackPane.setAlignment(timerLabel, Pos.TOP_RIGHT);
            root.setTop(topContainer);
        
    		resetTimer();

    		Scene gameScene = new Scene(root, levelWidth, levelHeight, Color.MAROON);
    		
    		bgMusic = new BackgroundMusic();
            PauseTransition delay = new PauseTransition(Duration.seconds(3));
            delay.setOnFinished(event -> bgMusic.playbg2());
            delay.play();

    		fadeToBlackAndBack(primaryStage.getScene().getRoot(), root, 0.5, 0.5, () -> {
                primaryStage.setScene(gameScene);
                test.start();
            });
    	}
    }
    
    private void startLevel3() {
    	currentLevel = 3;
    	if (unlockedLevels[2]) {
    		
    		BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
            BackgroundImage backgroundImage = new BackgroundImage(level3background, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
    		
    		BorderPane root = new BorderPane();
    		root.setBackground(new Background(backgroundImage));
    		test = new FlipracerWorld();
    		test.setFlipracerGame(this);
    		root.setCenter(test);
    		test.handleLoading();
    		test.moveObjectToTop(test.getPlayerBall());
    		
    		CheckpointFlag firstCheckpoint = test.getObjects(CheckpointFlag.class).get(0);
    		
    		test.getPlayerBall().setX(firstCheckpoint.getX());
    		test.getPlayerBall().setY(firstCheckpoint.getY());
        
    		timerLabel = new Label();
    		timerLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");
    		BorderPane.setAlignment(timerLabel, Pos.TOP_RIGHT);
    		root.setTop(timerLabel);
    		
    		StackPane topContainer = new StackPane();
            addReturnButton(topContainer); 
            topContainer.getChildren().add(timerLabel);
            StackPane.setAlignment(timerLabel, Pos.TOP_RIGHT);
            root.setTop(topContainer);
        
    		resetTimer();

    		Scene gameScene = new Scene(root, levelWidth, levelHeight, Color.BLACK);
    		
    		bgMusic = new BackgroundMusic();
            PauseTransition delay = new PauseTransition(Duration.seconds(3));
            delay.setOnFinished(event -> bgMusic.playbg3());
            delay.play();

    		fadeToBlackAndBack(primaryStage.getScene().getRoot(), root, 0.5, 0.5, () -> {
                primaryStage.setScene(gameScene);
                test.start();
            });
    	}
    }
    
    
    private void resetTimer() {
        timeRemaining = origTimeRemaining; // Reset to 2 minutes
        timerEnded = false;
        if (timeline != null) {
            timeline.stop();
        }
        
        if (timerLabel != null) {
            timerLabel.setText(formatTime(timeRemaining));
        } else {
            System.out.println("Timer label is null");
        }
        
        
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeRemaining--;
            timerLabel.setText(formatTime(timeRemaining));
            if (timeRemaining <= 0 && !timerEnded) {
            	timerEnded = true;
            	clearWorldResources();
                showLevelSelector();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
       
        
    }
    
    public void clearWorldResources() {
        if (test != null) {
        	System.out.println("Clearing resources for the current world: " + test);
            test.clearCurrentLevelResources();
        } else {
        	System.out.println("The current world is null");
        }
    }

    private void startTimer() {
    	BorderPane rootyFruity = new BorderPane();
    	timerLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");
        BorderPane.setAlignment(timerLabel, Pos.TOP_RIGHT);
        rootyFruity.setTop(timerLabel);
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeRemaining--;
            timerLabel.setText(formatTime(timeRemaining));
            if (timeRemaining <= 0) {
                showLevelSelector();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }
    
    private StackPane createLevelButton(String levelName, int levelIndex) {        
    	Button levelButton = new Button(levelName);
    	levelButton.setPrefSize(200, 50);
        StackPane levelPane = new StackPane(levelButton);
        levelPane.setStyle("-fx-background-color: SKYBLUE;");
        updateButtonLockState(levelButton, !unlockedLevels[levelIndex]);

        if (unlockedLevels[levelIndex]) {
            levelButton.setOnAction(event -> startLevel(levelIndex));
        } else {
            levelButton.setOnAction(null);
        }
        
        return levelPane;
    }
    
    public int getCurrentLevel() {
    	return currentLevel;
    }
    
    private void returnToLevelSelector() {
        if (test != null) {
            test.clearCurrentLevelResources();
        }
        showLevelSelector();
    } 
    
    private Button createReturnButton() {
        Button returnButton = new Button("Return to Level Selector");
        returnButton.setOnAction(event -> returnToLevelSelector());
        returnButton.setStyle("-fx-background-color: white; -fx-text-fill: black;");
        returnButton.setAlignment(Pos.TOP_LEFT);
        return returnButton;
    }
    
    private void addReturnButton(Pane root) {
    	Button returnButton = createReturnButton();
        VBox.setMargin(returnButton, new Insets(10, 0, 0, 10)); // Adjust position
        root.getChildren().add(returnButton);
    }



    public static void main(String[] args) {
        launch(args);
    }
}
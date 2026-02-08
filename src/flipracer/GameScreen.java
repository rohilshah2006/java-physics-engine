package flipracer;

import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Pos;

public class GameScreen extends BorderPane {
    private Button startButton;
    private Text levelCompleteText;
    private Button nextLevelButton;

    public GameScreen() {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);

        startButton = new Button("Start");
        vbox.getChildren().add(startButton);

        levelCompleteText = new Text("Level Complete");
        levelCompleteText.setVisible(false);
        vbox.getChildren().add(levelCompleteText);

        nextLevelButton = new Button("Next Level");
        nextLevelButton.setVisible(false);
        vbox.getChildren().add(nextLevelButton);

        setCenter(vbox);
    }

    public Button getStartButton() {
        return startButton;
    }

    public void showLevelComplete() {
        startButton.setVisible(false);
        levelCompleteText.setVisible(true);
        nextLevelButton.setVisible(true);
    }
}
package flipracer;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class LockedButton extends StackPane{
	private Button button;
    private ImageView lockOverlay;

    public LockedButton(String buttonText, String lockImagePath) {
        button = new Button(buttonText);

        Image lockImage = new Image(getClass().getClassLoader().getResource(lockImagePath).toString());
        lockOverlay = new ImageView(lockImage);
        lockOverlay.setFitWidth(50);  
        lockOverlay.setFitHeight(50); 

        // Center the lock overlay on the button
        lockOverlay.setPreserveRatio(true);

        getChildren().addAll(button, lockOverlay);
    }

    public Button getButton() {
        return button;
    }
}

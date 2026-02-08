package flipracer;

import java.io.File;

import engine.*;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

public class CheckpointFlag extends Circle {	
	public static String path = "flipracer_resources/flag1.png";
	private static AudioClip checkpointSound;
	
	static {
        try {
            File soundFile = new File("src/checkpointReached.mp3");
            if (soundFile.exists()) {
                checkpointSound = new AudioClip(soundFile.toURI().toString());
            } else {
                System.err.println("Sound file not found: " + soundFile.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CheckpointFlag(int size) {
        super(size, path);
    }
    
    public void act(long now) {
    	
    }
    
    public void playCheckpointSound() {
        if (checkpointSound != null) {
            checkpointSound.play();
        }
    }
	
}


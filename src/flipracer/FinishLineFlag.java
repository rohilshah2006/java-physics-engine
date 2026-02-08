package flipracer;

import java.io.File;

import engine.Actor;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class FinishLineFlag extends Circle {
	public static String path = "flipracer_resources/FlagFin.png";
	private boolean cooldown = false;
	private boolean completed = false;
	private final int cooldownDuration = 60; // Cooldown duration in frames
    private int cooldownTimer = 0;
    private MediaPlayer mediaPlayer;
	

    public FinishLineFlag(int size) {
        super(size,path);
        String musicFile = "src/LevelFinish.mp3"; 
        Media sound = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
    }
    
    public boolean touching(PlayerBall ball) {
        return this.getBoundsInParent().intersects(ball.getBoundsInParent());
    }

    @Override
    public void act(long now) {
    	if (cooldown) {
            if (cooldownTimer < cooldownDuration) {
                cooldownTimer++;
            } else {
                cooldown = false;
                cooldownTimer = 0;
            }
        }
    	
        // Check collision with PlayerBall
        PlayerBall playerBall = getOneIntersectingObject(PlayerBall.class);
        if (playerBall != null && !cooldown) {
            // Handle flag touch
            ((FlipracerWorld) getWorld()).finishLevel();
            cooldown = true;
            completed = true;
            mediaPlayer.play();
        }
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    //public void setCompleted(boolean completed) {
        //this.completed = completed;
    //}

}

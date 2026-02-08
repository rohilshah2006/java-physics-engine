package flipracer;

import javafx.scene.input.KeyCode;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

import engine.Actor;

public class PlayerBall extends GravityBall{
	
	private final int moveVector = 3;
	private final int maxMoveVelocity = 2;
	private boolean isPlaying = false;
	
	private MediaPlayer mediaPlayer;
	
	private double checkpointX = 0;
    private double checkpointY = 0;
    
    private CheckpointFlag currentCheckpoint = null;
    private FlipracerWorld flipworld;
	

	public PlayerBall(int s, String path) {
		super(s, path);
		
		String musicFile = "src/ballRolling.mp3"; 
        Media sound = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        
        checkpointX = getCenterX();
        checkpointY = getCenterY();
	}
	
	@Override
	public void act(long now) {
		super.act(now);
		
		 boolean isDPressed = getWorld().isKeyPressed(KeyCode.D);
	     boolean isAPressed = getWorld().isKeyPressed(KeyCode.A);
		
		
		if (isDPressed) {
			if (vx < maxMoveVelocity) {
				vx += moveVector; 
			}	
			
        }
		
		if (isAPressed) {
			if (vx > -maxMoveVelocity) {
				vx -= moveVector; 
			}
		}
		
		if (isDPressed && isAPressed) {
			vx = 0;
		}
		
		if (isTouchingFloorOrPlatform()) {
			if (isDPressed || isAPressed) {
				if (mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
					mediaPlayer.play();
				}
			}else{
				if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
					mediaPlayer.stop();
				}		
			}
		}
		
		if (!isTouchingFloorOrPlatform() || (isDPressed && isAPressed)) {
			mediaPlayer.stop();
		}

		setRotate(getRotate() + vx);
		
		CheckpointFlag flag = getOneIntersectingObject(CheckpointFlag.class);
        if (flag != null && flag != currentCheckpoint) {
        	currentCheckpoint = flag;
            setCheckpoint(flag.getCenterX(), flag.getCenterY());
            flag.playCheckpointSound();
        }
		
	}
	
	public void stopRollingSound() {
	    if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
	        mediaPlayer.stop();
	    }
	}
	
	private boolean isTouchingFloorOrPlatform() {
        // This method should return true if the ball is touching the floor or a platform
        for (Actor actor : getWorld().getObjects(Actor.class)) {
            if (actor instanceof Platform || actor instanceof FixedBall) {
                if (this.getBoundsInParent().intersects(actor.getBoundsInParent())) {
                    return true;
                }
            }
        }
        return false;
    }
	
	public void setCheckpoint(double x, double y) {
        checkpointX = x;
        checkpointY = y;
    }
	
	public void respawn() {
        setCenter(checkpointX, checkpointY);
        setVelocity(0, 0);
    }
	
	public void reset() {
		setCenter(0, 0);
		setVelocity(0, 0);
	}

}

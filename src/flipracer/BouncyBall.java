package flipracer;

import java.io.File;
import java.util.List;

import javafx.scene.media.AudioClip;

public class BouncyBall extends FixedBall{
	private static String path = "flipracer_resources/bouncyball.png";
	private static AudioClip bounceSound;
	
	private boolean isTouchingPlayerBall = false;
	
	static {
        try {
            File soundFile = new File("src/bounce.mp3");
            if (soundFile.exists()) {
                bounceSound = new AudioClip(soundFile.toURI().toString());
            } else {
                System.err.println("Sound file not found: " + soundFile.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public BouncyBall(int s, double b) {
		super(s, path, b);
		// TODO Auto-generated constructor stub
	}
	
	public void act(long now) {
		List<PlayerBall> playerBalls = getWorld().getObjects(PlayerBall.class);
        boolean currentlyTouching = false;
        
        for (PlayerBall playerBall : playerBalls) {
            if (this.getBoundsInParent().intersects(playerBall.getBoundsInParent())) {
                currentlyTouching = true;
                if (!isTouchingPlayerBall && bounceSound != null) {
                    bounceSound.play();
                }
                break;
            }
        }

        isTouchingPlayerBall = currentlyTouching;
	}
}

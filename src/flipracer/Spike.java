package flipracer;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class Spike extends Circle{

	public static String path = "flipracer_resources/spike.png";
	
	private int rotation;
	private MediaPlayer stabSoundPlayer;
	
	public Spike(int s, int r) {
		super(s, path);
		rotation = r;
		setRotate(rotation);
		
		String stabSoundFile = "src/stab.mp3";
        Media stabSound = new Media(new File(stabSoundFile).toURI().toString());
        stabSoundPlayer = new MediaPlayer(stabSound);
	}
	
	public void act(long now) {
		PlayerBall b = getWorld().getObjects(PlayerBall.class).get(0);
		
		if (touching(b)) {
			playStabSound();
			b.respawn();
		}
	}
	
	private void playStabSound() {
        if (stabSoundPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
            stabSoundPlayer.play();
        } else {
            stabSoundPlayer.stop();
            stabSoundPlayer.play();
        }
    }
	
	@Override
	public void setCenter(double x, double y) {
		super.setCenter(x, y);
	}
	
	public int getRotation() {
		return rotation;
		
	}

}

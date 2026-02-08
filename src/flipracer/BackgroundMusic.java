package flipracer;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class BackgroundMusic {
	
	private MediaPlayer audio1Player;
	private MediaPlayer audio2Player;
	private MediaPlayer audio3Player;
	
	public BackgroundMusic() {
		
	}
	
	public void playbg1() {
		String audio1File = "src/audio1.mp3";
		Media audio1Sound = new Media(new File(audio1File).toURI().toString());
		audio1Player = new MediaPlayer(audio1Sound);
		
		audio1Player.setOnEndOfMedia(() -> audio1Player.seek(javafx.util.Duration.ZERO)); 
        audio1Player.play();
	}
	
	public void playbg2() {
		String audio2File = "src/audio2.mp3";
		Media audio2Sound = new Media(new File(audio2File).toURI().toString());
		audio2Player = new MediaPlayer(audio2Sound);
		
		audio2Player.setOnEndOfMedia(() -> audio2Player.seek(javafx.util.Duration.ZERO)); 
        audio2Player.play();
	}
	
	public void playbg3() {
		String audio3File = "src/audio3.mp3";
		Media audio3Sound = new Media(new File(audio3File).toURI().toString());
		audio3Player = new MediaPlayer(audio3Sound);
		
		audio3Player.setOnEndOfMedia(() -> audio3Player.seek(javafx.util.Duration.ZERO)); 
        audio3Player.play();
	}
	
	public void stopBackgroundMusic() {
		if (audio1Player != null && audio1Player.getStatus() == MediaPlayer.Status.PLAYING) {
            audio1Player.stop();
        }
        if (audio2Player != null && audio2Player.getStatus() == MediaPlayer.Status.PLAYING) {
            audio2Player.stop();
        }
        if (audio3Player != null && audio3Player.getStatus() == MediaPlayer.Status.PLAYING) {
            audio3Player.stop();
        }
	}
}


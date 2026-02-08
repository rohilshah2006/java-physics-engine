package flipracer;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Portal extends Circle{
    private Portal linkedPortal;
    private MediaPlayer teleportSoundPlayer;

    public Portal(int size, String path) {
        super(size,path);
        String stabSoundFile = "src/teleport.mp3";
        Media teleportSound = new Media(new File(stabSoundFile).toURI().toString());
        teleportSoundPlayer = new MediaPlayer(teleportSound);
    }

    public void setLinkedPortal(Portal linkedPortal) {
        this.linkedPortal = linkedPortal;
    }

    public Portal getLinkedPortal() {
        return linkedPortal;
    }
    
    private void playStabSound() {
        if (teleportSoundPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
            teleportSoundPlayer.play();
        } else {
            teleportSoundPlayer.stop();
            teleportSoundPlayer.play();
        }
    }

    public void teleport(PlayerBall playerBall) {
    	if (linkedPortal != null) {
            //System.out.println("Teleporting ball to linked portal at: (" + linkedPortal.getCenterX() + ", " + linkedPortal.getCenterX() + ")");
            playerBall.setCenter(linkedPortal.getCenterX(), linkedPortal.getCenterY());
            playStabSound();
            
        } else {
            System.out.println("No linked portal found");
        }
    }
}
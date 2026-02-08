package flipracer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import engine.*;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/*
GOOD PLATFORM SETTINGS:
- fixedBallSize is 40% of ballSize
- platformOffset =  fixedBallSize + 10
- platformBounciness = 0.7
- spikeSize for spawned spikes is 30 minus the spikeSize of spikes on the bottom
- moveVector in playerBall = 3, this lets it move smoothly on platforms
*/ 


public class FlipracerWorld extends World{
	private int ballSize = 40; //80 
	private int fixedBallSize = 5; //5
	
	private double fixedBallBounciness = 1; //1
	private double platformBounciness = 0.7; //0.7
	
	private int platformWidth = 100; //50
	private int platformHeight = 25; //25
	
	private int spikeSize = 50;
	private int spikeRadius = spikeSize/2;
	
	private int bouncyBallSize = 150;
	private int bouncyBallBounciness = 10;
	
	private boolean currentlyLoading = false;
	private boolean currentlySaving = false;
	
	private final int saveLoadCooldown = 60; 
	private int saveLoadTimer = 0;
	
	private int flagSize = 30;
	private int finishLineFlagSize = 50;
	
	private PlayerBall playerBall;
	
	private int defaultSpikeRotation = 0;
	private int spikeRotation = defaultSpikeRotation;
	private int SRotationTick = 45;
	
	private final int spikeRotateCooldown = 30;
	private int spikeRotateTimer = 0;
	
	public int ballStartX = ballSize*2;
	public int ballStartY = ballSize*2;
	
	private final int platformOffset = 12; 
	private FlipracerGame flipracerGame;
	private int bluePortalSize = 100;
	private int orangePortalSize = 90;
	private List<Portal> portals = new ArrayList<>();
	
	private final int portalCooldown = 30;
	private int portalTimer = 0;
	
	public final boolean GOD_POWERS = false; //for creating levels, set to false when submitting
	
	
	public FlipracerWorld() {
		
		playerBall = new PlayerBall(ballSize, "flipracer_resources/tennisball.png");
		playerBall.setCenter(ballStartX, ballStartY);
		add(playerBall);

		moveObjectToTop(playerBall);
		
		portals = new ArrayList<>();
	}
	
	public void setFlipracerGame(FlipracerGame flipracerGame) {
        this.flipracerGame = flipracerGame;
    }
	
	@Override
	public void act(long now) {
		if (GOD_POWERS) {
			if (isKeyPressed(KeyCode.LEFT) && spikeRotateTimer == 0) { //rotating spikes left
				spikeRotation -= SRotationTick;
				spikeRotateTimer = spikeRotateCooldown;
				System.out.println(spikeRotation);
			}
			
			if (isKeyPressed(KeyCode.RIGHT) && spikeRotateTimer == 0) { //rotating spikes right
				spikeRotation += SRotationTick;
				spikeRotateTimer = spikeRotateCooldown;
				System.out.println(spikeRotation);
			}
			
			if (spikeRotateTimer > 0) {
				spikeRotateTimer--;
			}
			
			if (currentlyLoading || currentlySaving) {
				if (saveLoadTimer < saveLoadCooldown) {
					saveLoadTimer++;
				}else {
					currentlyLoading = false;
					currentlySaving = false;
					saveLoadTimer = 0;
				}
			}
			
			handleSaving();
			handleManualLoading();	
		}
		
		
		checkForFinishLine();
		checkForCheckpoint();
		checkPortalCollision();
		
		if (portalTimer > 0) {
	        portalTimer--;
	    } 
	}	
	
	
	public void checkPortalCollision() {
        for (Portal portal : portals ) {
            if (playerBall.touching(portal) && portalTimer == 0) {
            	System.out.println("Teleporting ball to other portal");
                portal.teleport(playerBall);
                portalTimer = portalCooldown;
                break;
            }
        }
    }
	
	public void linkPortals(Portal portal1, Portal portal2) {
	    portal1.setLinkedPortal(portal2);
	    portal2.setLinkedPortal(portal1);
	}
	
	
	private void checkForFinishLine() {
        List<FinishLineFlag> finishLineFlags = getObjects(FinishLineFlag.class);
        for (FinishLineFlag flag : finishLineFlags) {
            if (playerBall != null && flag.touching(playerBall)) {
            	System.out.println("Player ball is touching the finish line flag.");
                finishLevel();
            }
        }
    }

	public boolean finishLevel() {
		System.out.println("Finish level method called.");
		
		if (playerBall != null) {
	        playerBall.stopRollingSound(); // Stop the rolling sound
	    }
		
		if (flipracerGame != null) {
            flipracerGame.completeLevel(flipracerGame.getCurrentLevel() - 1);
            clearCurrentLevelResources();
            //flipracerGame.showLevelSelector();
            
            String soundPath = Paths.get("src/LevelFinish.mp3").toUri().toString();
            Media sound = new Media(soundPath);
            MediaPlayer finishSoundPlayer = new MediaPlayer(sound);
            finishSoundPlayer.play();

            finishSoundPlayer.setOnEndOfMedia(() -> {
                finishSoundPlayer.dispose();
                flipracerGame.showLevelSelector();
            });
        } else {
            System.err.println("FlipracerGame reference is null.");
        }
		return true;
	}
	
	public void startLevel(int level) {
        switch (level) {
            case 1:
                playerBall = new PlayerBall(ballSize, "flipracer_resources/tennisball.png");
                playerBall.setCenter(0, 0);
                add(playerBall);

                FinishLineFlag finishLineFlag = new FinishLineFlag(50);
                finishLineFlag.setCenter(700, 500);
                add(finishLineFlag);
                break;
            case 2:
                playerBall = new PlayerBall(ballSize, "flipracer_resources/tennisball.png");
                playerBall.setCenter(0, 0);
                add(playerBall);

                FinishLineFlag finishLineFlag1 = new FinishLineFlag(50);
                finishLineFlag1.setCenter(700, 500);
                add(finishLineFlag1);
                break;
            	
        }
    }
	
	private void checkForCheckpoint() {
		List<CheckpointFlag> checkpointFlags = getObjects(CheckpointFlag.class);
        for (CheckpointFlag flag : checkpointFlags) {
            if (playerBall != null && flag.touching(playerBall)) {
                playerBall.setCheckpoint(flag.getCenterX(), flag.getCenterY());
            }
        }
    }

	@Override
	public void onDimensionsInitialized() {	
		if (GOD_POWERS) {
			handleSpawning();
		}
	}
	
	public void clearCurrentLevelResources() {
		flipracerGame.getBackgroundMusic().stopBackgroundMusic();
        clearLevel();
        remove(playerBall);
    }
	
	public void handleLoading() {
		if (!currentlyLoading) {	  
			currentlyLoading = true;
			
	        try {
	            loadLevel("level" + flipracerGame.currentLevel + ".csv");
	            System.out.println("Loaded level " + flipracerGame.currentLevel);
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
		}
	}
	
	public void handleManualLoading() {
		if (isKeyPressed(KeyCode.L)) {
			handleLoading();
		}
	}
	
	public void handleSaving() {
		if (isKeyPressed(KeyCode.S) && !currentlySaving) {
			currentlySaving = true;
			
	        try {
	            saveLevel("level" + flipracerGame.currentLevel + ".csv");
	            System.out.println("Saved level " + flipracerGame.currentLevel);
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
		}
	}
	
	public void handleSpawning() {
		setOnMouseClicked(e -> {
        	if (isKeyPressed(KeyCode.E)) { // Fixed Ball
        		System.out.println("E key pressed. Creating FixedBall.");
        		FixedBall f = new FixedBall(fixedBallSize, "flipracer_resources/ball.png", fixedBallBounciness);
            	f.setCenter(e.getX(), e.getY());
            	add(f);
            }else if (isKeyPressed(KeyCode.Q)){
            	
            	List<Circle> cl = getObjectsAt(e.getX(), e.getY(), Circle.class);
            	for (Circle f : cl) {
            		if (!(f instanceof PlayerBall)) {
            			remove(f);
            			System.out.println("Removing object.");
            		}
            	}
            	
            	List<AssemblyObject> ob = getObjectsAt(e.getX(), e.getY(), AssemblyObject.class);            	
            	for (AssemblyObject f : ob) {
            		f.removeAssembly();
            		remove(f);
            		System.out.println("Removing object.");
            	}
            	
            	
            	
            }else if (isKeyPressed(KeyCode.F)){ // Platform
            	Platform p = new Platform(platformBounciness, platformWidth, platformHeight, e.getX(), e.getY());
            	add(p);
            	p.addAssembly();            	
            }else if (isKeyPressed(KeyCode.X)){ // RightInclinePlatform
            	System.out.println("X key pressed. Creating RightInclinePlatform.");
            	RightInclinePlatform p = new RightInclinePlatform(platformBounciness, platformWidth, platformHeight, e.getX(), e.getY());
            	add(p);
            	p.addAssembly();            	
            }else if (isKeyPressed(KeyCode.Z)){ // LeftInclinePlatform
            	System.out.println("Z key pressed. Creating LeftInclinePlatform.");
            	LeftInclinePlatform p = new LeftInclinePlatform(platformBounciness, platformWidth, platformHeight, e.getX(), e.getY());
            	add(p);
            	p.addAssembly();            	
            }else if (isKeyPressed(KeyCode.V)) { // BouncyBall
            	System.out.println("V key pressed. Creating BouncyBall.");
        		BouncyBall f = new BouncyBall(bouncyBallSize, bouncyBallBounciness);
            	f.setCenter(e.getX(), e.getY());
            	add(f);
            }else if (isKeyPressed(KeyCode.C)) { // CheckpointFlag
            	System.out.println("C key pressed. Creating CheckpointFlag.");
            	CheckpointFlag c = new CheckpointFlag(flagSize); 
                c.setCenter(e.getX(), e.getY());
                add(c);
            }else if (isKeyPressed(KeyCode.B)) { // Spike
            	System.out.println("B key pressed. Creating Spike");
            	Spike s = new Spike(spikeSize, spikeRotation);
            	s.setCenter(e.getX(),e.getY());
            	add(s);
            } else if (isKeyPressed(KeyCode.R)) { // FinishLineFlag
            	System.out.println("R key pressed. Creating FinishLineFlag");
            	FinishLineFlag f = new FinishLineFlag(finishLineFlagSize);
            	f.setCenter(e.getX(), e.getY());
            	add(f);
            } else if (isKeyPressed(KeyCode.P)) {
            	System.out.println("P key pressed. Creating BluePortal");
            	BluePortal p = new BluePortal(bluePortalSize);
            	p.setCenter(e.getX(), e.getY());
            	add(p);
            	portals.add(p);
            	if (portals.size() == 2) {
                    linkPortals(portals.get(0), portals.get(1));
                }
            }else if (isKeyPressed(KeyCode.O)) {
            	System.out.println("O key pressed. Creating OrangePortal");
            	OrangePortal o = new OrangePortal(orangePortalSize);
            	o.setCenter(e.getX(), e.getY());
            	add(o);
            	portals.add(o);
            	if (portals.size() == 2) {
                    linkPortals(portals.get(0), portals.get(1));
                }
            } else{
            	System.out.println("No special key pressed. Moving the ball to mouse location");
            	playerBall.setX(e.getX() - playerBall.getWidth()/2);
            	playerBall.setY(e.getY() - playerBall.getHeight()/2);
            	playerBall.setVelocity(new Point2D(0, 0));
                
                moveObjectToTop(playerBall);
            }
        });
	}
	 
	
	private void saveLevel(String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

        for (Platform p : getObjects(Platform.class)) {
            writer.write(String.format("Platform,%f,%f,%f,%f\n", p.getX(), p.getY(), p.getWidth(), p.getHeight()));
        }
        for (RightInclinePlatform p : getObjects(RightInclinePlatform.class)) {
            writer.write(String.format("RightInclinePlatform,%f,%f,%f,%f\n", p.getX(), p.getY(), p.getWidth(), p.getHeight()));
        }
        for (LeftInclinePlatform p : getObjects(LeftInclinePlatform.class)) {
            writer.write(String.format("LeftInclinePlatform,%f,%f,%f,%f\n", p.getX(), p.getY(), p.getWidth(), p.getHeight()));
        }
        for (BouncyBall b : getObjects(BouncyBall.class)) {
            writer.write(String.format("BouncyBall,%f,%f,%d,%f\n", b.getCenterX(), b.getCenterY(), b.getSize(), b.getBounciness()));
        }
        for (CheckpointFlag c : getObjects(CheckpointFlag.class)) {
            writer.write(String.format("CheckpointFlag,%f,%f\n", c.getX(), c.getY()));
        }
        for (FixedBall f : getObjects(FixedBall.class)) {
        	if (!f.getContainedInAssembly() && !(f instanceof BouncyBall)) {
        		writer.write(String.format("FixedBall,%f,%f,%d,%f\n", f.getX(), f.getY(), f.getSize(), f.getBounciness() ) );
        	}
        }
        for (Spike s : getObjects(Spike.class)) {
            writer.write(String.format("Spike,%f,%f,%d,%d\n", s.getX(), s.getY(), s.getSize(), s.getRotation()));
        }
        for (FinishLineFlag f : getObjects(FinishLineFlag.class)) {
        	writer.write(String.format("FinishLineFlag, %f,%f\n", f.getX(), f.getY()));
        }
        for (OrangePortal p : getObjects(OrangePortal.class)) {
        	writer.write(String.format("OrangePortal, %f,%f\n", p.getX(), p.getY()));
        	System.out.println("Saved orange at X: " + p.getX() + ", Y: " + p.getY());
        }
        for (BluePortal p : getObjects(BluePortal.class)) {
        	writer.write(String.format("BluePortal, %f,%f\n", p.getX(), p.getY()));
        	System.out.println("Saved blue at X: " + p.getX() + ", Y: " + p.getY());
        }
    
        
        writer.close();
    }
	
	private void loadLevel(String filename) throws IOException {
		clearLevel();
		
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            String type = parts[0];
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);

            switch (type) {
                case "Platform":
                    Platform fp = new Platform(platformBounciness, platformWidth, platformHeight, x, y);
                    add(fp);
                    fp.addAssembly();
                    break;
                case "RightInclinePlatform":
                    RightInclinePlatform rip = new RightInclinePlatform(platformBounciness, platformWidth, platformHeight, x + platformOffset, y);
                    rip.setX(x);
                    rip.setY(y);
                    add(rip);
                    rip.addAssembly();
                    break;
                case "LeftInclinePlatform":
                    LeftInclinePlatform lip = new LeftInclinePlatform(platformBounciness, platformWidth, platformHeight, x + platformOffset, y);
                    lip.setX(x);
                    lip.setY(y);
                    add(lip);
                    lip.addAssembly();
                    break;
                case "BouncyBall":
                    int radius = Integer.parseInt(parts[3]);
                    double bounciness = Double.parseDouble(parts[4]);
                    BouncyBall bb = new BouncyBall(radius, bounciness);
                    bb.setCenter(x, y);
                    add(bb);
                    break;
                case "CheckpointFlag":
                    CheckpointFlag cf = new CheckpointFlag(flagSize);
                    cf.setX(x);
                    cf.setY(y);
                    add(cf);
                    break;
                case "FixedBall":
                    int fbRadius = Integer.parseInt(parts[3]);
                    double fbBounciness = Double.parseDouble(parts[4]);
                    FixedBall fb = new FixedBall(fixedBallSize, "flipracer_resources/ball.png", fbBounciness);
                    fb.setX(x);
                    fb.setY(y);
                    add(fb);
                    break;
                case "Spike":
                    int size = Integer.parseInt(parts[3]);
                    int rotation = Integer.parseInt(parts[4]);
                    Spike s = new Spike(size, rotation);
                    s.setX(x);
                    s.setY(y);
                    add(s);
                    break;
                case "FinishLineFlag":
                	FinishLineFlag flf = new FinishLineFlag(finishLineFlagSize);
                	flf.setX(x);
                	flf.setY(y);
                	add(flf);
                	break;
                case "OrangePortal":
                	Portal o = new Portal(orangePortalSize,"flipracer_resources/orangePortal.png");
                	o.setX(x);
                	o.setY(y);
                	add(o);
                	portals.add(o);
                	if (portals.size() == 2) {
                        linkPortals(portals.get(0), portals.get(1));
                    }
                	break;
                case "BluePortal":
                	Portal p = new Portal(bluePortalSize,"flipracer_resources/bluePortal.png");
                	p.setX(x);
                	p.setY(y);
                	add(p);
                	portals.add(p);
                	if (portals.size() == 2) {
                        linkPortals(portals.get(0), portals.get(1));
                    }
                	break;
                
            }
        }
        reader.close();
    }
	
	public void clearLevel() {
    	for (Circle f : getObjects(Circle.class)) {
    		if (!(f instanceof PlayerBall)) {
    			remove(f);
    		}
    	}
    	        	
    	for (AssemblyObject f : getObjects(AssemblyObject.class)) {
    		f.removeAssembly();
    		remove(f);
    	}
	}

	public PlayerBall getPlayerBall() {
		return playerBall;
	}
	
}

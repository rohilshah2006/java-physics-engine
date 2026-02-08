package flipracer;

import javafx.scene.image.Image;

public class Platform extends AssemblyObject{
	
	private static String path = "flipracer_resources/platform.png";
	
	private int ballSize;
	private int ballRadius;
	private int xOffset;
	private int numBalls;
	private double bounciness;

	protected Platform(double bounciness, int width, int height, double x, double y) {
		super(bounciness, path);
		
		ballSize = height/2;
		ballRadius = ballSize/2;
		xOffset = 0;
		numBalls = width/ballSize;
		this.bounciness = bounciness;
		
		setImage(new Image(getClass().getClassLoader().getResource(path).toString()));
		
		setWidth(numBalls * ballSize);
		setHeight(ballSize);
		
		setX(x); setY(y);
		
		fill();
	}

	@Override
	public void fill() {
		double currX = getX();
		
		for (int i = 0; i < numBalls; i++) {
			FixedBall f = new FixedBall(ballSize, "", bounciness);
			f.setCenter(currX + ballRadius, getY() + ballSize);
			addToAssembly(f);
			
			//flipracer_resources/ball.png
			
			currX += ballSize + xOffset;
		}
	}

	@Override
	public void act(long now) {
		// TODO Auto-generated method stub
		
	}
	
	public void addAssembly() {
		for (FixedBall i : assembly) {
			getWorld().add(i);
		}
	}

}

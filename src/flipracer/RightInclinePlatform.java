package flipracer;

import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

public class RightInclinePlatform extends AssemblyObject{
	
	private static String path = "flipracer_resources/platform.png";
	
	int ballSize;
	int ballRadius;
	int xOffset;
	int yOffset;
	int numBalls;
	double bounciness;

	protected RightInclinePlatform(double bounciness, int width, int height, double x, double y) {
		super(bounciness, path);
		
		ballSize = height/2;
		ballRadius = ballSize/2;
		xOffset = 0;
		yOffset = 0;
		numBalls = width/ballSize;
		this.bounciness = bounciness;
		
		setImage(new Image(getClass().getClassLoader().getResource(path).toString()));
		
		setWidth(numBalls * ballSize);
		setHeight(ballSize);
		
		Rotate rotate = new Rotate(45, x + ballSize * 1.5, y);
		getTransforms().add(rotate);

		setX(x - ballSize); setY(y);
		
		fill();
	}

	@Override
	public void fill() {
		//flipracer_resources/ball.png
		
		double currX = getX() + ballSize;
		double currY = getY();
		
		for (int i = 0; i <= numBalls; i++) {
			FixedBall f = new FixedBall(ballSize, "", bounciness);
			
			f.setCenter(currX - ballSize/1.5, currY - ballSize/1.5);
			addToAssembly(f);
			
			currX += ballSize/1.5 + xOffset;
			currY += ballSize/1.5 + yOffset;
		}
	}

	@Override
	public void act(long now) {
		// TODO Auto-generated method stub
		
	}
}

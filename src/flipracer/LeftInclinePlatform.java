package flipracer;

import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;

public class LeftInclinePlatform extends AssemblyObject{
	
	private static String path = "flipracer_resources/platform.png";
	
	int ballSize;
	int ballRadius;
	int xOffset;
	int yOffset;
	int numBalls;
	double bounciness;

	protected LeftInclinePlatform(double bounciness, int width, int height, double x, double y) {
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
		
		Rotate rotate = new Rotate(135, x, y);
		getTransforms().add(rotate);
		
		Scale flip = new Scale(1, -1);
		flip.setPivotX(x);
        flip.setPivotY(y);
        getTransforms().add(flip);

		setX(x - ballSize); setY(y);
		
		fill();
	}

	@Override
	public void fill() {
		//flipracer_resources/ball.png
		
		double currX = getX() + ballSize * 2;
		double currY = getY();
		
		for (int i = 0; i <= numBalls; i++) {
			FixedBall f = new FixedBall(ballSize, "", bounciness);
			
			f.setCenter(currX, currY + ballRadius);
			addToAssembly(f);
			
			currX -= ballSize/1.5 + xOffset;
			currY += ballSize/1.5 + yOffset;
		}
	}

	@Override
	public void act(long now) {
		// TODO Auto-generated method stub
		
	}
}

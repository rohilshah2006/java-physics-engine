package flipracer;

import engine.Actor;
import javafx.scene.image.Image;
import java.awt.Point;

public class FixedBall extends Circle{
	
	private double bounciness;
	private boolean containedInAssembly = false;
	
	public FixedBall(int s, String path, double b) {
		super(s, path);
		bounciness = b;
    }
	
	@Override
	public void act(long now) {
		// TODO Auto-generated method stub
		
	}
	
	public double getBounciness() {
		return bounciness;
	}
	
	public boolean getContainedInAssembly() {
		return containedInAssembly;
	}
	
	public void setContainedInAssembly() {
		containedInAssembly = true;
	}
}

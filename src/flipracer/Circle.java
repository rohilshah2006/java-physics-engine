package flipracer;

import engine.*;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class Circle extends Actor{	
	private int size;
	private int radius;
	
	public Circle(int s, String path) {
		size = s;
		radius = size/2;
		setImage(new Image(getClass().getClassLoader().getResource(path).toString()));
		setHeight(size); setWidth(size);
	}
	
	
	@Override
	public void act(long now) {
		// TODO Auto-generated method stub
		
	}
	
	public int getRadius() {
		return radius;
	}
	
	public int getSize() {
		return size;
	}
	
	public void setCenter(double x, double y) {
		setX(x - radius);
		setY(y - radius);
		
	}
	
	public Point2D getCenter() {
		return new Point2D(getX() + radius, getY() + radius);	
	}
	
	public double getCenterX() {
		return getX() + radius;
	}
	
	public double getCenterY() {
		return getY() + radius;
	}
	
	public boolean touching(Circle c) {
		double dis = Math.hypot(getCenterX() - c.getCenterX(),  getCenterY() - c.getCenterY());
		double sum = getRadius() + c.getRadius();
		
		//if not touching, return false
		if (dis > sum) {
			return false;
		}
		
		return true;
	}
}

package flipracer;

import engine.*;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;

import collisions.Collision;

public class GravityBall extends Circle{
	protected int vx;
	protected int vy;
	
	private final int gravity = 1;
	private final double groundBounciness = 0.9;
	
	private final int maxVelocity = 15;
	
	public GravityBall(int s, String path) {
		super(s, path);
    }
	
	@Override
	public void act(long now) {
		vy += gravity;
		
		applyBounce();
		
		move(vx, vy);
		
		collideFixedBall();
		
		capVelocity();
		
		//System.out.println("Vx: " + vx);
		//System.out.println("Vy: " + vy);
		//System.out.println();		
	}
	
	public boolean isOnGround() {
		if (getCenterY() + getRadius() >= getWorld().getHeight()) {
			return true;
		}
		return false;
	}
	
	
	public void applyBounce(){
        if (isOnGround()){
            setCenter(getCenterX(), getWorld().getHeight() - getRadius());
            vy = (int)Math.floor(vy*groundBounciness);
            vy = -vy;
        }
        
        if (getCenterY() - getRadius() <= 0){
            setCenter(getCenterX(), getRadius());
            vy = -vy;
        }
        
        if (getCenterX() - getRadius() < 0){
            setCenter(getRadius(), getCenterY());
            vx = -vx;
        }
        
        if (getCenterX() + getRadius() > getWorld().getWidth()){
            setCenter(getWorld().getWidth() - getRadius(), getCenterY());
            vx = -vx;
        }
    }
	
	public void collideFixedBall() {	
		List<FixedBall> fbList = getIntersectingObjects(FixedBall.class);
		
		int sumVx = 0;
		int sumVy = 0;
		
		double bounciness = 0;
		
		boolean velocityChanged = false;
		
		for (FixedBall f : fbList) {
			if (f != null && touching(f)) {
				preventClipping(f);
				
				Point2D v1 = new Point2D(vx, vy);
				Point2D p1 = new Point2D(getCenterX(), getCenterY());
				Point2D p2 = new Point2D(f.getCenterX(), f.getCenterY());
					
				Point2D a = Collision.getBounceVector(v1, p1, p2);
				sumVx += a.getX();
				sumVy += a.getY();

				bounciness = f.getBounciness();
				
				velocityChanged = true;
			}
		}
		
		if (velocityChanged) {
			sumVy *= bounciness;
			sumVx *= bounciness;
			setVelocity(sumVx, sumVy);
		}
		
	}

	public void setVelocity(Point2D v) {
		vx = (int) v.getX();
		vy = (int) v.getY();
	}
	
	public void setVelocity(int x, int y) {
		vx = x;
		vy = y;
	}
	
	public void preventClipping(Circle c) {
		double dis = Math.hypot(getCenterX() - c.getCenterX(),  getCenterY() - c.getCenterY());	

		double xLength = (getCenterX() - c.getCenterX()); 
		double yLength = (getCenterY() - c.getCenterY()); 

		double overlap = (getRadius() + c.getRadius()) - dis;

		double normX = xLength / dis;
		double normY = yLength / dis;

		double newX = getCenterX() + overlap * normX;
		double newY = getCenterY() + overlap * normY;
		
		setCenter(newX, newY);
	}
	
	/*
	public void capVelocity() {
		if (Math.abs(vx) > maxVelocity) {
			if (vx < 0) {
				vx = -maxVelocity;
			}else {
				vx = maxVelocity;
			}	
		}
		
		if (Math.abs(vy) > maxVelocity) {
			if (vy < 0) {
				vy = -maxVelocity;
			}else {
				vy = maxVelocity;
			}				
		}	
	}
	*/
	
	public void capVelocity() {
		if (vx > maxVelocity) vx = maxVelocity;
		if (vx < -maxVelocity) vx = -maxVelocity;
		if (vy > maxVelocity) vy = maxVelocity;
		if (vy < -maxVelocity) vy = -maxVelocity;
	}
}

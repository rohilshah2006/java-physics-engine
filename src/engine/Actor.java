package engine;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.*;

public abstract class Actor extends ImageView {
	
	public Actor() {}
	
	public abstract void act(long now);
	
	public void addedToWorld() {}
	
	public double getHeight() {
		return getBoundsInParent().getHeight();
	}
	
	public double getWidth() {
		return getBoundsInParent().getWidth();
	}
	
	public World getWorld() {
		return (World)getParent();
	}
	

	
	public <A extends Actor> java.util.List<A> getIntersectingObjects(java.lang.Class<A> cls) {
	    java.util.List<A> list = new java.util.ArrayList<>();
	    for (Actor i : getWorld().getObjects(cls)) {
	        if (i != this && i.intersects(this.getBoundsInParent())) {
	            list.add(cls.cast(i));
	        }
	    }
	    return list;
	} 
	
	public <A extends Actor> A getOneIntersectingObject(java.lang.Class<A> cls) {
	    for (Actor i : getWorld().getObjects(cls)) {
	        if (i != this && i.intersects(this.getBoundsInParent())) {
	            return cls.cast(i);
	        }
	    }
	    return null;
	}
	
	/*
	public <A extends Actor> A getOneIntersectingObject(java.lang.Class<A> cls) {
		for (Actor i : getWorld().getObjects(cls)) {
			if (i.getBoundsInParent().intersects(getBoundsInParent())) {
				return cls.cast(i);
			}
		}
		return null;
	}
	*/
	
	public void move(double dx, double dy) {
		setX(getX() + dx); setY(getY() + dy);
	}
	
	public void setLocation(int x, int y) {
		setX(x); setY(y);
	}
	
	public void setWidth(int w) {
		setFitWidth(w);
	}
	
	public void setHeight(int h) {
		setFitHeight(h);
	}
}

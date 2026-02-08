package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.animation.AnimationTimer;

public abstract class World extends Pane {
	private AnimationTimer timer;
	private boolean timerRunning;
	private HashSet<KeyCode> keysPressed;
	private boolean heightSet;
	private boolean widthSet;
		
	public World() {
		heightSet = false; widthSet = false;
		keysPressed = new HashSet();
		timerRunning = false;
		
		widthProperty().addListener((ob, oldV, newV) -> {
			if (!newV.equals(0)) {
				widthSet = true;
				//System.out.println("Width changed from 0 to " + newV);
				//System.out.println(heightSet);
		    }
		});
		
		
		heightProperty().addListener((ob, oldV, newV) -> {
			if (!newV.equals(0)) {
				heightSet = true;
				//System.out.println("Height changed from 0 to " + newV);
				//System.out.println(heightSet);
				onDimensionsInitialized();
		    }
		});
		
		
		sceneProperty().addListener((ob, oldV, newV) -> {
		    if (newV != null) {
		    	requestFocus();
		    }
		});

		setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            /*
            if (!keysPressed.contains(keyCode)) {
            	keysPressed.add(keyCode);
                //System.out.println("Key pressed: " + keyCode);
            }
            */
            
            keysPressed.add(keyCode);
            //System.out.println(keysPressed);
        });
		
		setOnKeyReleased(event -> {
            KeyCode keyCode = event.getCode();
            keysPressed.remove(keyCode);
            //System.out.println(keysPressed);
        });
		
		timer = new AnimationTimer() {
			long prevTime = 0;
			
            @Override
            public void handle(long now) {
            	ArrayList<Node> getChildrenCopy = new ArrayList(getChildren());
            	long dt = now - prevTime;
            	
            	if (dt > 5e6) {
            		act(now);
            	
            		for (int i = 0; i < getChildrenCopy.size(); i++) {
            			if (getChildrenCopy.get(i) instanceof Actor) {
            				Actor a = ((Actor) getChildrenCopy.get(i));
                			
                			if (a.getWorld() != null) {
                				a.act(now);
                			}
            			}
            		}
            		prevTime = now;
            	}
            }
        };
	}
		
	public abstract void act(long now);
	
	//a couple methods called getObjects instead of getChildren which was breaking the code
	
	public abstract void onDimensionsInitialized();
	
	public void add(Actor actor) {
		getChildren().add(actor);
		actor.addedToWorld();
	}
	
	public void	remove(Actor actor) {
		getChildren().remove(actor);
		//System.out.println(actor.getWorld());
	}
	
	/*
	 * public <A extends Actor> java.util.List<A> getObjects(java.lang.Class<A> cls){
		List<A> list = new ArrayList<>();
		
		for (Node i : getChildren()) {
	        if (cls.isInstance(i)) {
	            list.add(cls.cast(i));
	        }
	    }
		
		return list;
	}
	 */
	
	public <A extends Actor> java.util.List<A> getObjects(java.lang.Class<A> cls){
		List<A> list = new ArrayList<>();
		
		for (Node i : getChildren()) {
	        if (cls.isInstance(i)) {
	            list.add(cls.cast(i));
	        }
	    }
		
		return list;
	}
	
	public <A extends Actor> java.util.List<A> getObjectsAt(double x, double y, java.lang.Class<A> cls){
		List<A> list = new ArrayList<>();
		
		for (Actor i : getObjects(cls)) {
			
			if (i.getBoundsInParent().contains(x, y)) {
				list.add(cls.cast(i));
			}
		}

		return list;
	}
	
	public boolean isKeyPressed(KeyCode code) {
		return keysPressed.contains(code);
	}
	
	public boolean isStopped() {
		return !timerRunning;
	}
	
	public void start() {
		timer.start();
		timerRunning = true;
	}
	
	public void stop() {
		timer.stop();
		timerRunning = false;
	}	
	
	public void moveObjectToTop(Actor a) {
		remove(a);
		add(a);
	}
}

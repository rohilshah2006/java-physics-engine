package flipracer;

import java.util.ArrayList;

import engine.Actor;
import javafx.scene.image.Image;

public abstract class AssemblyObject extends Actor{
	protected ArrayList<FixedBall> assembly = new ArrayList();
	
	protected AssemblyObject(double bounciness, String path) {
		
	}

	public abstract void fill();
	
	public void addToAssembly(FixedBall f) {
		f.setContainedInAssembly();
		assembly.add(f);
	}
	
	public void addAssembly() {
		for (FixedBall i : assembly) {
			getWorld().add(i);
		}
	}
	
	public void removeAssembly() {
		for (FixedBall i : assembly) {
			getWorld().remove(i);
		}
	}
	
}

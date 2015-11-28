package hu.tron.gridelement;

import java.awt.Point;

/**
 * Azt a játéktérbeli elemet reprezentálja amire egyszerre próbált lépni a két
 * kliens
 * 
 * @author Dávid Bajzáth
 */
public class TieCrash extends GridElement {
	
	public TieCrash(int x, int y) {
		super(x, y);
	}
	
	public TieCrash(Point position) {
		super(position);
	}
	
	@Override
	public String toString() {
		return "x";
	}

}

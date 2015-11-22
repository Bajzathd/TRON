package hu.tron.gridelement;

import java.awt.Point;

/**
 * Játéktérbeli akadályt reprezentáló osztály
 * 
 * @author Dávid Bajzáth
 */
public class Obstacle extends GridElement {
	
	/**
	 * Az akadály magassága
	 */
	private int height;
	
	public Obstacle(int x, int y, int height) {
		super(x, y);
		this.height = height;
	}
	
	public Obstacle(Point position, int height) {
		super(position);
		this.height = height;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public String toString() {
		return "#";
	}
}

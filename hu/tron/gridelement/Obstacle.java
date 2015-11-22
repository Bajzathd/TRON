package hu.tron.gridelement;

import java.awt.Point;

/**
 * J�t�kt�rbeli akad�lyt reprezent�l� oszt�ly
 * 
 * @author D�vid Bajz�th
 */
public class Obstacle extends GridElement {
	
	/**
	 * Az akad�ly magass�ga
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

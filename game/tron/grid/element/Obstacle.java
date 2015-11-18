package game.tron.grid.element;

import java.awt.Point;


public class Obstacle extends GridElement {
	
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
}

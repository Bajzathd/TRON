package game.tron.grid.element;

import java.awt.Point;

public class GridElement {
	
	protected Point position = new Point();
	
	public GridElement(int x, int y) {
		position.x = x;
		position.y = y;
	}
	
	public GridElement(Point position) {
		this.position = position;
	}
	
	public Point getPosition() {
		return position;
	}
	
	public int getX() {
		return position.x;
	}
	
	public int getY() {
		return position.y;
	}
}

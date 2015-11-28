package hu.tron.gridelement;

import java.awt.Point;

/**
 * Játéktérre helyezhetõ elemek õsosztálya
 * 
 * @author Dávid Bajzáth
 */
public abstract class GridElement {
	
	/**
	 * Elem pozíciója a játéktéren
	 */
	protected Point position;
	
	public GridElement(int x, int y) {
		position = new Point(x, y);
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
	
	@Override
	public abstract String toString();
}

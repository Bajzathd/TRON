package hu.tron.gridelement;

import java.awt.Point;

/**
 * J�t�kt�rre helyezhet� elemek �soszt�lya
 * 
 * @author D�vid Bajz�th
 */
public abstract class GridElement {
	
	/**
	 * Elem poz�ci�ja a j�t�kt�ren
	 */
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

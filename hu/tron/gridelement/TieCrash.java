package hu.tron.gridelement;

import java.awt.Point;

/**
 * Azt a j�t�kt�rbeli elemet reprezent�lja amire egyszerre pr�b�lt l�pni a k�t
 * kliens
 * 
 * @author D�vid Bajz�th
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

package game.tron.grid.element;

import java.awt.Point;

public class Trail extends GridElement {
	
	private int clientId;
	
	public Trail(int x, int y, int clientId) {
		super(x, y);
		this.clientId = clientId;
	}
	
	public Trail(Point position, int clientId) {
		super(position);
		this.clientId = clientId;
	}
	
	public int getClientId() {
		return clientId;
	}
}

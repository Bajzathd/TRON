package game.tron.grid.element;

import game.tron.client.Client;

import java.awt.Point;

public class Trail extends GridElement {
	
	private Client client;
	
	public Trail(int x, int y, Client client) {
		super(x, y);
		this.client = client;
	}
	
	public Trail(Point position, Client client) {
		super(position);
		this.client = client;
	}
	
	public Client getClient() {
		return client;
	}
}

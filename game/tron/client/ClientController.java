package game.tron.client;

import game.tron.grid.GridController;

import java.awt.Point;

public class ClientController {
	
	protected Client client;
	
	public ClientController(Client client) {
		this.client = client;
	}

	public Point getNextPosition() {
		return client.getDirection().getTranslatedPoint(client.getPosition());
	}
	
	public void step(GridController gridController) {
		client.step();
	}
	
	public Client getClient() {
		return client;
	}
	
}

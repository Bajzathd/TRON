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
	
	public static PlayerController get(Player player) {
		return new PlayerController(player);
	}
	
	public static AIController get(AI ai) {
		switch (ai.getLevel()) {
		case 1:
			return new AIControllerLevel1(ai);
		default:
			System.out.println("Invalid AI level");
			System.exit(1);
		}
		return null;
	}
	
}

package game.tron.grid;

import java.util.Iterator;

import game.tron.client.Client;
import game.tron.client.ClientController;
import game.tron.client.ai.AI;
import game.tron.client.player.Player;

public class GridController {

	protected Grid grid;

	public GridController(Grid grid) {
		this.grid = grid;
	}

	public GridController(int width, int height, double obstacleRatio) {
		this.grid = new GridCreator(width, height, obstacleRatio).getGrid();
	}

	public GridController clone() {
		return new GridController(grid.clone());
	}

	public void update() {
		for (Client client : grid.getAliveClients()) {
			if (client instanceof Player) {
				ClientController.get((Player) client).step(this);
			} else {
				ClientController.get((AI) client).step(this);
			}
		}
		evaluate();
	}

	public void evaluate() {
		Client client1 = grid.getClient1();
		Client client2 = grid.getClient2();
		
		if (client1.getPosition().equals(client2.getPosition())) {
			grid.getAliveClients().clear();
			client1.kill();
			client2.kill();
		} else {
			handleClient(client1);
			handleClient(client2);
		}
	}
	
	private void handleClient(Client client) {
		if (grid.isFloor(client.getPosition())) {
			grid.addTrail(client.getTrail());
		} else {
			grid.killClient(client);
		}
	}

	public void start() {
		grid.start();
	}

	public Grid getGrid() {
		return grid;
	}

	public int getNumAliveClients() {
		return grid.getAliveClients().size();
	}

	public boolean isOver() {
		return getNumAliveClients() < 2;
	}

}

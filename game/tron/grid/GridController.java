package game.tron.grid;

import java.util.Iterator;

import game.tron.client.Client;

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
			client.step();
		}
		evaluate();
	}

	public void evaluate() {
		Iterator<Client> it = grid.getAliveClients().iterator();
		Client client;

		while (it.hasNext()) {
			client = it.next();

			if (grid.isFloor(client.getPosition())) {
				grid.addTrail(client.getTrail());
			} else {
				it.remove();
				grid.killClient(client);
			}
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

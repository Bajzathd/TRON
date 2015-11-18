package game.tron.grid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.omg.CosNaming.NamingContextPackage.NotFound;

import game.tron.client.AI;
import game.tron.client.AIControllerLevel1;
import game.tron.client.Client;
import game.tron.client.ClientController;
import game.tron.client.Player;
import game.tron.client.PlayerController;

public class GridController {
	
	protected Grid grid;
	protected List<ClientController> aliveClientControllers = new ArrayList<ClientController>();

	public GridController(Grid grid) {
		this.grid = grid;
	}
	
	public GridController(int width, int height, double obstacleRatio) {
		this.grid = new GridCreator(width, height, obstacleRatio).getGrid();
	}
	
	public GridController clone() {
		GridController clone = new GridController(grid.clone());
		clone.aliveClientControllers = new ArrayList<ClientController>(aliveClientControllers);
		
		return clone;
	}
	
	public void addPlayer(Player player) throws Exception {
		grid.addPlayer(player);
		aliveClientControllers.add(new PlayerController(player));
	}
	
	public void addAI(AI ai) throws Exception {
		grid.addAI(ai);
		switch (ai.getLevel()) {
		case 1:
			aliveClientControllers.add(new AIControllerLevel1(ai));
			break;
		default:
			throw new Exception("Invalid AI level");
		}
	}
	
	public void update() {
		
		for (ClientController clientController : aliveClientControllers) {
			// lépteti az összes klienst
			stepClient(clientController);
		}
		
		evaluate();
	}
	
	public void stepClient(ClientController clientController) {
		clientController.step(this);
	}
	
	public void evaluate() {
		Iterator<ClientController> it = aliveClientControllers.iterator();
		ClientController clientController;
		Client client;
		
		while (it.hasNext()) {
			clientController = it.next();
			client = clientController.getClient();
			
			if (grid.isFloor(client.getPosition())) {
				grid.addTrail(client.getTrail());
			} else {
				grid.killClient(client);
				it.remove();
			}
		}
	}
	
	public void start() {
		grid.start();
	}
	
	public Grid getGrid() {
		return grid;
	}
	
	public boolean isOver() {
		return aliveClientControllers.size() < 2;
	}
	
	public ClientController getController(Client client) throws NotFound {
		for (ClientController clientController : aliveClientControllers) {
			if (clientController.getClient().getId() == client.getId()) {
				return clientController;
			}
		}
		throw new NotFound();
	}
	
}

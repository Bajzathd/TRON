package game.tron.client;

import game.tron.client.ai.AI;
import game.tron.client.ai.RandomAIController;
import game.tron.client.ai.MinimaxAIController;
import game.tron.client.player.Player;
import game.tron.client.player.PlayerController;
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
	
	public static ClientController get(Player player) {
		return new PlayerController(player);
	}
	
	public static ClientController get(AI ai) {
		int level = ai.getLevel();
		
		if (level <= 1) {
			return new RandomAIController(ai);
		} else if (level > 1) {
			return new MinimaxAIController(ai);
		}
		
		return null;
	}
	
}

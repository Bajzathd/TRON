package game.tron.client;

import game.tron.utility.Direction;

public class ClientSimulation extends Client {
	
	public ClientSimulation(int id) {
		super(id);
	}
	
	@Override
	public ClientSimulation clone() {
		return new ClientSimulation(id);
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

}

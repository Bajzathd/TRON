package game.tron.client.player;

import game.tron.client.Client;

public class Player extends Client {
	
	public Player(int id) {
		super(id);
	}
	
	public Player clone() {
		Player clone = new Player(id);
		
		clone.position = position;
		clone.alive = alive;
		clone.direction = direction;
		
		return clone;
	}
	
	public String toString() {
		return "Player#"+id;
	}

}

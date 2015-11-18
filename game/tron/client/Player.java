package game.tron.client;

public class Player extends Client {
	
	public Player(int id) {
		super(id);
	}
	
	public Player clone() {
		Player clone = new Player(id);
		
		clone.alive = alive;
		clone.direction = direction;
		
		return clone;
	}
	
	public String toString() {
		return "Player";
	}

}

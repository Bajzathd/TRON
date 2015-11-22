package hu.tron.client.player;

import hu.tron.client.Client;

/**
 * Játékos által irányított klienst reprezentáló osztály
 * 
 * @author Dávid Bajzáth
 */
public class Player extends Client {
	
	public Player(int id) {
		super(id);
	}

	@Override
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

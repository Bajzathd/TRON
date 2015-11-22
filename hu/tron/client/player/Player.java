package hu.tron.client.player;

import hu.tron.client.Client;

/**
 * J�t�kos �ltal ir�ny�tott klienst reprezent�l� oszt�ly
 * 
 * @author D�vid Bajz�th
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

package hu.tron.client.ai;

import hu.tron.client.Client;

/**
 * Mesterséges intelligenciával rendelkezõ klienseket reprezentáló osztály
 * 
 * @author Dávid Bajzáth
 */
public class AI extends Client {

	public AI(int id) {
		super(id);
	}

	@Override
	public AI clone() {
		AI clone = new AI(id);

		clone.position = position;
		clone.alive = alive;
		clone.lastDirection = lastDirection;
		clone.nextDirection = nextDirection;

		return clone;
	}
	
	@Override
	public String toString() {
		return "AI#"+id;
	}

}

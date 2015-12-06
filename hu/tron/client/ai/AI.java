package hu.tron.client.ai;

import hu.tron.client.Client;

/**
 * Mesters�ges intelligenci�val rendelkez� klienseket reprezent�l� oszt�ly
 * 
 * @author D�vid Bajz�th
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

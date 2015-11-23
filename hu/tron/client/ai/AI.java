package hu.tron.client.ai;

import hu.tron.client.Client;

/**
 * Mesters�ges intelligenci�val rendelkez� klienseket reprezent�l� oszt�ly
 * 
 * @author D�vid Bajz�th
 */
public class AI extends Client {

	/**
	 * AI szintje (minn�l magasabb ann�l jobban j�tszik, de ann�l tov�bb tart
	 * l�pnie)
	 */
	private int level;

	public AI(int id, int level) {
		super(id);
		this.level = level;
	}

	@Override
	public AI clone() {
		AI clone = new AI(id, level);

		clone.position = position;
		clone.alive = alive;
		clone.lastDirection = lastDirection;
		clone.nextDirection = nextDirection;

		return clone;
	}

	public int getLevel() {
		return level;
	}

	public String toString() {
		return "AI (level" + level + ")";
	}

}

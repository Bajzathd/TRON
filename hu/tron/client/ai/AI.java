package hu.tron.client.ai;

import hu.tron.client.Client;

/**
 * Mesterséges intelligenciával rendelkezõ klienseket reprezentáló osztály
 * 
 * @author Dávid Bajzáth
 */
public class AI extends Client {

	/**
	 * AI szintje (minnél magasabb annál jobban játszik, de annál tovább tart
	 * lépnie)
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

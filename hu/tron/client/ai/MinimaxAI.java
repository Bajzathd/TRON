package hu.tron.client.ai;

import hu.tron.heuristic.Heuristic;

public class MinimaxAI extends AI {
	
	/**
	 * AI szintje (minnél magasabb annál jobban játszik, de annál tovább tart
	 * lépnie)
	 */
	private int level;
	/**
	 * Heurisztika típusa (ez alapján számítódik a minimax fában a levelek 
	 * értéke)
	 */
	private Heuristic.type heuristic;

	public MinimaxAI(int id, Heuristic.type heuristic, int level) {
		super(id);
		this.heuristic = heuristic;
		this.level = level;
	}
	
	@Override
	public MinimaxAI clone() {
		MinimaxAI clone = new MinimaxAI(id, heuristic, level);
		
		clone.position = position;
		clone.alive = alive;
		clone.lastDirection = lastDirection;
		clone.nextDirection = nextDirection;
		
		return clone;
	}
	
	/**
	 * @return Minimax algoritmus leveleihez számított értékekhez használt
	 *         heurisztika típusa
	 */
	public Heuristic.type getHeuristicType() {
		return heuristic;
	}

	/**
	 * @return Minimax fa maximális mélysége
	 */
	public int getLevel() {
		return level;
	}
	
	@Override
	public String toString() {
		return "AI#" + id + " " + heuristic + "^" + level;
	}

}

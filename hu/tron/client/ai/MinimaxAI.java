package hu.tron.client.ai;

import hu.tron.heuristic.Heuristic;

public class MinimaxAI extends AI {
	
	/**
	 * AI szintje (minn�l magasabb ann�l jobban j�tszik, de ann�l tov�bb tart
	 * l�pnie)
	 */
	private int level;
	/**
	 * Heurisztika t�pusa (ez alapj�n sz�m�t�dik a minimax f�ban a levelek 
	 * �rt�ke)
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
	 * @return Minimax algoritmus leveleihez sz�m�tott �rt�kekhez haszn�lt
	 *         heurisztika t�pusa
	 */
	public Heuristic.type getHeuristicType() {
		return heuristic;
	}

	/**
	 * @return Minimax fa maxim�lis m�lys�ge
	 */
	public int getLevel() {
		return level;
	}
	
	@Override
	public String toString() {
		return "AI#" + id + " " + heuristic + "^" + level;
	}

}

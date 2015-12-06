package hu.tron.heuristic;

import hu.tron.client.ai.MinimaxAI;
import hu.tron.grid.Grid;

/**
 * Heurisztikák õsosztálya
 * 
 * @author Dávid Bajzáth
 */
public abstract class Heuristic {

	/**
	 * Lehetséges heurisztika típusok
	 */
	public static enum type {
		SEPARATOR, WALLHUGGER
	}

	/**
	 * Gépi játékos id-ja, amelyikhez számoljuk az értékeket
	 */
	protected int aiId;

	public Heuristic(int aiId) {
		this.aiId = aiId;
	}

	/**
	 * Játéktér állásának értékét kiszámító metódus
	 * 
	 * @param grid
	 *            játéktér
	 * @return játéktér heurisztikus értéke
	 */
	public abstract double getGrade(Grid grid);

	/**
	 * @param ai
	 *            gépi játékos
	 * @return gépi játékoshoz tartozó heurisztika
	 */
	public static Heuristic get(MinimaxAI ai) {
		switch (ai.getHeuristicType()) {
		case SEPARATOR:
			return new SeparatorHeuristic(ai.getId());
		case WALLHUGGER:
			return new WallhuggerHeuristic(ai.getId());
		}
		return null;
	}

}

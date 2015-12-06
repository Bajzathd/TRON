package hu.tron.heuristic;

import hu.tron.client.ai.MinimaxAI;
import hu.tron.grid.Grid;

/**
 * Heurisztik�k �soszt�lya
 * 
 * @author D�vid Bajz�th
 */
public abstract class Heuristic {

	/**
	 * Lehets�ges heurisztika t�pusok
	 */
	public static enum type {
		SEPARATOR, WALLHUGGER
	}

	/**
	 * G�pi j�t�kos id-ja, amelyikhez sz�moljuk az �rt�keket
	 */
	protected int aiId;

	public Heuristic(int aiId) {
		this.aiId = aiId;
	}

	/**
	 * J�t�kt�r �ll�s�nak �rt�k�t kisz�m�t� met�dus
	 * 
	 * @param grid
	 *            j�t�kt�r
	 * @return j�t�kt�r heurisztikus �rt�ke
	 */
	public abstract double getGrade(Grid grid);

	/**
	 * @param ai
	 *            g�pi j�t�kos
	 * @return g�pi j�t�koshoz tartoz� heurisztika
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

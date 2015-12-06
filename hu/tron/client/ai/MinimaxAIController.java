package hu.tron.client.ai;

import hu.tron.grid.Grid;
import hu.tron.heuristic.MinimaxTree;

/**
 * Minimax algoritmusra �p�l� AI-t kezel� oszt�ly
 * 
 * @author D�vid Bajz�th
 */
public class MinimaxAIController extends AIController {

	/**
	 * J�t�kfa
	 */
	private MinimaxTree stepTree;

	public MinimaxAIController(MinimaxAI ai) {
		super(ai);
	}

	@Override
	public void step(Grid grid) {
		stepTree = new MinimaxTree(grid, (MinimaxAI) client);

		client.trySetDirection(stepTree.getBestDirection());

		super.step(grid);
	}

}

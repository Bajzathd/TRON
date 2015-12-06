package hu.tron.client.ai;

import hu.tron.grid.Grid;
import hu.tron.heuristic.MinimaxTree;

/**
 * Minimax algoritmusra épülõ AI-t kezelõ osztály
 * 
 * @author Dávid Bajzáth
 */
public class MinimaxAIController extends AIController {

	/**
	 * Játékfa
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

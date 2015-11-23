package hu.tron.client.ai;

import hu.tron.grid.Grid;
import hu.tron.utility.MinimaxTree;

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
	/**
	 * Játékfa maximális mélysége. Minél nagyobb annál jobb eredményt ad, de
	 * annál több idõ kell a számításához.
	 */
	private int depth;

	public MinimaxAIController(AI ai) {
		super(ai);
		depth = ai.getLevel();
	}

	@Override
	public void step(Grid grid) {
		long startTime = System.nanoTime();
		
		stepTree = new MinimaxTree(grid, client.getId(), depth);

		client.trySetDirection(stepTree.getBestDirection());

		super.step(grid);
		
		System.out.println("It took " + 
				((System.nanoTime() - startTime) / 1000000) + " ms for " +
				client + " to step.");
	}

}

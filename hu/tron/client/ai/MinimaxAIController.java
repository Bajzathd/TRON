package hu.tron.client.ai;

import hu.tron.grid.Grid;
import hu.tron.utility.MinimaxTree;

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
	/**
	 * J�t�kfa maxim�lis m�lys�ge. Min�l nagyobb ann�l jobb eredm�nyt ad, de
	 * ann�l t�bb id� kell a sz�m�t�s�hoz.
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

package game.tron.client.ai;

import java.util.Iterator;

import game.tron.grid.GridController;
import game.tron.utility.Direction;
import game.tron.utility.MinimaxTree;
import game.tron.utility.MinimaxTree.State;

public class MinimaxAIController extends AIController {
	
	private int depth;

	public MinimaxAIController(AI ai) {
		super(ai);
		depth = ai.getLevel();
	}
	
	@Override
	public void step(GridController gridController) {
		MinimaxTree stepTree = new MinimaxTree(gridController, client.getId());
		Direction bestDirection = client.getDirection();
		
		for (int round = 1; round <= depth; round++) {
			handleState(stepTree.getStartState(), round);
			bestDirection = stepTree.getDirection();
		}
		
		client.trySetDirection(bestDirection);
		
		super.step(gridController);
	}
	
	/**
	 * Adott �ll�sb�l adott m�lys�gig elk�sz�ti a r�szf�t
	 * @param state �ll�s
	 * @param limit m�lys�g
	 */
	private void handleState(State state, int limit) {
		if (limit <= 0) {
			/* 
			 * ha el�ri a maxim�lis m�lys�get akkor oszt�lyozza
			 * az �ll�st
			 */
			state.grade();
			return;
		}
		// gyerekeit legener�lja
		state.setChildren();
		
		if (state.getChildren().isEmpty()) {
			// ha nincsenek gyerekei akkor oszt�lyoz (v�g�llapothoz �rt)
			state.grade();
		}
		Iterator<State> it = state.getChildren().iterator();
		while (it.hasNext()) {
			// elk�sz�ti a lehets�ges �ll�sok r�szf�it 1-el kisebb m�lys�ggel
			handleState(it.next(), limit - 1);
		}
	}
	
}

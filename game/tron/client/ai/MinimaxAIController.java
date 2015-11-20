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
	 * Adott állásból adott mélységig elkészíti a részfát
	 * @param state állás
	 * @param limit mélység
	 */
	private void handleState(State state, int limit) {
		if (limit <= 0) {
			/* 
			 * ha eléri a maximális mélységet akkor osztályozza
			 * az állást
			 */
			state.grade();
			return;
		}
		// gyerekeit legenerálja
		state.setChildren();
		
		if (state.getChildren().isEmpty()) {
			// ha nincsenek gyerekei akkor osztályoz (végállapothoz ért)
			state.grade();
		}
		Iterator<State> it = state.getChildren().iterator();
		while (it.hasNext()) {
			// elkészíti a lehetséges állások részfáit 1-el kisebb mélységgel
			handleState(it.next(), limit - 1);
		}
	}
	
}

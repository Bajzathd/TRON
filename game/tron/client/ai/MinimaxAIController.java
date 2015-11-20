package game.tron.client.ai;

import java.util.Iterator;

import game.tron.grid.GridController;
import game.tron.utility.Direction;
import game.tron.utility.MinimaxTree;
import game.tron.utility.MinimaxTree.Node;

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
			handleNode(stepTree.getRoot(), round);
			bestDirection = stepTree.getDirection();
		}
		
		System.out.println(bestDirection);
		
		client.trySetDirection(bestDirection);
		
		super.step(gridController);
	}
	
	/**
	 * Adott állásból adott mélységig elkészíti a részfát
	 * @param node állás
	 * @param limit mélység
	 */
	private void handleNode(Node node, int limit) {
		if (limit <= 0) {
			/* 
			 * ha eléri a maximális mélységet akkor osztályozza
			 * a pozíciót 
			 */
			node.grade();
			return;
		}
		// gyerekeit legenerálja
		node.setChildren();
		
		if (node.getChildren().isEmpty()) {
			// ha nincsenek gyerekei akkor osztályoz (végállapothoz ért)
			node.grade();
		}
		Iterator<Node> it = node.getChildren().iterator();
		while (it.hasNext()) {
			// elkészíti a lehetséges pozíciók részfáit 1-el kisebb mélységgel
			handleNode(it.next(), limit - 1);
		}
	}
	
}

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
		
//		for (int round = 1; round <= depth; round++) {
			handleNode(stepTree.getRoot(), depth);
//		}
		client.trySetDirection(stepTree.getDirection());
		
		System.out.println(stepTree);
		
		super.step(gridController);
	}
	
	/**
	 * Adott pozícióból adott mélységig elkészíti a részfát
	 * @param node pozíció
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
		if (node.getChildren().isEmpty()) {
			// ha nincsenek gyerekei akkor beállítja õket
			node.setChildren();
		}
		if (node.getChildren().isEmpty()) {
			// ha továbbra sincsenek akkor osztályoz (végállapothoz ért)
			node.grade();
		}
		Iterator<Node> it = node.getChildren().iterator();
		while (it.hasNext()) {
			// elkészíti a lehetséges pozíciók részfáit 1-el kisebb mélységgel
			handleNode(it.next(), limit - 1);
		}
	}
	
}

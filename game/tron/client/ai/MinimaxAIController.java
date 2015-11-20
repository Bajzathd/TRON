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
	 * Adott �ll�sb�l adott m�lys�gig elk�sz�ti a r�szf�t
	 * @param node �ll�s
	 * @param limit m�lys�g
	 */
	private void handleNode(Node node, int limit) {
		if (limit <= 0) {
			/* 
			 * ha el�ri a maxim�lis m�lys�get akkor oszt�lyozza
			 * a poz�ci�t 
			 */
			node.grade();
			return;
		}
		// gyerekeit legener�lja
		node.setChildren();
		
		if (node.getChildren().isEmpty()) {
			// ha nincsenek gyerekei akkor oszt�lyoz (v�g�llapothoz �rt)
			node.grade();
		}
		Iterator<Node> it = node.getChildren().iterator();
		while (it.hasNext()) {
			// elk�sz�ti a lehets�ges poz�ci�k r�szf�it 1-el kisebb m�lys�ggel
			handleNode(it.next(), limit - 1);
		}
	}
	
}

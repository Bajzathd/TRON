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
	 * Adott poz�ci�b�l adott m�lys�gig elk�sz�ti a r�szf�t
	 * @param node poz�ci�
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
		if (node.getChildren().isEmpty()) {
			// ha nincsenek gyerekei akkor be�ll�tja �ket
			node.setChildren();
		}
		if (node.getChildren().isEmpty()) {
			// ha tov�bbra sincsenek akkor oszt�lyoz (v�g�llapothoz �rt)
			node.grade();
		}
		Iterator<Node> it = node.getChildren().iterator();
		while (it.hasNext()) {
			// elk�sz�ti a lehets�ges poz�ci�k r�szf�it 1-el kisebb m�lys�ggel
			handleNode(it.next(), limit - 1);
		}
	}
	
}

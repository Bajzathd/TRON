package game.tron.client.ai;

import java.util.Iterator;

import game.tron.grid.GridController;
import game.tron.utility.Direction;
import game.tron.utility.StepTree;
import game.tron.utility.StepTree.Node;

public class MinimaxAIController extends AIController {
	
	private int depth;

	public MinimaxAIController(AI ai) {
		super(ai);
		depth = ai.getLevel();
	}
	
	@Override
	public void step(GridController gridController) {
		StepTree stepTree = new StepTree(gridController, client.getId());
		
//		for (int round = 1; round <= depth; round++) {
			handleNode(stepTree.getRoot(), depth);
//		}
		stepTree.grade();
		
		Direction direction = stepTree.getDirection();
		if (direction != null) {
			client.trySetDirection(direction);
		}
		
		super.step(gridController);
	}
	
	private void handleNode(Node node, int limit) {
		if (limit <= 0) {
			node.grade();
			return;
		}
		if (node.getChildren().isEmpty()) {
			node.setChildren();
		}
		Iterator<Node> it = node.getChildren().iterator();
		while (it.hasNext()) {
			handleNode(it.next(), limit - 1);
		}
	}
	
}

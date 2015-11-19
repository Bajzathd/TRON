package game.tron.client.ai;

import java.util.List;
import java.util.Random;

import org.omg.CosNaming.NamingContextPackage.NotFound;

import game.tron.grid.GridController;
import game.tron.utility.Direction;

/**
 * Random v�laszt a lehets�ges ir�nyok k�z�l ahol padl� van
 */
public class RandomAIController extends AIController {

	public RandomAIController(AI ai) {
		super(ai);
	}
	
	@Override
	public void step(GridController gridController) {
		try {
			List<Direction> validDirections = gridController.getGrid().getValidDirections(client.getPosition());
			if (! validDirections.isEmpty()) {
				Random rnd = new Random();
				client.trySetDirection(validDirections.get(rnd.nextInt(validDirections.size())));
			}
		} catch (NotFound ex) {
			// nincs j� d�nt�s, marad az el�z� ir�ny
		}
		client.step();
	}
	
}

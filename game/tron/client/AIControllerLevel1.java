package game.tron.client;

import org.omg.CosNaming.NamingContextPackage.NotFound;

import game.tron.grid.GridController;

/**
 * Random v�laszt a lehets�ges ir�nyok k�z�l ahol padl� van
 */
public class AIControllerLevel1 extends AIController {

	public AIControllerLevel1(AI ai) {
		super(ai);
	}
	
	@Override
	public void step(GridController gridController) {
		try {
			client.trySetDirection(gridController.getGrid().getValidDirections(client.getPosition()).get(0));
		} catch (NotFound ex) {
			// nincs j� d�nt�s, marad az el�z� ir�ny
		}
		client.step();
	}
	
}

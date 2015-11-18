package game.tron.client;

import org.omg.CosNaming.NamingContextPackage.NotFound;

import game.tron.grid.GridController;

/**
 * Random választ a lehetséges irányok közül ahol padló van
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
			// nincs jó döntés, marad az elõzõ irány
		}
		client.step();
	}
	
}

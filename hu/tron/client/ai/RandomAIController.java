package hu.tron.client.ai;

import java.util.List;
import java.util.Random;

import org.omg.CosNaming.NamingContextPackage.NotFound;

import hu.tron.grid.Grid;
import hu.tron.utility.Direction;

/**
 * Random algoritmusra épülõ AI-t kezelõ osztály
 * 
 * @author Dávid Bajzáth
 */
public class RandomAIController extends AIController {
	/*
	 * Az AI-val szomszédos padlók közül egy véletlenszerûen választottra lép
	 */
	
	public RandomAIController(AI ai) {
		super(ai);
	}

	@Override
	public void step(Grid grid) {
		try {
			List<Direction> validDirections = grid.getValidDirections(
					client.getPosition());
			
			if (!validDirections.isEmpty()) {
				Random rnd = new Random();
				client.trySetDirection(validDirections.get(
						rnd.nextInt(validDirections.size())));
			}
		} catch (NotFound ex) {} // nincs jó döntés, marad az elõzõ irány
		
		super.step(grid);
	}

}

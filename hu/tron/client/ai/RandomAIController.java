package hu.tron.client.ai;

import java.util.List;
import java.util.Random;

import org.omg.CosNaming.NamingContextPackage.NotFound;

import hu.tron.grid.Grid;
import hu.tron.utility.Direction;

/**
 * Random algoritmusra �p�l� AI-t kezel� oszt�ly
 * 
 * @author D�vid Bajz�th
 */
public class RandomAIController extends AIController {
	/*
	 * Az AI-val szomsz�dos padl�k k�z�l egy v�letlenszer�en v�lasztottra l�p
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
		} catch (NotFound ex) {} // nincs j� d�nt�s, marad az el�z� ir�ny
		
		super.step(grid);
	}

}

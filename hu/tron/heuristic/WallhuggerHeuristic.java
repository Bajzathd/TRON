package hu.tron.heuristic;

import java.awt.Point;
import java.util.List;

import org.omg.CosNaming.NamingContextPackage.NotFound;

import hu.tron.client.Client;
import hu.tron.grid.Grid;
import hu.tron.utility.Direction;

/**
 * Wallhugger-nek elnevezett heurisztik�t megval�s�t� oszt�ly
 * 
 * @author D�vid Bajz�th
 */
public class WallhuggerHeuristic extends Heuristic {

	public WallhuggerHeuristic(int aiId) {
		super(aiId);
	}

	@Override
	public double getGrade(Grid grid) {
		Client client = grid.getClient(aiId);
		List<Point> accessibleFloors = grid.getAccessibleFloors(client);
		int numNeighborWalls = 0;
		
		if (!client.isAlive()) {
			return -1;
		}
		
		try {
			for (Direction d : grid.getValidDirections(client.getPosition())) {
				if (!grid.isFloor(d.getTranslatedPoint(client.getPosition()))) {
					numNeighborWalls++;
				}
			}
		} catch (NotFound e) {}
		
		return accessibleFloors.size() + numNeighborWalls;
	}

}

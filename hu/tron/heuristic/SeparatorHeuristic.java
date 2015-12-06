package hu.tron.heuristic;

import java.awt.Point;
import java.util.List;

import hu.tron.client.Client;
import hu.tron.grid.Grid;
import hu.tron.utility.Direction;

/**
 * Separator-nak elnevezett heurisztik�t megval�s�t� oszt�ly
 * 
 * @author D�vid Bajz�th
 */
public class SeparatorHeuristic extends Heuristic {

	public SeparatorHeuristic(int aiId) {
		super(aiId);
	}

	@Override
	public double getGrade(Grid grid) {
		int numAliveClients = grid.getAliveClients().size();
		int numFloors = grid.getNumFloors();
		
		if (numAliveClients == 0) {
			return 0; // TIE
		} 
		if (numAliveClients == 1) {
			Client winner = grid.getAliveClients().get(0);

			return (winner.getId() == aiId) 
					? (double) numFloors // WIN
					: (double) -numFloors; // LOSE
		}
		
		Client client = grid.getClient(aiId);
		Client enemy = grid.getEnemy(aiId);
		List<Point> accessibleFloors = grid.getAccessibleFloors(client);
		
		/*
		 * ha az ellenf�l poz�ci�j�nak valamelyik szomsz�dj�t el�ri 
		 * akkor nincs elszepar�lva
		 */
		boolean isSeparated = true;
		for (Direction d : Direction.values()) {
			if (accessibleFloors.contains(d.getTranslatedPoint(
					enemy.getPosition()))) {
				isSeparated = false;
				break;
			}
		}

		if (!isSeparated) {
			double distance = client.getPosition().distance(
					enemy.getPosition());

			/*
			 * minn�l k�zelebb van hozz� (csak heurisztikus t�vols�got
			 * m�r, l�gvonalban milyen t�vol vannak egym�st�l a
			 * kliensek)
			 */
			return 1 / distance;
		}
		
		int difference = accessibleFloors.size() - 
				(numFloors - accessibleFloors.size());

		// minn�l t�bb padl�t �r el
		return difference;
	}

}

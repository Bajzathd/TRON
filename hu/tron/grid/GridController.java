package hu.tron.grid;

import hu.tron.client.Client;
import hu.tron.client.ai.AI;
import hu.tron.client.ai.AIController;
import hu.tron.client.player.Player;
import hu.tron.client.player.PlayerController;
import hu.tron.utility.Log;

/**
 * Játéktér irányítását végzõ osztály
 * 
 * @author Dávid Bajzáth
 */
public class GridController {

	/**
	 * Irányítandó játéktér
	 */
	protected Grid grid;

	public GridController(Grid grid) {
		this.grid = grid;
	}

	public GridController(int width, int height, double obstacleRatio) {
		this.grid = new GridCreator(width, height, obstacleRatio).getGrid();
	}

	/**
	 * Firssíti a játékteret: minden klienst léptet, majd kiértékeli a lépéseket
	 */
	public void update() {
		Grid gridClone = grid.clone();
		for (Client client : grid.getAliveClients()) {
			if (client instanceof Player) {
				new PlayerController((Player) client).step(gridClone);
			} else {
				AIController.get((AI) client).step(gridClone);
			}
		}
		evaluate();
	}
	
	/**
	 * Firssíti a játékteret: minden klienst léptet, majd kiértékeli a lépéseket
	 * és elmenti a statisztikákat a naplóba
	 * 
	 * @param log Naplózásra használt objektum
	 */
	public void update(Log log) {
		Grid gridClone = grid.clone();
		for (Client client : grid.getAliveClients()) {
			long startTime = System.nanoTime();
			AIController.get((AI) client).step(gridClone);
			log.addStepTime(client, System.nanoTime() - startTime);
		}
		evaluate();
	}

	/**
	 * Lépések kiértékelése. Ha a két kliens ugyan arra a mezõre próbál lépni
	 * akkor egy különleges eset ({@link TieCrash}) keletkezik. Különben a
	 * <code>handleClient</code> metódus kezeli a klienseket.
	 */
	public void evaluate() {
		Client client1 = grid.getClient1();
		Client client2 = grid.getClient2();
		
		if (client1.getPosition().equals(client2.getPosition())) {
			grid.setTieCrash(client1.getPosition());
			grid.killClient(client1);
			grid.killClient(client2);
		} else {
			handleClient(client1);
			handleClient(client2);
		}
	}

	/**
	 * Kliens lépésének kezelése. Ha padlóra lép akkor csíkot húz maga után,
	 * különben meghal.
	 * 
	 * @param client kliens
	 */
	private void handleClient(Client client) {
		if (grid.isFloor(client.getPosition())) {
			grid.addTrail(client.getTrail());
		} else {
			grid.killClient(client);
		}
	}

	/**
	 * Elindítja a játékot
	 * 
	 * @throws Exception nem lett bellítva minden kliens
	 */
	public void start() throws Exception {
		grid.start();
	}

	public Grid getGrid() {
		return grid;
	}

	/**
	 * @return élõ kliensek száma
	 */
	public int getNumAliveClients() {
		return grid.getAliveClients().size();
	}

	/**
	 * @return vége van-e a játéknak
	 */
	public boolean isOver() {
		return getNumAliveClients() < 2;
	}

}

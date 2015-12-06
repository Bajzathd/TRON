package hu.tron.grid;

import hu.tron.client.Client;
import hu.tron.client.ai.AI;
import hu.tron.client.ai.AIController;
import hu.tron.client.player.Player;
import hu.tron.client.player.PlayerController;
import hu.tron.utility.Log;

/**
 * J�t�kt�r ir�ny�t�s�t v�gz� oszt�ly
 * 
 * @author D�vid Bajz�th
 */
public class GridController {

	/**
	 * Ir�ny�tand� j�t�kt�r
	 */
	protected Grid grid;

	public GridController(Grid grid) {
		this.grid = grid;
	}

	public GridController(int width, int height, double obstacleRatio) {
		this.grid = new GridCreator(width, height, obstacleRatio).getGrid();
	}

	/**
	 * Firss�ti a j�t�kteret: minden klienst l�ptet, majd ki�rt�keli a l�p�seket
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
	 * Firss�ti a j�t�kteret: minden klienst l�ptet, majd ki�rt�keli a l�p�seket
	 * �s elmenti a statisztik�kat a napl�ba
	 * 
	 * @param log Napl�z�sra haszn�lt objektum
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
	 * L�p�sek ki�rt�kel�se. Ha a k�t kliens ugyan arra a mez�re pr�b�l l�pni
	 * akkor egy k�l�nleges eset ({@link TieCrash}) keletkezik. K�l�nben a
	 * <code>handleClient</code> met�dus kezeli a klienseket.
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
	 * Kliens l�p�s�nek kezel�se. Ha padl�ra l�p akkor cs�kot h�z maga ut�n,
	 * k�l�nben meghal.
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
	 * Elind�tja a j�t�kot
	 * 
	 * @throws Exception nem lett bell�tva minden kliens
	 */
	public void start() throws Exception {
		grid.start();
	}

	public Grid getGrid() {
		return grid;
	}

	/**
	 * @return �l� kliensek sz�ma
	 */
	public int getNumAliveClients() {
		return grid.getAliveClients().size();
	}

	/**
	 * @return v�ge van-e a j�t�knak
	 */
	public boolean isOver() {
		return getNumAliveClients() < 2;
	}

}

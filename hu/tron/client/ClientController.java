package hu.tron.client;

import hu.tron.grid.Grid;

import java.awt.Point;

/**
 * Kliensek kezelését végzõ osztályok õsosztálya
 * 
 * @author Dávid Bajzáth
 */
public abstract class ClientController {

	/**
	 * Irányítandó kliens
	 */
	protected Client client;

	public ClientController(Client client) {
		this.client = client;
	}

	/**
	 * @return kliens következõ pozíciója
	 */
	public Point getNextPosition() {
		return client.getDirection().getTranslatedPoint(client.getPosition());
	}

	/**
	 * Lépteti a kienst
	 * 
	 * @param grid
	 *            játéktér
	 */
	public void step(Grid grid) {
		client.step();
	}

	public Client getClient() {
		return client;
	}

}

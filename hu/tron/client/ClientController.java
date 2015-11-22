package hu.tron.client;

import hu.tron.grid.Grid;

import java.awt.Point;

/**
 * Kliensek kezel�s�t v�gz� oszt�lyok �soszt�lya
 * 
 * @author D�vid Bajz�th
 */
public abstract class ClientController {

	/**
	 * Ir�ny�tand� kliens
	 */
	protected Client client;

	public ClientController(Client client) {
		this.client = client;
	}

	/**
	 * @return kliens k�vetkez� poz�ci�ja
	 */
	public Point getNextPosition() {
		return client.getDirection().getTranslatedPoint(client.getPosition());
	}

	/**
	 * L�pteti a kienst
	 * 
	 * @param grid
	 *            j�t�kt�r
	 */
	public void step(Grid grid) {
		client.step();
	}

	public Client getClient() {
		return client;
	}

}

package hu.tron.gridelement;

import hu.tron.client.Client;

import java.awt.Point;

/**
 * A játéktéren a kliens által húzott csík egy elemét reprezentáló osztály
 * 
 * @author Dávid Bajzáth
 */
public class Trail extends GridElement {
	
	/**
	 * Ez-e a kliens utolsó csík eleme, azaz fej-e
	 */
	public boolean isHead = true;
	/**
	 * A kliens akihez tartozik ez a csík elem
	 */
	private Client client;
	
	public Trail(int x, int y, Client client) {
		super(x, y);
		this.client = client;
	}
	
	public Trail(Point position, Client client) {
		super(position);
		this.client = client;
	}
	
	public Client getClient() {
		return client;
	}
	
	@Override
	public String toString() {
		return Integer.toString(client.getId());
	}
}

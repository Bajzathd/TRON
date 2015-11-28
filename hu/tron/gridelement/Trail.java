package hu.tron.gridelement;

import hu.tron.client.Client;

import java.awt.Point;

/**
 * A j�t�kt�ren a kliens �ltal h�zott cs�k egy elem�t reprezent�l� oszt�ly
 * 
 * @author D�vid Bajz�th
 */
public class Trail extends GridElement {
	
	/**
	 * Ez-e a kliens utols� cs�k eleme, azaz fej-e
	 */
	public boolean isHead = true;
	/**
	 * A kliens akihez tartozik ez a cs�k elem
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

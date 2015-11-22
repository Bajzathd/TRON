package hu.tron.renderer;

import hu.tron.client.Client;

import java.awt.event.KeyListener;
import java.util.List;

/**
 * Megjelenítõk közös interface-e
 * 
 * @author Dávid Bajzáth
 */
public interface Renderer {
	public void render();

	public void showResults(List<Client> aliveClients);

	public void addKeyListener(KeyListener keyListener);
}

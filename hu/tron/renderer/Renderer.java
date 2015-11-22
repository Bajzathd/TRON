package hu.tron.renderer;

import hu.tron.client.Client;

import java.awt.event.KeyListener;
import java.util.List;

/**
 * Megjelen�t�k k�z�s interface-e
 * 
 * @author D�vid Bajz�th
 */
public interface Renderer {
	public void render();

	public void showResults(List<Client> aliveClients);

	public void addKeyListener(KeyListener keyListener);
}

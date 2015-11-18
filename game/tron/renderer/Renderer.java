package game.tron.renderer;

import game.tron.client.Client;

import java.awt.event.KeyListener;
import java.util.List;

public interface Renderer {
	public void render();
	public void showResults(List<Client> aliveClients);
	public void addKeyListener(KeyListener keyListener);
}

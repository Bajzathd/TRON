package game.tron.renderer;

import game.tron.client.ClientController;

import java.awt.event.KeyListener;
import java.util.List;

public interface Renderer {
	public void render();
	public void showResults(List<ClientController> aliveClientControllers);
	public void addKeyListener(KeyListener keyListener);
}

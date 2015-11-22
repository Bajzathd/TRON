package hu.tron.grid;

import hu.tron.client.Client;
import hu.tron.client.player.Player;
import hu.tron.client.player.PlayerController;
import hu.tron.renderer.Renderer;
import hu.tron.renderer.SwingRenderer;

/**
 * Játéktér irányítását és megjelenítését kezelõ osztály
 * 
 * @author Dávid Bajzáth
 */
public class GridControllerWithView extends GridController {

	/**
	 * Játéktér megjelenítéséért felelõs objektum
	 */
	private Renderer renderer;

	public GridControllerWithView(Grid grid, Renderer renderer) {
		super(grid);
		this.renderer = renderer;
	}

	public GridControllerWithView(int width, int height, double obstacleRatio) {
		super(width, height, obstacleRatio);
		renderer = new SwingRenderer(grid);
	}

	@Override
	public void start() throws Exception {
		super.start();
		for (Client client : grid.getAliveClients()) {
			if (client instanceof Player) {
				renderer.addKeyListener(new PlayerController((Player) client));
			}
		}
	}

	/**
	 * Körönkénti megjelenítést végzi
	 */
	public void render() {
		renderer.render();
	}

	/**
	 * Eredmény hirdetést végzi
	 */
	public void showResults() {
		renderer.showResults(grid.getAliveClients());
	}

}

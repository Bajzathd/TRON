package hu.tron.grid;

import hu.tron.client.Client;
import hu.tron.client.player.Player;
import hu.tron.client.player.PlayerController;
import hu.tron.renderer.Renderer;
import hu.tron.renderer.SwingRenderer;

/**
 * J�t�kt�r ir�ny�t�s�t �s megjelen�t�s�t kezel� oszt�ly
 * 
 * @author D�vid Bajz�th
 */
public class GridControllerWithView extends GridController {

	/**
	 * J�t�kt�r megjelen�t�s��rt felel�s objektum
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
	 * K�r�nk�nti megjelen�t�st v�gzi
	 */
	public void render() {
		renderer.render();
	}

	/**
	 * Eredm�ny hirdet�st v�gzi
	 */
	public void showResults() {
		renderer.showResults(grid.getAliveClients());
	}

}

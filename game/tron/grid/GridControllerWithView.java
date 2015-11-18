package game.tron.grid;

import game.tron.client.Client;
import game.tron.client.ClientController;
import game.tron.client.Player;
import game.tron.client.PlayerKeyListener;
import game.tron.renderer.Renderer;
import game.tron.renderer.SwingRenderer;

public class GridControllerWithView extends GridController {
	
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
	public void start() {
		super.start();
		for (Client client : grid.getAliveClients()) {
			if (client instanceof Player) {
				Player player = (Player) client;
				renderer.addKeyListener(new PlayerKeyListener(
						player, ClientController.get(player).getControls()));
			}
		}
	}
	
	public void render() {
		renderer.render();
	}
	
	public void showResults() {
		renderer.showResults(grid.getAliveClients());
	}
	
}

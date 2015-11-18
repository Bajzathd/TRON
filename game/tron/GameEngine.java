package game.tron;

import game.tron.client.Player;
import game.tron.client.PlayerController;
import game.tron.client.PlayerKeyListener;
import game.tron.grid.Grid;
import game.tron.grid.GridController;
import game.tron.renderer.Renderer;
import game.tron.renderer.SwingRenderer;

public class GameEngine extends GridController {
	
	private Renderer renderer;
	
	public GameEngine(int width, int height, double obstacleRatio) {
		super(width, height, obstacleRatio);
		renderer = new SwingRenderer(grid);
	}
	
	public GameEngine(Grid grid, Renderer renderer) {
		super(grid);
		this.renderer = renderer;
	}
	
	public void addPlayer(Player player) throws Exception {
		grid.addPlayer(player);
		PlayerController playerController = new PlayerController(player);
		aliveClientControllers.add(playerController);
		renderer.addKeyListener(new PlayerKeyListener(player, playerController.getControls()));
	}
	
	public void render() {
		renderer.render();
	}
	
	public void showResults() {
		renderer.showResults(aliveClientControllers);
	}
	
}

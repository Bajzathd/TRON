package game.tron;

import game.tron.client.Client;
import game.tron.grid.GridControllerWithView;

public class Tron implements Runnable {

	/**
	 * Képfrissítés millisecben
	 */
	private static final long FRAME_LENGTH = 200000000L;
	
	private GridControllerWithView engine;

	public Tron(int width, int height, double obstacleRatio, 
			Client client1, Client client2) {
		
		engine = new GridControllerWithView(width, height, obstacleRatio);
		
		engine.getGrid().setClient1(client1);
		engine.getGrid().setClient2(client2);
		
		new Thread(this).start();
	}

	public void run() {
		long lastTime;
		long delta;

		engine.start();
		
		while (! engine.isOver()) {
			lastTime = System.nanoTime();

			// Játék állapot frissítés
			engine.update();
			// Renderelés
			engine.render();

			delta = System.nanoTime() - lastTime;
			if (delta < FRAME_LENGTH) {
				try {
					Thread.sleep((FRAME_LENGTH - delta) / 1000000L);
				} catch (Exception ex) {
					// Interrupted
				}
			}
		}
		engine.showResults();
	}

}

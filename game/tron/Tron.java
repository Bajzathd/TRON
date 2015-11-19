package game.tron;

import game.tron.client.ai.AI;
import game.tron.client.player.Player;
import game.tron.grid.GridControllerWithView;
import game.tron.utility.Log;

public class Tron implements Runnable {
	/**
	 * Grid szélessége
	 */
	public static final int WIDTH = 20;
	/**
	 * Grid magassága
	 */
	public static final int HEIGHT = 20;
	/**
	 * Maximális akadály arány
	 */
	public static final double OBSTACLE_RATIO = 0.05;
	/**
	 * Képfrissítés millisecben
	 */
	public static final long FRAME_LENGTH = 200000000L;
	/**
	 * Hány kör fusson le
	 */
	public static final int ROUNDS = 1;

	private GridControllerWithView engine = new GridControllerWithView(WIDTH,
			HEIGHT, OBSTACLE_RATIO);
	private Log log = new Log();

	public Tron() {
		engine.getGrid().setClient1(new Player(1));
//		engine.getGrid().setClient2(new Player(2));
//		engine.getGrid().setClient1(new AI(1, 2));
		engine.getGrid().setClient2(new AI(2, 4));
	}

	public void run() {
		long lastTime;
		long delta;

		for (int round = 1; round <= ROUNDS; round++) {
			engine.start();
			if (round == 1) {
				log.init(engine.getGrid().getAliveClients());
			}
			while (!engine.isOver()) {
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
			log.addResult(engine.getGrid());
		}
		// log.writeToFile();
	}

	public static void main(String[] args) {
		new Thread(new Tron()).start();
	}

}

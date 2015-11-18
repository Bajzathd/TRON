package game.tron;

import game.tron.client.AI;
import game.tron.client.Player;

public class Tron implements Runnable {
	/**
	 * Grid szélessége
	 */
	public static final int WIDTH = 96;
	/**
	 * Grid magassága
	 */
	public static final int HEIGHT = 64;
	/**
	 * Maximális akadály arány
	 */
	public static final double OBSTACLE_RATIO = 0.05;
	/**
	 * Képfrissítés millisecben
	 */
	public static final long FRAME_LENGTH = 200000000L;
	
	private GameEngine engine = new GameEngine(WIDTH, HEIGHT, OBSTACLE_RATIO);
	
	public Tron() {
		try {
			engine.addPlayer(new Player());
			engine.addPlayer(new Player());
			engine.addAI(new AI(1));
			engine.addAI(new AI(1));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		engine.start();
	}

	public void run() {
		long lastTime;
		long delta;
		
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
	
	public static void main(String[] args) {
		Thread t = new Thread(new Tron());
		t.start();
    }

}

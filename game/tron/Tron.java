package game.tron;

import game.tron.client.AI;
import game.tron.client.Player;

public class Tron implements Runnable {
	/**
	 * Grid sz�less�ge
	 */
	public static final int WIDTH = 96;
	/**
	 * Grid magass�ga
	 */
	public static final int HEIGHT = 64;
	/**
	 * Maxim�lis akad�ly ar�ny
	 */
	public static final double OBSTACLE_RATIO = 0.05;
	/**
	 * K�pfriss�t�s millisecben
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
			
			// J�t�k �llapot friss�t�s
			engine.update();
			// Renderel�s
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

package hu.tron;

import hu.tron.client.Client;
import hu.tron.grid.GridControllerWithView;

/**
 * Játék futtatásáért felelõs osztály
 * 
 * @author Dávid Bajzáth
 */
public class Tron implements Runnable {

	/**
	 * Képfrissítés millisecben
	 */
	private static final long FRAME_LENGTH = 200000000L;

	/**
	 * Játék motorja
	 */
	private GridControllerWithView engine;

	/**
	 * Játék inicializálását, majd indítását végzi
	 * 
	 * @param width
	 *            pálya szélessége
	 * @param height
	 *            pálya magassága
	 * @param obstacleRatio
	 *            akadályok minimális aránya
	 * @param client1
	 *            elsõ kliens
	 * @param client2
	 *            második kliens
	 */
	public Tron(int width, int height, double obstacleRatio, Client client1,
			Client client2) {

		engine = new GridControllerWithView(width, height, obstacleRatio);

		engine.getGrid().setClient1(client1);
		engine.getGrid().setClient2(client2);

		new Thread(this).start();
	}

	@Override
	public void run() {
		long lastTime;
		long delta;

		try {
			engine.start();
	
			while (!engine.isOver()) { // játék ciklus
				lastTime = System.nanoTime();
	
				engine.update();
				engine.render();
	
				delta = System.nanoTime() - lastTime;
				
				/*
				 * framerate maximalizálása érdekében szüneteltetjük a szál
				 * futását ha szükséges
				 */
				if (delta < FRAME_LENGTH) {
					try {
						Thread.sleep((FRAME_LENGTH - delta) / 1000000L);
					} catch (InterruptedException ex) {}
				}
			}
			engine.showResults();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}

}

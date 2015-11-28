package hu.tron;

import hu.tron.client.Client;
import hu.tron.grid.GridControllerWithView;

/**
 * J�t�k futtat�s��rt felel�s oszt�ly
 * 
 * @author D�vid Bajz�th
 */
public class Tron implements Runnable {

	/**
	 * K�pfriss�t�s millisecben
	 */
	private static final long FRAME_LENGTH = 200000000L;

	/**
	 * J�t�k motorja
	 */
	private GridControllerWithView engine;

	/**
	 * J�t�k inicializ�l�s�t, majd ind�t�s�t v�gzi
	 * 
	 * @param width
	 *            p�lya sz�less�ge
	 * @param height
	 *            p�lya magass�ga
	 * @param obstacleRatio
	 *            akad�lyok minim�lis ar�nya
	 * @param client1
	 *            els� kliens
	 * @param client2
	 *            m�sodik kliens
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
	
			while (!engine.isOver()) { // j�t�k ciklus
				lastTime = System.nanoTime();
	
				engine.update();
				engine.render();
	
				delta = System.nanoTime() - lastTime;
				
				/*
				 * framerate maximaliz�l�sa �rdek�ben sz�neteltetj�k a sz�l
				 * fut�s�t ha sz�ks�ges
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

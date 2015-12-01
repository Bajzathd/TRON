package hu.tron;

import hu.tron.client.Client;
import hu.tron.client.ai.AI;
import hu.tron.client.ai.AIController;
import hu.tron.grid.Grid;
import hu.tron.grid.GridController;
import hu.tron.utility.Log;

/**
 * Szimul�ci�k futtat�s�t v�gz� oszt�ly
 * 
 * @author D�vid Bajz�th
 */
public class TronSimulation implements Runnable {

	/**
	 * P�lya sz�less�ge
	 */
	private int width;
	/**
	 * P�lya magass�ga
	 */
	private int height;
	/**
	 * Akad�lyok minim�lis ar�nya
	 */
	private double obstacleRatio;
	/**
	 * K�r�k sz�ma
	 */
	private int rounds;
	/**
	 * Els� kliens
	 */
	private Client client1;
	/**
	 * M�sodik kliens
	 */
	private Client client2;
	/**
	 * Megjelen�t�s
	 */
	private SimulationView view;
	/**
	 * Logk�sz�t�
	 */
	private Log log;

	/**
	 * Szimul�ci� inicializ�l�s�t �s elind�t�s�t v�gz� konstruktor
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
	 * @param rounds
	 *            k�r�k sz�ma
	 */
	public TronSimulation(int width, int height, double obstacleRatio,
			Client client1, Client client2, int rounds) {
		
		this.width = width;
		this.height = height;
		this.obstacleRatio = obstacleRatio;
		this.rounds = rounds;

		this.client1 = client1;
		this.client2 = client2;

		view = new SimulationView(rounds);
		log = new Log(client1, client2);

		new Thread(this).start();
	}

	@Override
	public void run() {
		for (int round = 1; round <= rounds; round++) {
			new SimulationRound(client1.clone(), client2.clone());
		}
		log.writeToFile();
		view.done();
	}

	/**
	 * Egy szimul�ci� futtat�s�t v�gz� sz�l
	 * 
	 * @author D�vid Bajz�th
	 */
	private class SimulationRound {

		/**
		 * J�t�k motorja
		 */
		private GridController engine;

		/**
		 * K�r inicializ�l�sa
		 * 
		 * @param client1
		 *            els� kliens
		 * @param client2
		 *            m�sodik kliens
		 * @param log
		 *            logol�
		 */
		public SimulationRound(Client client1, Client client2) {
			engine = new GridController(width, height, obstacleRatio);
			engine.getGrid().setClient1(client1);
			engine.getGrid().setClient2(client2);
			
			run();
		}

		private void run() {
			try {
				engine.start();
				do {
					Grid grid = engine.getGrid();
					for (Client client : grid.getAliveClients()) {
						long startTime = System.nanoTime();
						AIController.get((AI) client).step(grid);
						log.addStepTime(client, System.nanoTime() - startTime);
					}
					engine.evaluate();
				} while (!engine.isOver());
				view.progress();
				log.addResult(engine.getGrid());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

}

package hu.tron;

import hu.tron.client.Client;
import hu.tron.client.ai.AI;
import hu.tron.client.ai.AIController;
import hu.tron.grid.Grid;
import hu.tron.grid.GridController;
import hu.tron.utility.Log;

/**
 * Szimulációk futtatását végzõ osztály
 * 
 * @author Dávid Bajzáth
 */
public class TronSimulation implements Runnable {

	/**
	 * Pálya szélessége
	 */
	private int width;
	/**
	 * Pálya magassága
	 */
	private int height;
	/**
	 * Akadályok minimális aránya
	 */
	private double obstacleRatio;
	/**
	 * Körök száma
	 */
	private int rounds;
	/**
	 * Elsõ kliens
	 */
	private Client client1;
	/**
	 * Második kliens
	 */
	private Client client2;
	/**
	 * Megjelenítés
	 */
	private SimulationView view;
	/**
	 * Logkészítõ
	 */
	private Log log;

	/**
	 * Szimuláció inicializálását és elindítását végzõ konstruktor
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
	 * @param rounds
	 *            körök száma
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
	 * Egy szimuláció futtatását végzõ szál
	 * 
	 * @author Dávid Bajzáth
	 */
	private class SimulationRound {

		/**
		 * Játék motorja
		 */
		private GridController engine;

		/**
		 * Kör inicializálása
		 * 
		 * @param client1
		 *            elsõ kliens
		 * @param client2
		 *            második kliens
		 * @param log
		 *            logoló
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

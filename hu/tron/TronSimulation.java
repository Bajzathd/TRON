package hu.tron;

import java.util.ArrayList;
import java.util.List;

import hu.tron.client.Client;
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
	 * Szimulációk szálait tároló lista
	 */
	private List<Thread> simulations = new ArrayList<Thread>();

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

		initSimulations();

		new Thread(this).start();
	}

	/**
	 * Szimulációk listához adását és elindítását végzi
	 */
	private void initSimulations() {
		for (int round = 1; round <= rounds; round++) {
			Thread simulation = new Thread(new SimulationRound(client1.clone(),
					client2.clone(), log));

			simulation.start();
			simulations.add(simulation);
		}
	}

	@Override
	public void run() {
		for (Thread simulation : simulations) {
			try {
				simulation.join();
			} catch (Exception e) {
				// interrupted
			}
		} // minden játék lefutott

		log.writeToFile();
		view.done();
	}

	/**
	 * Egy szimuláció futtatását végzõ szál
	 * 
	 * @author Dávid Bajzáth
	 */
	private class SimulationRound implements Runnable {

		/**
		 * Játék motorja
		 */
		private GridController engine;
		/**
		 * Logoló
		 */
		private Log log;

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
		public SimulationRound(Client client1, Client client2, Log log) {
			
			engine = new GridController(width, height, obstacleRatio);
			engine.getGrid().setClient1(client1);
			engine.getGrid().setClient2(client2);

			this.log = log;
		}

		@Override
		public void run() {
			try {
				engine.start();
	
				while (!engine.isOver()) {
					engine.update();
				}
	
				view.progress();
				
				log.addResult(engine.getGrid());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

}

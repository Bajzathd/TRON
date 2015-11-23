package hu.tron;

import java.util.ArrayList;
import java.util.List;

import hu.tron.client.Client;
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
	 * Szimul�ci�k sz�lait t�rol� lista
	 */
	private List<Thread> simulations = new ArrayList<Thread>();

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

		initSimulations();

		new Thread(this).start();
	}

	/**
	 * Szimul�ci�k list�hoz ad�s�t �s elind�t�s�t v�gzi
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
		} // minden j�t�k lefutott

		log.writeToFile();
		view.done();
	}

	/**
	 * Egy szimul�ci� futtat�s�t v�gz� sz�l
	 * 
	 * @author D�vid Bajz�th
	 */
	private class SimulationRound implements Runnable {

		/**
		 * J�t�k motorja
		 */
		private GridController engine;
		/**
		 * Logol�
		 */
		private Log log;

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

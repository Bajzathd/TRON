package game.tron;

import java.util.ArrayList;
import java.util.List;

import game.tron.client.Client;
import game.tron.grid.GridController;
import game.tron.utility.Log;
import game.tron.utility.SimulationView;

public class TronSimulation implements Runnable {
	
	private int width;
	private int height;
	private double obstacleRatio;
	private int rounds;
	
	private Client client1;
	private Client client2;

	private SimulationView view;
	private Log log;
	private List<Thread> simulations = new ArrayList<Thread>();
	
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

	private void initSimulations() {
		for (int round = 1; round <= rounds; round++) {
			Thread simulation = new Thread(new SimulationRound(
					client1.clone(),
					client2.clone(),
					log));
			
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
	
	private class SimulationRound implements Runnable {
		
		private GridController engine;
		private Log log;
		
		public SimulationRound(Client client1, Client client2, Log log) {
			
			engine = new GridController(width, height, obstacleRatio);
			engine.getGrid().setClient1(client1);
			engine.getGrid().setClient2(client2);
			
			this.log = log;
		}

		@Override
		public void run() {
			engine.start();
			
			while (! engine.isOver()) {
				// Játék állapot frissítés
				engine.update();
			}
			
			view.progress();
			log.addResult(engine.getGrid());
		}
		
	}

}

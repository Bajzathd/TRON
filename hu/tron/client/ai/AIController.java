package hu.tron.client.ai;

import hu.tron.client.ClientController;
import hu.tron.heuristic.Heuristic;

/**
 * AI-kat kezelõ osztályok õsosztálya
 * 
 * @author Dávid Bajzáth
 */
public abstract class AIController extends ClientController {

	public AIController(AI ai) {
		super(ai);
	}

	/**
	 * Példányosítja a klienshez megfelelõ kezelõ osztályt
	 * 
	 * @param ai
	 *            kliens
	 * @return klienskezelõ
	 */
	public static AIController get(AI ai) {
		if (ai instanceof MinimaxAI) {
			return new MinimaxAIController((MinimaxAI) ai);
		} else {
			return new RandomAIController((AI) ai);
		}
	}
	
	/**
	 * @return Random AI modell
	 */
	public static AI getNewModel() {
		return new AI(ClientController.nextId++);
	}
	
	/**
	 * @param type heurisztika típusa
	 * @param level minimax fa maximális mélysége
	 * @return Minimax AI modell
	 */
	public static AI getNewModel(Heuristic.type type, int level) {
		return new MinimaxAI(ClientController.nextId++, type, level);
	}
	
}

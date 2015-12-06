package hu.tron.client.ai;

import hu.tron.client.ClientController;
import hu.tron.heuristic.Heuristic;

/**
 * AI-kat kezel� oszt�lyok �soszt�lya
 * 
 * @author D�vid Bajz�th
 */
public abstract class AIController extends ClientController {

	public AIController(AI ai) {
		super(ai);
	}

	/**
	 * P�ld�nyos�tja a klienshez megfelel� kezel� oszt�lyt
	 * 
	 * @param ai
	 *            kliens
	 * @return klienskezel�
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
	 * @param type heurisztika t�pusa
	 * @param level minimax fa maxim�lis m�lys�ge
	 * @return Minimax AI modell
	 */
	public static AI getNewModel(Heuristic.type type, int level) {
		return new MinimaxAI(ClientController.nextId++, type, level);
	}
	
}

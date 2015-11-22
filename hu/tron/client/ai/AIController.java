package hu.tron.client.ai;

import hu.tron.client.ClientController;

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
		int level = ai.getLevel();

		if (level <= 1) {
			return new RandomAIController(ai);
		} else {
			return new MinimaxAIController(ai);
		}
	}

}

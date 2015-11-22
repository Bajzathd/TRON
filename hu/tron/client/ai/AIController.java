package hu.tron.client.ai;

import hu.tron.client.ClientController;

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
		int level = ai.getLevel();

		if (level <= 1) {
			return new RandomAIController(ai);
		} else {
			return new MinimaxAIController(ai);
		}
	}

}

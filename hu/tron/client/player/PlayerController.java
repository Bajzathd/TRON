package hu.tron.client.player;

import hu.tron.client.ClientController;
import hu.tron.utility.Direction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Játékos billenytû leütéseit fogadó osztály. Játékoshoz irányítást rendel,
 * majd ha a leütött billentyûhöz van rendelve egy irány akkor megpróbálja abba
 * az irányba fordítani a játékost.
 * 
 * @author Dávid Bajzáth
 */
public class PlayerController extends ClientController implements KeyListener {

	/**
	 * Játékos irányítása, azaz egy billentyû egy irányhoz rendelése
	 */
	private Map<String, Direction> controls = new HashMap<String, Direction>();

	/**
	 * Beállítja a játékos irányítását
	 * 
	 * @param player játékos
	 */
	public PlayerController(Player player) {
		super(player);

		switch (player.getId()) {
		case 1:
			controls.put("Up", Direction.UP);
			controls.put("Right", Direction.RIGHT);
			controls.put("Down", Direction.DOWN);
			controls.put("Left", Direction.LEFT);
			break;
		case 2:
			controls.put("W", Direction.UP);
			controls.put("D", Direction.RIGHT);
			controls.put("S", Direction.DOWN);
			controls.put("A", Direction.LEFT);
			break;
		}
	}

	public Map<String, Direction> getControls() {
		return controls;
	}
	
	public static Player getNewModel() {
		return new Player(ClientController.nextId++);
	}

	/**
	 * Minden billentyû leütésnél meghívódik. Ha a leütött billentyûhöz lett
	 * rendelve irány akkor megpróbálja abba az irányba fordítani a játékost.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		String keyText = KeyEvent.getKeyText(e.getKeyCode());

		if (client.isAlive() && controls.containsKey(keyText)) {
			client.trySetDirection(controls.get(keyText));
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

}

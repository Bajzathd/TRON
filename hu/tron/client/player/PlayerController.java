package hu.tron.client.player;

import hu.tron.client.ClientController;
import hu.tron.utility.Direction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 * J�t�kos billenyt� le�t�seit fogad� oszt�ly. J�t�koshoz ir�ny�t�st rendel,
 * majd ha a le�t�tt billenty�h�z van rendelve egy ir�ny akkor megpr�b�lja abba
 * az ir�nyba ford�tani a j�t�kost.
 * 
 * @author D�vid Bajz�th
 */
public class PlayerController extends ClientController implements KeyListener {

	/**
	 * J�t�kos ir�ny�t�sa, azaz egy billenty� egy ir�nyhoz rendel�se
	 */
	private Map<String, Direction> controls = new HashMap<String, Direction>();

	/**
	 * Be�ll�tja a j�t�kos ir�ny�t�s�t
	 * 
	 * @param player j�t�kos
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
	 * Minden billenty� le�t�sn�l megh�v�dik. Ha a le�t�tt billenty�h�z lett
	 * rendelve ir�ny akkor megpr�b�lja abba az ir�nyba ford�tani a j�t�kost.
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

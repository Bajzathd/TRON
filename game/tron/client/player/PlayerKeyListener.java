package game.tron.client.player;

import game.tron.utility.Direction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;

public class PlayerKeyListener implements KeyListener {
	
	private Player player;
	Map<String, Direction> controls;
	
	public PlayerKeyListener(Player player, Map<String, Direction> controls) {
		this.player = player;
		this.controls = controls;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		String keyText = KeyEvent.getKeyText(e.getKeyCode());
		
		if (player.isAlive() && controls.containsKey(keyText)) {
			player.trySetDirection(controls.get(keyText));
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

}

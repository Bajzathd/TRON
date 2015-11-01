package client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Player1 extends Client {

	public Player1(int id) {
		super(id);
		this.name = "Player1";
		this.controls = new ControlsP1();
	}
	
	public class ControlsP1 extends Controls {

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()){
			case KeyEvent.VK_UP:
				trySetDirection(Client.DIRECTION_UP);
				break;
			case KeyEvent.VK_RIGHT:
				trySetDirection(Client.DIRECTION_RIGHT);
				break;
			case KeyEvent.VK_DOWN:
				trySetDirection(Client.DIRECTION_DOWN);
				break;
			case KeyEvent.VK_LEFT:
				trySetDirection(Client.DIRECTION_LEFT);
				break;
			}
		}
	}

}

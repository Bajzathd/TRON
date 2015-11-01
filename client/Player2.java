package client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Player2 extends Client {

	public Player2(int id) {
		super(id);
		this.name = "Player2";
		this.controls = new ControlsP2();
	}
	
	public class ControlsP2 extends Controls {
		
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()){
			case KeyEvent.VK_W:
				trySetDirection(Client.DIRECTION_UP);
				break;
			case KeyEvent.VK_D:
				trySetDirection(Client.DIRECTION_RIGHT);
				break;
			case KeyEvent.VK_S:
				trySetDirection(Client.DIRECTION_DOWN);
				break;
			case KeyEvent.VK_A:
				trySetDirection(Client.DIRECTION_LEFT);
				break;
			}
		}
	}

}

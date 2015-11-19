package game.tron.client.player;

import game.tron.client.ClientController;
import game.tron.utility.Direction;

import java.util.HashMap;
import java.util.Map;


public class PlayerController extends ClientController {
	
	private Map<String, Direction> controls = new HashMap<String, Direction>();

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

}

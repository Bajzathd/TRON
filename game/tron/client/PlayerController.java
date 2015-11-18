package game.tron.client;

import game.tron.utility.Direction;

import java.util.HashMap;
import java.util.Map;


public class PlayerController extends ClientController {
	
	private Map<String, Direction> controls = new HashMap<String, Direction>();

	public PlayerController(Player player) throws Exception {
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
		default:
			throw new Exception("Invalid player id: "+player.getId()+" (valid ones are 1 and 2)");
		}
	}
	
	public Map<String, Direction> getControls() {
		return controls;
	}

}

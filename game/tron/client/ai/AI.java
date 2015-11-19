package game.tron.client.ai;

import game.tron.client.Client;


public class AI extends Client {
	
	private int level;
	
	public AI(int id, int level) {
		super(id);
		this.level = level;
	}
	
	@Override
	public AI clone() {
		AI clone = new AI(id, level);
		
		clone.position = position;
		clone.alive = alive;
		clone.direction = direction;
		
		return clone;
	}
	
	public int getLevel() {
		return level;
	}
	
	public String toString() {
		return "AI (level"+level+")";
	}

}

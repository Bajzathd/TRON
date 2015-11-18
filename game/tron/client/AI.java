package game.tron.client;


public class AI extends Client {
	
	private int level;
	
	public AI(int level) {
		super();
		this.level = level;
	}
	
	public int getLevel() {
		return level;
	}
	
	public String toString() {
		return "AI#"+id+" (level"+level+")";
	}
	
}

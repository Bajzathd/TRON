package game.tron.utility;

import java.awt.Point;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Direction {
	UP, 
	RIGHT, 
	DOWN, 
	LEFT;
	
	private static Random random = new Random();
	
	public boolean isOpposite(Direction d){
		return this.ordinal() != d.ordinal() 
			&& (this.ordinal() + d.ordinal()) % 2 == 0;
	}
	
	public static Direction getRandom() {
		return Direction.values()[random.nextInt(Direction.values().length)];
	}
	
	public static List<Direction> getAllShuffled() {
		List<Direction> directions = Arrays.asList(Direction.values());
		Collections.shuffle(directions);
		
		return directions;
	}
	
	public Point getTranslatedPoint(Point p) {
		Point clone = new Point(p);
		
		switch (this){
		case UP:
			clone.translate(0, -1); 
			break;
		case RIGHT: 
			clone.translate(1, 0); 
			break;
		case DOWN: 
			clone.translate(0, 1); 
			break;
		case LEFT: 
			clone.translate(-1, 0);
			break;
		}
		
		return clone;
	}
}

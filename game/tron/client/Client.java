package game.tron.client;

import game.tron.grid.element.Trail;
import game.tron.utility.Direction;

import java.awt.Point;

public abstract class Client {
	
	public static int idCounter = 1;
	
	protected int id;
	protected Direction direction;
	protected boolean alive;
	
	private Point position = new Point();
	
	public Client(){
		this.id = idCounter++;
	}
	
	public void setStart(int x, int y, Direction startDirection){
		alive = true;
		
		position.x = x;
		position.y = y;
		direction = startDirection;
	}
	
	public void setStart(Point position, Direction startDirection){
		alive = true;
		
		this.position = position;
		direction = startDirection;
	}
	
	public void trySetDirection(Direction direction){
		if (! direction.isOpposite(this.direction)) {
			this.direction = direction;
		}
	}
	
	public Direction getDirection(){
		return direction;
	}
	
	public int getId(){
		return id;
	}
	
	public int getX() {
		return position.x;
	}
	
	public int getY() {
		return position.y;
	}
	
	public Point getPosition() {
		return position;
	}
	
	public void step() {
		position = direction.getTranslatedPoint(position);
	}
	
	public Trail getTrail() {
		return new Trail((Point) position.clone(), id);
	}
	
	public void kill(){
		alive = false;
	}
	
	public boolean isAlive(){
		return alive;
	}
	
}

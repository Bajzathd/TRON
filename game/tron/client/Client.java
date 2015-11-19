package game.tron.client;

import game.tron.gridelement.Trail;
import game.tron.utility.Direction;

import java.awt.Point;

public abstract class Client {
	
	protected int id;
	protected Direction direction;
	protected boolean alive;
	
	protected Point position = new Point();
	
	public Client(int id){
		if (id != 1 && id != 2) {
			System.out.println("Invalid client id, either choose 1 or 2");
			System.exit(1);
		}
		this.id = id;
	}
	
	public abstract Client clone();
	
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
		if (direction != null && 
				! direction.isOpposite(this.direction)) {
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
		return new Trail((Point) position.clone(), clone());
	}
	
	public void kill(){
		alive = false;
	}
	
	public boolean isAlive(){
		return alive;
	}
	
}

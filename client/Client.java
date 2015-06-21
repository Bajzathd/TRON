package client;

import java.awt.Point;

public class Client {
	
	public static final int DIRECTION_UP = 0;
	public static final int DIRECTION_RIGHT = 1;
	public static final int DIRECTION_DOWN = 2;
	public static final int DIRECTION_LEFT = 3;
	
	private Point position;
	private int id;
	private int direction;
	private boolean alive = true;
	
	public Client(final int id, final Point startPosition){
		
		this.id = id;
		this.position = startPosition;
		
	}
	
	public void step(){
		switch (this.direction){
			case DIRECTION_UP: 	this.position.translate(0, -1); break;
			case DIRECTION_RIGHT: this.position.translate(1, 0); break;
			case DIRECTION_DOWN: 	this.position.translate(0, 1); break;
			case DIRECTION_LEFT: 	this.position.translate(-1, 0); break;
		}
	}
	
	public void setDirection(final int direction){
		this.direction = direction;
	}
	
	public Point getPosition(){
		return this.position;
	}
	
	public int getId(){
		return this.id;
	}
	
	public void kill(){
		this.alive = false;
	}
	
	public boolean isAlive(){
		return this.alive;
	}

}

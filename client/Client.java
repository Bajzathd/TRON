package client;

import java.awt.Point;

public class Client {
	
	private int id;
	private int direction;
	private boolean alive;
	
	public static final int DIRECTION_UP = 0;
	public static final int DIRECTION_RIGHT = 1;
	public static final int DIRECTION_DOWN = 2;
	public static final int DIRECTION_LEFT = 3;
	
	public Client(final int id){
		this.id = id;
	}
	
	public void setStart(final int startDirection){
		this.alive = true;
		this.direction = startDirection;
	}
	
	public int getDirection(){
		return this.direction;
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

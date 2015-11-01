package client;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Client {
	
	protected int id;
	private int direction;
	protected boolean alive;
	protected Controls controls;
	
	protected String name;
	public Color color;
	
	public static final int DIRECTION_UP = 0;
	public static final int DIRECTION_RIGHT = 1;
	public static final int DIRECTION_DOWN = 2;
	public static final int DIRECTION_LEFT = 3;
	
	public Client(final int id){
		this.id = id;
		
		int[] colorComponents = {0, 0, 0};
		if( id < 3 ){
			colorComponents[id] = 255;
		} else {
			colorComponents[id%3] = 255;
			colorComponents[(id+1)%3] = 255;
		}
		
		this.name = "CPU#"+id;
		
		this.color = new Color(
			colorComponents[0],
			colorComponents[1],
			colorComponents[2]
		);
		
		this.controls = new Controls();
	}
	
	public void setStart(final int startDirection){
		this.alive = true;
		this.direction = startDirection;
	}
	
	public int getDirection(){
		return this.direction;
	}
	
	protected void trySetDirection(final int direction){
		if( 
			this.direction == direction 				//azonos irány
			|| (this.direction + direction) % 2 == 0	//ellentétes irány
		){
			return;
		}
		
		this.direction = direction;
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
	
	public String getName(){
		return this.name;
	}
	
	public Controls getControls(){
		return this.controls;
	}
	
	protected class Controls implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}

}

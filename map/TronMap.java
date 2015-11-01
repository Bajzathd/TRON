package map;

import java.awt.Point;
import java.awt.Rectangle;

public class TronMap {
	private int width;
	private int height;
	private int[][] map;
	private Rectangle mapRectangle;
	
	public static final int FREE = 0;
	public static final int OBSTACLE = -1;
	
	public TronMap(int width, int height){
		this.width = width;
		this.height = height;
		this.mapRectangle = new Rectangle(width, height);
		
		Tron3dMapGenerator generator = new Tron3dMapGenerator(width, height);
		this.map = generator.getMap();
	}
	
	public int getValue(final Point p){
		if(this.mapRectangle.contains(p)){
			return this.map[p.x][p.y];
		} else {
			return TronMap.OBSTACLE;
		}
	}
	
	public int getValue(final int x, final int y){
		if(this.mapRectangle.contains(x, y)){
			return this.map[x][y];
		} else {
			return TronMap.OBSTACLE;
		}
	}
	
	public void setValue(final Point p, final int value){
		this.map[p.x][p.y] = value;
	}
	
	public int getWidth(){
		return this.width;
	}
	
	public int getHeight(){
		return this.height;
	}
	
	public int[][] getMap(){
		return this.map;
	}
	
}

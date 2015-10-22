package map;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
		
		TronMapGenerator gen = new TronMapGenerator(width, height);
		this.map = gen.generate();
	}
	
	public void print(){
		for(int x = 0; x <= width; x++){
			System.out.print("#");
		}
		System.out.println("#");
		for(int y = 0; y < height; y++){
			System.out.print("#");
			for(int x = 0; x < width; x++){
				if(this.map[x][y] == 0){
					System.out.print(" ");
				} else {
					System.out.print(this.map[x][y] == -1 ? "#" : this.map[x][y]);
				}
			}
			System.out.println("#");
		}
		for(int x = -1; x <= width; x++){
			System.out.print("#");
		}
		System.out.println();
	}
	
	public int getValue(final Point p){
		return this.map[p.x][p.y];
	}
	
	public void setValue(final Point p, final int value){
		this.map[p.x][p.y] = value;
	}
	
	public boolean contains(Point p){
		return this.mapRectangle.contains(p);
	}

}

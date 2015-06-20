package map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TronMap {
	private int width;
	private int height;
	private Map<Point, Integer> map;
	
	public TronMap(int width, int height){
		this.width = width;
		this.height = height;
		
		TronMapGenerator gen = new TronMapGenerator();
		this.map = gen.generateMap(width, height);
	}
	
	public int getPoint(Point p){
		return this.map.get(p);
	}
	
	private void setPoint(Point p, int value){
		map.put(p, value);
	}
	
	public void printMap(){
		Point p;
		
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				p = new Point(x,y);
				
				if(getPoint(p) == 0){
					System.out.print(" ");
				} else {
					System.out.print("#");
				}
//				System.out.printf("%d ", this.map.get(p));
				
			}
			System.out.println();
		}
	}

}

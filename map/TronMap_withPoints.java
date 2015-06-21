package map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TronMap_withPoints {
	private int width;
	private int height;
	private Map<Point, Integer> map;
	
	public TronMap_withPoints(int width, int height){
		this.width = width;
		this.height = height;
		
		TronMapGenerator_withPoints gen = new TronMapGenerator_withPoints();
		this.map = gen.generateMap(width, height);
	}
	
	public void printMap(){
		Point p;
		
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				if(this.map.get(new Point(x, y)) == 0){
					System.out.print(" ");
				} else {
					System.out.print("#");
				}
			}
			System.out.println();
		}
	}

}

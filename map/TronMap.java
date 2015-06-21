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
	private int[][] map;
	
	public TronMap(int width, int height){
		this.width = width;
		this.height = height;
		
		TronMapGenerator gen = new TronMapGenerator(width, height);
		this.map = gen.generate();
	}
	
	public void printMap(){
		Point p;
		
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				if(this.map[x][y] == 0){
					System.out.print(" ");
				} else {
					System.out.print("#");
				}
			}
			System.out.println();
		}
	}

}

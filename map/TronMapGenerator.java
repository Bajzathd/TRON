package map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class TronMapGenerator {
	
	private int width;
	private int height;
	private int[][] map;
	
	private Random rnd;
	private ArrayList<Point> visitedPoints;
	private int obstacles;
	
	private static final double LIMIT = 0.05;
	
	public static final int FREE = 0;
	public static final int OBSTACLE = -1;
	private static final int TEMP_OBSTACLE = -2;

	public TronMapGenerator(int width, int height) {
		
		this.width = width;
		this.height = height;
		this.map = new int[width][height];
		
		this.rnd = new Random();
	}
	
	/**
	 * Legener�lja a p�ly�t
	 * 0: j�tszhat� p�lyar�sz
	 * <0: akad�ly
	 * >0: j�t�kos
	 */
	public int[][] generate(){
		int x, y;
		
		//minden mez�t j�tszhat�ra �ll�tunk
		for(y = 0; y < height; y++){
			for(x = 0; x < width; x++){
				this.map[x][y] = FREE;
			}
		}
		
		do{
			x = this.rnd.nextInt(width);
			y = this.rnd.nextInt(height);
			this.visitedPoints = new ArrayList<Point>();
			
			this.pacagen(x, y);
			this.checkDrop();
//			System.out.printf("visited:%d sum:%d obstacles:%d isUnderLimit:%b\n", visitedPoints.size(), (width*height), obstacles, isUnderLimit());
//			printMap();
		} while(this.isUnderLimit());
		
		return this.map;
	}
	
	private void pacagen(final int x, final int y){
		if(
			!isValidPoint(x, y)			//kil�g a mapr�l a pont
			|| map[x][y] != FREE		//m�r elfoglalt a pont
			|| rnd.nextDouble() < 0.6	//es�ly
		){
			return;
		}
		map[x][y] = TEMP_OBSTACLE;		//megpr�b�ljuk elfoglalni
		obstacles++;					//akad�lyok sz�ma n�tt
		
		//szomsz�dokra is megh�vjuk
		pacagen(x-1, y);
		pacagen(x+1, y);
		pacagen(x, y-1);
		pacagen(x, y+1);
	}
	
	private boolean isValidPoint(final int x, final int y){
		return 
			x >= 0 
			&& x < width 
			&& y >= 0 
			&& y < height;
	}

	private void checkDrop(){
		try{
			Point p = getFirstEmpty();
			visitPoint((int) p.getX(), (int) p.getY());
			handleLastDrop(visitedPoints.size() == (width * height - obstacles)); //minden nem akad�ly pont be lett-e j�rva
		} catch(Exception e){
			handleLastDrop(false);
		}
	}
	
	private Point getFirstEmpty() throws Exception{
		Point p;
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
				if(map[x][y] == FREE){
					return new Point(x, y);
				}
			}
		}
		throw new Exception("Nincs �res pont");
	}
	
	private void visitPoint(final int x, final int y){
		Point p = new Point(x, y);
		
		if(
			!isValidPoint(x, y)				//lel�g a mapr�l
			|| map[x][y] != FREE			//nem �res pont
			|| visitedPoints.contains(p)	//m�r megl�togattuk
		){
			return;
		}
		
		visitedPoints.add(p);
		
		visitPoint(x-1, y);
		visitPoint(x+1, y);
		visitPoint(x, y-1);
		visitPoint(x, y+1);
	}
	
	private void handleLastDrop(final boolean isValid){
		int x
			,y
			,value = isValid ? OBSTACLE : FREE
			,tempObstacles = 0;
		
		for(y=0; y<height; y++){
			for(x=0; x<width; x++){
				if(map[x][y] == TEMP_OBSTACLE){
					map[x][y] = value;
					tempObstacles++;
				}
			}
		}
		
		if(!isValid){
			obstacles -= tempObstacles;
		}
	}
	
	private boolean isUnderLimit(){
		return ((double)obstacles / (width * height)) <= LIMIT; 
	}

}

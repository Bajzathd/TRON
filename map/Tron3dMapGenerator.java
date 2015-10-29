package map;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class Tron3dMapGenerator {
	
	private int width;
	private int height;
	private int[][] map;
	private Rectangle mapRectangle;
	
	private Random rnd = new Random();
	private ArrayList<Point> visitedPoints;
	private ArrayList<Point> tempObstacles;
	private int numObstacles;
	
	private static final double LIMIT = 0.05;
	
	public Tron3dMapGenerator(final int width, final int height) {
		this.width = width;
		this.height = height;
		this.map = new int[width][height];
		this.mapRectangle = new Rectangle(width, height);
	}
	
	/**
	 * Legenerálja a pályát
	 * 0: játszható pályarész
	 * <0: akadály
	 * >0: játékos
	 */
	public int[][] getMap(){
		do {
			this.generateMap();
		} while( !this.isValidMap() );
		
		return this.map;
	}
	
	private void generateMap(){
		this.reset();
		
		do {
			this.raiseCell(
					this.rnd.nextInt(this.width), 
					this.rnd.nextInt(this.height), 
					Integer.MAX_VALUE
			);
			
		} while( this.isUnderLimit() );
	}
	
	private void reset(){
		//minden mezõt játszhatóra állítunk
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				this.map[x][y] = TronMap.FREE;
			}
		}
		this.numObstacles = 0;
		this.visitedPoints = new ArrayList<Point>();
	}
	
	private void raiseCell(final int x, final int y, final int d){
		if(
			!mapRectangle.contains(x, y)	//kilóg a mapról a pont
			|| map[x][y] >= d				//magasabb a pont mint amire emelnénk
		){
			return;
		}
		
		if( map[x][y] == TronMap.FREE ){
			//most lesz elfoglalva
			numObstacles++;
		}
		
		map[x][y]++;
		
		
		//szomszédok max 1-el lehetnek alacsonyabban
		int minNeighborHeight = map[x][y]-1;
		
		raiseCell(x-1, y, minNeighborHeight);
		raiseCell(x+1, y, minNeighborHeight);
		raiseCell(x, y-1, minNeighborHeight);
		raiseCell(x, y+1, minNeighborHeight);
	}
	
	private boolean isValidMap(){
		try {
			Point p = getFirstEmpty();
			visitPoint(p.x, p.y);
			
			//minden nem akadály pont be lett-e járva
			return visitedPoints.size() == (width * height - numObstacles);
		} catch(Exception e){
			//nincs üres pont
			return false;
		}
	}
	
	private void checkDrop(){
		try{
			Point p = getFirstEmpty();
			visitPoint(p.x, p.y);
			handleLastDrop(visitedPoints.size() == (width * height - numObstacles)); //minden nem akadály pont be lett-e járva
		} catch(Exception e){
			handleLastDrop(false);
		}
	}
	
	private Point getFirstEmpty() throws Exception{
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				if(map[x][y] == TronMap.FREE){
					return new Point(x, y);
				}
			}
		}
		
		throw new Exception("Nincs üres pont");
	}
	
	private void visitPoint(final int x, final int y){
		Point p = new Point(x, y);
		
		if(
			!mapRectangle.contains(x, y)	//lelóg a mapról
			|| map[x][y] != TronMap.FREE	//nem üres pont
			|| visitedPoints.contains(p)	//már meglátogattuk
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
		int newMapValue = isValid ? TronMap.OBSTACLE : TronMap.FREE;
		
		for(Point tempObstacle : tempObstacles){
			map[tempObstacle.x][tempObstacle.y] = newMapValue;
		}
		
		if(!isValid){
			numObstacles -= tempObstacles.size();
		}
	}
	
	private boolean isUnderLimit(){
		return ((double)numObstacles / (width * height)) <= LIMIT; 
	}

}

package map;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class TronMapGenerator {
	
	private int width;
	private int height;
	private int[][] map;
	private Rectangle mapRectangle;
	
	private Random rnd = new Random();
	private ArrayList<Point> visitedPoints;
	private ArrayList<Point> tempObstacles;
	private int numObstacles;
	
	private static final double LIMIT = 0.05;
	
	private static final int TEMP_OBSTACLE = -2;

	public TronMapGenerator(final int width, final int height) {
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
	public int[][] generate(){
		//minden mezõt játszhatóra állítunk
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				this.map[x][y] = TronMap.FREE;
			}
		}
		
		do{
			this.visitedPoints = new ArrayList<Point>();
			this.tempObstacles = new ArrayList<Point>();
			
			this.generateObstacle(this.rnd.nextInt(width), this.rnd.nextInt(height));
			this.checkDrop();
//			System.out.printf("visited:%d sum:%d obstacles:%d isUnderLimit:%b\n", visitedPoints.size(), (width*height), obstacles, isUnderLimit());
//			printMap();
		} while(this.isUnderLimit());
		
		return this.map;
	}
	
	private void generateObstacle(final int x, final int y){
		if(
			rnd.nextDouble() < 0.6			//esély
			|| !mapRectangle.contains(x, y)	//kilóg a mapról a pont
			|| map[x][y] != TronMap.FREE	//már elfoglalt a pont
		){
			return;
		}
		
		//ideiglenesen elfoglaljuk
		map[x][y] = TEMP_OBSTACLE;
		tempObstacles.add(new Point(x, y));
		
		numObstacles++;
		
		//szomszédokra is meghívjuk
		generateObstacle(x-1, y);
		generateObstacle(x+1, y);
		generateObstacle(x, y-1);
		generateObstacle(x, y+1);
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

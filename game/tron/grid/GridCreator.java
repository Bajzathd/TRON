package game.tron.grid;

import game.tron.grid.element.GridElement;
import game.tron.grid.element.Obstacle;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GridCreator {
	
	private int width;
	private int height;
	private double obstacleRatio;
	
	private int[][] grid;
	private Rectangle mapRectangle;
	
	private Random random = new Random();
	private ArrayList<Point> visitedPoints;
	private int numObstacles;
	
	public GridCreator(int width, int height, double obstacleRatio) {
		this.width = width;
		this.height = height;
		this.obstacleRatio = obstacleRatio;
		
		grid = new int[height][width];
		mapRectangle = new Rectangle(width, height);
	}
	
	/**
	 * Legener�lja a p�ly�t
	 * 0: j�tszhat� p�lyar�sz
	 * <0: akad�ly
	 * >0: j�t�kos
	 */
	public Grid getGrid(){
		GridElement[][] elements = new GridElement[height][width];
		
		do {
			this.generateGrid();
		} while( !this.isValidGrid() );
		
		Obstacle newObstacle;
		List<Obstacle> obstacles = new ArrayList<Obstacle>();
		
		for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (grid[y][x] == 0) {
                	elements[y][x] = null;
                } else {
                	newObstacle = new Obstacle(x, y, grid[y][x]);
                	
                	obstacles.add(newObstacle);
                	elements[y][x] = newObstacle;
                }
            }
        }
		
		return new Grid(elements, obstacles);
	}
	
	private void generateGrid(){
		this.reset();
		
		do {
			this.raiseCell(
					this.random.nextInt(this.width), 
					this.random.nextInt(this.height), 
					Integer.MAX_VALUE
			);
			
		} while( this.isUnderLimit() );
	}
	
	private void reset(){
		//minden mez�t j�tszhat�ra �ll�tunk
		for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[y][x] = 0;
            }
        }
		this.numObstacles = 0;
		this.visitedPoints = new ArrayList<Point>();
	}
	
	private void raiseCell(int x, int y, int d){
		if(
			!mapRectangle.contains(x, y)	//kil�g a mapr�l a pont
			|| grid[y][x] >= d				//magasabb a pont mint amire emeln�nk
		){
			return;
		}
		
		if( grid[y][x] == 0 ){
			//most lesz elfoglalva
			numObstacles++;
		}
		
		grid[y][x]++;
		
		
		//szomsz�dok max 1-el lehetnek alacsonyabban
		int minNeighborHeight = grid[y][x] - 1;
		
		raiseCell(x-1, y, minNeighborHeight);
		raiseCell(x+1, y, minNeighborHeight);
		raiseCell(x, y-1, minNeighborHeight);
		raiseCell(x, y+1, minNeighborHeight);
	}
	
	private boolean isValidGrid(){
		try {
			Point p = getFirstEmpty();
			visitPoint(p.x, p.y);
			
			//minden nem akad�ly pont be lett-e j�rva
			return visitedPoints.size() == (width * height - numObstacles);
		} catch(Exception e){
			//nincs �res pont
			return false;
		}
	}
	
	private Point getFirstEmpty() throws Exception{
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				if(grid[y][x] == 0){
					return new Point(x, y);
				}
			}
		}
		
		throw new Exception("Nincs �res pont");
	}
	
	private void visitPoint(final int x, final int y){
		Point p = new Point(x, y);
		
		if(
			!mapRectangle.contains(x, y)	//lel�g a mapr�l
			|| grid[y][x] != 0				//nem �res pont
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
	
	private boolean isUnderLimit(){
		return ((double)numObstacles / (width * height)) <= obstacleRatio; 
	}
}

package game.tron.grid;

import game.tron.grid.element.GridElement;
import game.tron.grid.element.Obstacle;
import game.tron.utility.Direction;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.omg.CosNaming.NamingContextPackage.NotFound;

public class GridCreator {
	
	private int width;
	private int height;
	private double obstacleRatio;
	
	private int[][] grid;
	private Rectangle obstacleGridRectangle;
	
	private Random random = new Random();
	private ArrayList<Point> visitedPoints;
	private int numObstacles;
	
	public GridCreator(int width, int height, double obstacleRatio) {
		this.width = width - 2;
		this.height = height - 2;
		this.obstacleRatio = obstacleRatio;
		
		grid = new int[this.height][this.width];
		obstacleGridRectangle = new Rectangle(this.width, this.height);
	}
	
	public Grid getGrid() {
		GridElement[][] elements = new GridElement[height + 2][width + 2];
		
		do {
			generateGrid();
		} while (! isValidGrid());
		
		int x;
		int y;
		Obstacle newObstacle;
		List<Obstacle> obstacles = new ArrayList<Obstacle>();
		
		for (x = 0; x < width + 2; x++) {
            for (y = 0; y < height + 2; y++) {
            	elements[y][x] = null;
            }
		}
		for (x = 0; x < width; x++) {
            for (y = 0; y < height; y++) {
                if (grid[y][x] != 0) {
                	newObstacle = new Obstacle(x + 1, y + 1, grid[y][x]);
                	
                	obstacles.add(newObstacle);
                	elements[y + 1][x + 1] = newObstacle;
                }
            }
        }
		
		return new Grid(elements, obstacles);
	}
	
	private void generateGrid() {
		reset();
		
		do {
			raiseCell(random.nextInt(width),
					random.nextInt(height),
					Integer.MAX_VALUE);
		} while (isUnderLimit());
	}
	
	private void reset() {
		//minden mezõt játszhatóra állítunk
		for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[y][x] = 0;
            }
        }
		numObstacles = 0;
		visitedPoints = new ArrayList<Point>();
	}
	
	private void raiseCell(int x, int y, int d) {
		if (! obstacleGridRectangle.contains(x, y)	//kilóg a mapról a pont
				|| grid[y][x] >= d) {				//magasabb a pont mint amire emelnénk
			return;
		}
		
		if (grid[y][x] == 0) {
			//most lesz elfoglalva
			numObstacles++;
		}
		
		grid[y][x]++;
		
		//szomszédok max 1-el lehetnek alacsonyabban
		int minNeighborHeight = grid[y][x] - 1;
		
		raiseCell(x-1, y, minNeighborHeight);
		raiseCell(x+1, y, minNeighborHeight);
		raiseCell(x, y-1, minNeighborHeight);
		raiseCell(x, y+1, minNeighborHeight);
	}
	
	private boolean isValidGrid() {
		try {
			Point p = getFirstEmpty();
			visitPoint(p);
			
			//minden nem akadály pont be lett-e járva
			return visitedPoints.size() == (width * height - numObstacles);
		} catch(NotFound ex) {
			//nincs üres pont
			return false;
		}
	}
	
	private Point getFirstEmpty() throws NotFound {
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				if(grid[y][x] == 0){
					return new Point(x, y);
				}
			}
		}
		throw new NotFound();
	}
	
	private void visitPoint(Point p) {
		
		if (!obstacleGridRectangle.contains(p)		//lelóg a gridrõl
				|| grid[p.y][p.x] != 0				//nem üres pont
				|| visitedPoints.contains(p)) {		//már meglátogattuk
			return;
		}
		
		visitedPoints.add(p);
		
		for (Direction direction : Direction.values()) {
			visitPoint(direction.getTranslatedPoint(p));
		}
	}
	
	private boolean isUnderLimit() {
		return ((double)numObstacles / (width * height)) <= obstacleRatio; 
	}
}

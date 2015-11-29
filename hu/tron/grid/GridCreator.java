package hu.tron.grid;

import hu.tron.gridelement.GridElement;
import hu.tron.gridelement.Obstacle;
import hu.tron.utility.Direction;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.omg.CosNaming.NamingContextPackage.NotFound;

/**
 * Új játéktér generálását végzõ osztály
 * 
 * @author Dávid Bajzáth
 */
public class GridCreator {

	/**
	 * Játéktér akadályokkal nehezíthetõ részének szélessége
	 */
	private int width;
	/**
	 * Játéktér akadályokkal nehezíthetõ részének magassága 
	 */
	private int height;
	/**
	 * Akadályok minimális aránya
	 */
	private double obstacleRatio;
	/**
	 * Játéktér egyszerûsített reprezentációja
	 */
	private int[][] grid;
	/**
	 * Játéktér akadályokkal nehezíthetõ részének korlátja
	 */
	private Rectangle obstaclesBound;

	private Random random = new Random();
	private ArrayList<Point> visitedPoints;
	private int numObstacles;

	/**
	 * @param width
	 *            játéktár szélessége
	 * @param height
	 *            játéktér magassága
	 * @param obstacleRatio
	 *            akadályok minimális aránya
	 */
	public GridCreator(int width, int height, double obstacleRatio) {
		this.width = width - 2;
		this.height = height - 2;
		this.obstacleRatio = obstacleRatio;

		grid = new int[this.height][this.width];
		obstaclesBound = new Rectangle(this.width, this.height);
	}

	/**
	 * @return konstruktorban megadott feltételeknek megfelelõ {@link Grid}
	 */
	public Grid getGrid() {
		Obstacle newObstacle;
		List<Obstacle> obstacles = new ArrayList<Obstacle>();
		GridElement[][] elements = new GridElement[height + 2][width + 2];

		do {
			generateGrid();
		} while (!isValidGrid());

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (grid[y][x] != 0) {
					newObstacle = new Obstacle(x + 1, y + 1, grid[y][x]);

					obstacles.add(newObstacle);
					elements[y + 1][x + 1] = newObstacle;
				}
			}
		}

		return new Grid(elements, obstacles);
	}

	/**
	 * Generál egy a konstruktorban megadott feltételeknek megfelelõ játékteret
	 */
	private void generateGrid() {
		reset();

		do {
			raiseCell(new Point(random.nextInt(width), random.nextInt(height)),
					Integer.MAX_VALUE);
		} while (isUnderLimit());
	}

	/**
	 * Visszaállítja alapértékekre
	 */
	private void reset() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				grid[y][x] = 0;
			}
		}
		numObstacles = 0;
		visitedPoints = new ArrayList<Point>();
	}

	/**
	 * Megemel egy elemet, majd a szomszédait is ellenõrzi, hogy teljesül-e 
	 * hogy egy elem minden szomszédja maximum 1-el alacsonyabb.
	 * 
	 * @param p pont amit megemel
	 * @param h magasság amire emeljük
	 */
	private void raiseCell(Point p, int h) {
		
		/*
		 * ha kilóg a pont az akadállyokkal nehezíthetõ részrõl vagy már
		 * magasabb mint amire emelnénk, akkor megszakítjuk
		 */
		if (!obstaclesBound.contains(p) || grid[p.y][p.x] >= h) {
			return;
		}

		/*
		 * minden szomszédnak minimum olyan magasnak kell lennie mint a pont 
		 * jelenlegi magassága
		 */
		int minNeighborHeight = grid[p.y][p.x];
		
		/*
		 * Ha eddig 0 magasságú (padló) volt akkor ezen a ponton egy új
		 * akadály jön létre
		 */
		if (grid[p.y][p.x] == 0) {
			numObstacles++;
		}

		grid[p.y][p.x]++;

		for (Direction direction : Direction.values()) {
			raiseCell(direction.getTranslatedPoint(p), minNeighborHeight);
		}
	}

	/**
	 * Eldönti a generált játéktérrõl hogy érvényes-e. A játéktér akkor számít
	 * érvényesnek ha minden pontja elérhetõ.
	 * 
	 * @return érvényes-e a játéktér
	 */
	private boolean isValidGrid() {
		try {
			visitPoint(getFirstFloor());

			// minden nem akadály pont be lett-e járva
			return visitedPoints.size() == ((width * height) - numObstacles);
		} catch (NotFound ex) {
			return false; // nincs üres pont
		}
	}

	/**
	 * @return elsõ 0 magasságú (padló) pont
	 * @throws NotFound nincs ilyen pont
	 */
	private Point getFirstFloor() throws NotFound {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (grid[y][x] == 0) {
					return new Point(x, y);
				}
			}
		}
		throw new NotFound();
	}

	/**
	 * Játéktér pontjait rekurzívan bejáró metódus
	 * 
	 * @param p kezdõ pont
	 */
	private void visitPoint(Point p) {

		/*
		 * ha kilóg a pont az akadállyokkal nehezíthetõ részrõl vagy nem padló
		 * vagy már meglátogattuk akkor megszakítjuk a futását
		 */
		if (!obstaclesBound.contains(p) || grid[p.y][p.x] != 0
				|| visitedPoints.contains(p)) {
			return;
		}

		visitedPoints.add(p);

		for (Direction direction : Direction.values()) {
			visitPoint(direction.getTranslatedPoint(p));
		}
	}

	/**
	 * @return megfelel-e a pálya az akadály aránynak
	 */
	private boolean isUnderLimit() {
		return ((double) numObstacles / (width * height)) < obstacleRatio;
	}
}

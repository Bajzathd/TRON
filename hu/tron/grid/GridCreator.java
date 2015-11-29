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
 * �j j�t�kt�r gener�l�s�t v�gz� oszt�ly
 * 
 * @author D�vid Bajz�th
 */
public class GridCreator {

	/**
	 * J�t�kt�r akad�lyokkal nehez�thet� r�sz�nek sz�less�ge
	 */
	private int width;
	/**
	 * J�t�kt�r akad�lyokkal nehez�thet� r�sz�nek magass�ga 
	 */
	private int height;
	/**
	 * Akad�lyok minim�lis ar�nya
	 */
	private double obstacleRatio;
	/**
	 * J�t�kt�r egyszer�s�tett reprezent�ci�ja
	 */
	private int[][] grid;
	/**
	 * J�t�kt�r akad�lyokkal nehez�thet� r�sz�nek korl�tja
	 */
	private Rectangle obstaclesBound;

	private Random random = new Random();
	private ArrayList<Point> visitedPoints;
	private int numObstacles;

	/**
	 * @param width
	 *            j�t�kt�r sz�less�ge
	 * @param height
	 *            j�t�kt�r magass�ga
	 * @param obstacleRatio
	 *            akad�lyok minim�lis ar�nya
	 */
	public GridCreator(int width, int height, double obstacleRatio) {
		this.width = width - 2;
		this.height = height - 2;
		this.obstacleRatio = obstacleRatio;

		grid = new int[this.height][this.width];
		obstaclesBound = new Rectangle(this.width, this.height);
	}

	/**
	 * @return konstruktorban megadott felt�teleknek megfelel� {@link Grid}
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
	 * Gener�l egy a konstruktorban megadott felt�teleknek megfelel� j�t�kteret
	 */
	private void generateGrid() {
		reset();

		do {
			raiseCell(new Point(random.nextInt(width), random.nextInt(height)),
					Integer.MAX_VALUE);
		} while (isUnderLimit());
	}

	/**
	 * Vissza�ll�tja alap�rt�kekre
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
	 * Megemel egy elemet, majd a szomsz�dait is ellen�rzi, hogy teljes�l-e 
	 * hogy egy elem minden szomsz�dja maximum 1-el alacsonyabb.
	 * 
	 * @param p pont amit megemel
	 * @param h magass�g amire emelj�k
	 */
	private void raiseCell(Point p, int h) {
		
		/*
		 * ha kil�g a pont az akad�llyokkal nehez�thet� r�szr�l vagy m�r
		 * magasabb mint amire emeln�nk, akkor megszak�tjuk
		 */
		if (!obstaclesBound.contains(p) || grid[p.y][p.x] >= h) {
			return;
		}

		/*
		 * minden szomsz�dnak minimum olyan magasnak kell lennie mint a pont 
		 * jelenlegi magass�ga
		 */
		int minNeighborHeight = grid[p.y][p.x];
		
		/*
		 * Ha eddig 0 magass�g� (padl�) volt akkor ezen a ponton egy �j
		 * akad�ly j�n l�tre
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
	 * Eld�nti a gener�lt j�t�kt�rr�l hogy �rv�nyes-e. A j�t�kt�r akkor sz�m�t
	 * �rv�nyesnek ha minden pontja el�rhet�.
	 * 
	 * @return �rv�nyes-e a j�t�kt�r
	 */
	private boolean isValidGrid() {
		try {
			visitPoint(getFirstFloor());

			// minden nem akad�ly pont be lett-e j�rva
			return visitedPoints.size() == ((width * height) - numObstacles);
		} catch (NotFound ex) {
			return false; // nincs �res pont
		}
	}

	/**
	 * @return els� 0 magass�g� (padl�) pont
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
	 * J�t�kt�r pontjait rekurz�van bej�r� met�dus
	 * 
	 * @param p kezd� pont
	 */
	private void visitPoint(Point p) {

		/*
		 * ha kil�g a pont az akad�llyokkal nehez�thet� r�szr�l vagy nem padl�
		 * vagy m�r megl�togattuk akkor megszak�tjuk a fut�s�t
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
	 * @return megfelel-e a p�lya az akad�ly ar�nynak
	 */
	private boolean isUnderLimit() {
		return ((double) numObstacles / (width * height)) < obstacleRatio;
	}
}

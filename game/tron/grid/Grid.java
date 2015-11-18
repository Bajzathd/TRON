package game.tron.grid;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.omg.CosNaming.NamingContextPackage.NotFound;

import game.tron.client.Client;
import game.tron.grid.element.GridElement;
import game.tron.grid.element.Obstacle;
import game.tron.grid.element.Trail;
import game.tron.utility.Direction;

public class Grid {

	private Rectangle gridRectangle;

	private GridElement[][] elements;
	private int numFloors;

	private Client client1 = null;
	private Client client2 = null;
	private List<Client> aliveClients = new ArrayList<Client>();

	private List<Obstacle> obstacles;
	private List<Trail> trails = new ArrayList<Trail>();

	public Grid(GridElement[][] elements, List<Obstacle> obstacles) {
		this.elements = elements;
		this.obstacles = obstacles;
		numFloors = elements[0].length * elements.length - obstacles.size();

		gridRectangle = new Rectangle(elements[0].length, elements.length);
	}

	public Grid clone() {
		GridElement[][] elements = new GridElement[this.elements[0].length][];
		for (int index = 0; index < this.elements.length; index++) {
			elements[index] = Arrays.copyOf(this.elements[index],
					this.elements[index].length);
		}
		List<Obstacle> obstacles = new ArrayList<Obstacle>(this.obstacles);

		Grid clone = new Grid(elements, obstacles);

		clone.client1 = client1.clone();
		clone.client2 = client2.clone();
		clone.trails = new ArrayList<Trail>(trails);

		return clone;
	}

	public void setClient1(Client client) {
		client1 = client;
	}

	public void setClient2(Client client) {
		client2 = client;
	}

	public void start() {
		if (client1 == null || client2 == null) {
			System.out.println("Can't start a game with empty clients");
			System.exit(1);
		}

		client1.setStart(new Point(0, 0), Direction.RIGHT);
		client2.setStart(new Point(gridRectangle.width - 1,
				gridRectangle.height - 1), Direction.LEFT);

		aliveClients.add(client1);
		aliveClients.add(client2);
	}

	public void killClient(Client client) {
		aliveClients.remove(client);
		client.kill();
	}

	public void addTrail(Trail trail) {
		trails.add(trail);
		elements[trail.getY()][trail.getX()] = trail;
		numFloors--;
	}

	public int getWidth() {
		return gridRectangle.width;
	}

	public int getHeight() {
		return gridRectangle.height;
	}

	public Client getClient1() {
		return client1;
	}

	public Client getClient2() {
		return client2;
	}

	public List<Client> getAliveClients() {
		return aliveClients;
	}

	public Object[][] getElements() {
		return elements;
	}

	public List<Obstacle> getObstacles() {
		return obstacles;
	}

	public List<Trail> getTrails() {
		return trails;
	}

	public List<Direction> getValidDirections(Point p) throws NotFound {
		List<Direction> validDirections = new ArrayList<Direction>();

		for (Direction direction : Direction.getAllShuffled()) {
			if (isFloor(direction.getTranslatedPoint(p))) {
				validDirections.add(direction);
			}
		}

		if (validDirections.isEmpty()) {
			throw new NotFound();
		}

		return validDirections;
	}

	public GridElement getElement(Point p) {
		if (gridRectangle.contains(p)) {
			return elements[p.y][p.x];
		}
		return new Obstacle(p, 1);
	}

	public boolean isFloor(Point p) {
		return getElement(p) == null;
	}

	public int getNumFloors() {
		return numFloors;
	}

	public List<Point> getAccessableFloors(Point startPoint) {
		List<Point> visitedPoints = new ArrayList<Point>();

		visitPoint(startPoint, visitedPoints);

		return visitedPoints;
	}

	private void visitPoint(Point p, List<Point> visitedPoints) {

		if (!gridRectangle.contains(p) // lelóg a gridrõl
				|| !isFloor(p) // nem üres pont
				|| visitedPoints.contains(p)) { // már meglátogattuk
			return;
		}

		visitedPoints.add(p);

		for (Direction direction : Direction.values()) {
			visitPoint(direction.getTranslatedPoint(p), visitedPoints);
		}
	}

}

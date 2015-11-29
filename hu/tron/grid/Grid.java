package hu.tron.grid;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.omg.CosNaming.NamingContextPackage.NotFound;

import hu.tron.client.Client;
import hu.tron.gridelement.GridElement;
import hu.tron.gridelement.Obstacle;
import hu.tron.gridelement.TieCrash;
import hu.tron.gridelement.Trail;
import hu.tron.utility.Direction;

/**
 * Játékteret reprezentáló osztály
 * 
 * @author Dávid Bajzáth
 */
public class Grid {

	/**
	 * Határoló négyszög
	 */
	private Rectangle gridRectangle;
	/**
	 * Játéktér elemei
	 */
	private GridElement[][] elements;
	/**
	 * Padlók (szabad területek) száma
	 */
	private int numFloors;
	/**
	 * Elsõ kliens
	 */
	private Client client1 = null;
	/**
	 * Második kliens
	 */
	private Client client2 = null;
	/**
	 * Még játékban lévõ kliensek
	 */
	private List<Client> aliveClients = new ArrayList<Client>();
	/**
	 * Akadályok listája
	 */
	private List<Obstacle> obstacles;
	/**
	 * Kliensek után húzott csíkok listája
	 */
	private List<Trail> trails = new ArrayList<Trail>();
	/**
	 * Döntetlen különleges esete ({@link TieCrash})
	 */
	private TieCrash tieCrash = null;

	/**
	 * Új játéktér létrehozása
	 * 
	 * @param elements
	 *            mezõk értékei
	 * @param obstacles
	 *            akadályok
	 */
	public Grid(GridElement[][] elements, List<Obstacle> obstacles) {
		this.elements = elements;
		this.obstacles = obstacles;
		
		gridRectangle = new Rectangle(elements[0].length, elements.length);
		numFloors = (gridRectangle.width * gridRectangle.height) - 
				obstacles.size();
	}

	@Override
	public Grid clone() {
		GridElement[][] elements = new GridElement[this.elements.length][];
		for (int index = 0; index < this.elements.length; index++) {
			elements[index] = Arrays.copyOf(this.elements[index],
					this.elements[index].length);
		}

		List<Obstacle> obstacles = new ArrayList<Obstacle>();
		for (Obstacle obstacle : this.obstacles) {
			obstacles.add(obstacle);
		}

		Grid clone = new Grid(elements, obstacles);

		clone.gridRectangle = (Rectangle) gridRectangle.clone();
		clone.numFloors = numFloors;
		clone.client1 = client1.clone();
		clone.client2 = client2.clone();

		clone.aliveClients = new ArrayList<Client>();
		for (Client aliveClient : aliveClients) {
			clone.aliveClients.add(aliveClient.clone());
		}

		clone.trails = new ArrayList<Trail>(trails);

		return clone;
	}

	/**
	 * Új játék indítása
	 * 
	 * @throws Exception nem lett beállítva minden kliens
	 */
	public void start() throws Exception {
		if (client1 == null || client2 == null) {
			throw new Exception("Can't start a game with empty clients");
		}

		client1.setStart(0, 0, Direction.RIGHT);
		addTrail(client1.getTrail());
		aliveClients.add(client1);

		client2.setStart(gridRectangle.width - 1, gridRectangle.height - 1,
				Direction.LEFT);
		addTrail(client2.getTrail());
		aliveClients.add(client2);
	}

	/**
	 * Kliens játékának végét állítja be
	 * 
	 * @param client
	 *            kliens
	 */
	public void killClient(Client client) {
		aliveClients.remove(client);
		client.kill();
	}

	/**
	 * Új csík elemet ad a pályához
	 * 
	 * @param trail
	 *            csík elem
	 */
	public void addTrail(Trail trail) {
		for (Trail otherTrail : trails) {
			if (trail.getClient().getId() == otherTrail.getClient().getId()) {
				
				// az új csík elem lesz a kliensének a feje
				otherTrail.isHead = false;
			}
		}

		trails.add(trail);
		elements[trail.getY()][trail.getX()] = trail;
		numFloors--;
	}

	/**
	 * Adott pontból megadja milyen irányokban találunk padlót
	 * 
	 * @param p
	 *            pont
	 * @return irányok listája
	 * @throws NotFound
	 *             nincs padló szomszédja a pontnak
	 */
	public List<Direction> getValidDirections(Point p) throws NotFound {
		List<Direction> validDirections = new ArrayList<Direction>();

		for (Direction direction : Direction.values()) {
			if (isFloor(direction.getTranslatedPoint(p))) {
				validDirections.add(direction);
			}
		}

		if (validDirections.isEmpty()) {
			throw new NotFound();
		}

		return validDirections;
	}

	/**
	 * Visszaadja a ponton lévõ elemet. Ha a pont játéktéren kívül esik akkor
	 * akadályt ad vissza
	 * 
	 * @param p
	 *            pont
	 * @return elem
	 */
	public GridElement getElement(Point p) {
		return (gridRectangle.contains(p)) 
				? elements[p.y][p.x] 
				: new Obstacle(p, 1);
	}

	/**
	 * Megkeresi a kliens pozíciójából elérhetõ pontokat
	 * 
	 * @param client
	 *            kliens
	 * @return elérhetõ pontok
	 */
	public List<Point> getAccessableFloors(Client client) {
		List<Point> visitedPoints = new ArrayList<Point>();

		for (Direction direction : Direction.values()) {
			visitPoint(direction.getTranslatedPoint(client.getPosition()),
					visitedPoints);
		}

		return visitedPoints;
	}

	/**
	 * Rekurzívan bejárja az adott pontot és szomszédait ha még nem járt rajta
	 * 
	 * @param p
	 *            pont
	 * @param visitedPoints
	 *            bejárt pontok
	 */
	private void visitPoint(Point p, List<Point> visitedPoints) {
		if (!gridRectangle.contains(p) || !isFloor(p)
				|| visitedPoints.contains(p)) {
			return;
		}

		visitedPoints.add(p);

		for (Direction direction : Direction.values()) {
			visitPoint(direction.getTranslatedPoint(p), visitedPoints);
		}
	}

	public void setTieCrash(Point position) {
		tieCrash = new TieCrash(position);
		elements[position.y][position.x] = tieCrash;
	}

	public void setClient1(Client client) {
		client1 = client;
	}

	public void setClient2(Client client) {
		client2 = client;
	}

	public Client getClient(int clientId) {
		return (client1.getId() == clientId) ? client1 : client2;
	}

	public Client getEnemy(int clientId) {
		return (client1.getId() != clientId) ? client1 : client2;
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

	public TieCrash getTieCrash() {
		return tieCrash;
	}

	public boolean isFloor(Point p) {
		return getElement(p) == null;
	}

	public int getNumFloors() {
		return numFloors;
	}

	@Override
	public String toString() {
		String grid = "";

		for (int y = -1; y <= gridRectangle.height; y++) {
			for (int x = -1; x <= gridRectangle.width; x++) {
				GridElement element = getElement(new Point(x, y));
				if (element == null) {
					grid += " ";
				} else {
					grid += element.toString();
				}
			}
			grid += "\n";
		}

		return grid;
	}

}

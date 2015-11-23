package hu.tron.client;

import hu.tron.gridelement.Trail;
import hu.tron.utility.Direction;

import java.awt.Point;

/**
 * Kliens típusok õsosztálya
 * 
 * @author Dávid Bajzáth
 */
public abstract class Client {

	/**
	 * Kliens azonosító
	 */
	protected int id;
	/**
	 * Utolsó lépés haladási iránya
	 */
	protected Direction lastDirection;
	/**
	 * Következõ lépés haladási iránya
	 */
	protected Direction nextDirection;
	/**
	 * Életben van-e
	 */
	protected boolean alive;
	/**
	 * Pozíció
	 */
	protected Point position = new Point();
	
	public Client(int id) {
		this.id = id;
	}

	@Override
	public abstract Client clone();

	/**
	 * Beállítja a klient elindításához szükséges adatokat
	 * 
	 * @param x
	 *            pozíció az x tengelyen
	 * @param y
	 *            pozíció az y tengeylen
	 * @param startDirection
	 *            irány
	 */
	public void setStart(int x, int y, Direction startDirection) {
		alive = true;

		position.x = x;
		position.y = y;
		lastDirection = startDirection;
		nextDirection = startDirection;
	}

	/**
	 * Beállítja a klient elindításához szükséges adatokat
	 * 
	 * @param position
	 *            pozíció
	 * @param startDirection
	 *            irány
	 */
	public void setStart(Point position, Direction startDirection) {
		alive = true;

		this.position = position;
		lastDirection = startDirection;
		nextDirection = startDirection;
	}

	/**
	 * Megpróbálja beállítani az irányt
	 * 
	 * @param direction
	 *            irány
	 */
	public void trySetDirection(Direction direction) {
		if (direction != null && !direction.isOpposite(this.lastDirection)) {
			
			// Csak akkor állítjuk be az irányt ha nem üres és nem ellentétes
			this.nextDirection = direction;
		}
	}
	
	/**
	 * Beállított irányba lépés
	 */
	public void step() {
		position = nextDirection.getTranslatedPoint(position);
		lastDirection = nextDirection;
	}
	
	public void kill() {
		alive = false;
	}
	
	public boolean isAlive() {
		return alive;
	}

	public Direction getDirection() {
		return nextDirection;
	}

	public int getId() {
		return id;
	}

	public int getX() {
		return position.x;
	}

	public int getY() {
		return position.y;
	}

	public Point getPosition() {
		return position;
	}

	public Trail getTrail() {
		return new Trail((Point) position.clone(), clone());
	}

}

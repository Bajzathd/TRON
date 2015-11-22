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
	 * Haladási irány
	 */
	protected Direction direction;
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
		direction = startDirection;
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
		direction = startDirection;
	}

	/**
	 * Megpróbálja beállítani az irányt
	 * 
	 * @param direction
	 *            irány
	 */
	public void trySetDirection(Direction direction) {
		if (direction != null && !direction.isOpposite(this.direction)) {
			
			// Csak akkor állítjuk be az irányt ha nem üres és nem ellentétes
			this.direction = direction;
		}
	}
	
	/**
	 * Beállított irányba lépés
	 */
	public void step() {
		position = direction.getTranslatedPoint(position);
	}
	
	public void kill() {
		alive = false;
	}
	
	public boolean isAlive() {
		return alive;
	}

	public Direction getDirection() {
		return direction;
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

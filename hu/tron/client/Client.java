package hu.tron.client;

import hu.tron.gridelement.Trail;
import hu.tron.utility.Direction;

import java.awt.Point;

/**
 * Kliens t�pusok �soszt�lya
 * 
 * @author D�vid Bajz�th
 */
public abstract class Client {

	/**
	 * Kliens azonos�t�
	 */
	protected int id;
	/**
	 * Halad�si ir�ny
	 */
	protected Direction direction;
	/**
	 * �letben van-e
	 */
	protected boolean alive;
	/**
	 * Poz�ci�
	 */
	protected Point position = new Point();
	
	public Client(int id) {
		this.id = id;
	}

	@Override
	public abstract Client clone();

	/**
	 * Be�ll�tja a klient elind�t�s�hoz sz�ks�ges adatokat
	 * 
	 * @param x
	 *            poz�ci� az x tengelyen
	 * @param y
	 *            poz�ci� az y tengeylen
	 * @param startDirection
	 *            ir�ny
	 */
	public void setStart(int x, int y, Direction startDirection) {
		alive = true;

		position.x = x;
		position.y = y;
		direction = startDirection;
	}

	/**
	 * Be�ll�tja a klient elind�t�s�hoz sz�ks�ges adatokat
	 * 
	 * @param position
	 *            poz�ci�
	 * @param startDirection
	 *            ir�ny
	 */
	public void setStart(Point position, Direction startDirection) {
		alive = true;

		this.position = position;
		direction = startDirection;
	}

	/**
	 * Megpr�b�lja be�ll�tani az ir�nyt
	 * 
	 * @param direction
	 *            ir�ny
	 */
	public void trySetDirection(Direction direction) {
		if (direction != null && !direction.isOpposite(this.direction)) {
			
			// Csak akkor �ll�tjuk be az ir�nyt ha nem �res �s nem ellent�tes
			this.direction = direction;
		}
	}
	
	/**
	 * Be�ll�tott ir�nyba l�p�s
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

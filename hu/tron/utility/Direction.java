package hu.tron.utility;

import java.awt.Point;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * J�t�kt�ren v�laszthat� ir�nyok reprezent�ci�ja
 * 
 * @author D�vid Bajz�th
 */
public enum Direction {
	UP, RIGHT, DOWN, LEFT;

	private static Random random = new Random();

	/**
	 * @return random ir�ny
	 */
	public static Direction getRandom() {
		return Direction.values()[random.nextInt(Direction.values().length)];
	}

	/**
	 * @return ir�nyok list�ja random sorrendben
	 */
	public static List<Direction> getAllShuffled() {
		List<Direction> directions = Arrays.asList(Direction.values());
		Collections.shuffle(directions);
		return directions;
	}

	/**
	 * @param d
	 *            ir�ny
	 * @return ellent�tes-e a megadott ir�nnyal
	 */
	public boolean isOpposite(Direction d) {
		return this.ordinal() != d.ordinal()
				&& (this.ordinal() + d.ordinal()) % 2 == 0;
	}

	/**
	 * @param p
	 *            pont
	 * @return jelenlegi ir�nyba eltolt pont
	 */
	public Point getTranslatedPoint(Point p) {
		Point clone = new Point(p);

		switch (this) {
		case UP:
			clone.translate(0, -1);
			break;
		case RIGHT:
			clone.translate(1, 0);
			break;
		case DOWN:
			clone.translate(0, 1);
			break;
		case LEFT:
			clone.translate(-1, 0);
			break;
		}

		return clone;
	}
}

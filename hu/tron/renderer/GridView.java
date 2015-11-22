package hu.tron.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import hu.tron.grid.Grid;
import hu.tron.gridelement.Obstacle;
import hu.tron.gridelement.TieCrash;
import hu.tron.gridelement.Trail;

/**
 * Játékteret grafikusan reprezentáló osztály
 * 
 * @author Dávid Bajzáth
 */
public class GridView extends JPanel {

	/**
	 * Játéktér
	 */
	private Grid grid;
	/**
	 * Egy elem mérete (szélesség és magasság) pixelben
	 */
	private int elementSize;

	/**
	 * Kirajzolja a játéktér kezdõ állását
	 * 
	 * @param grid
	 *            játéktér
	 * @param elementSize
	 *            elemek mérete pixelben
	 */
	public GridView(Grid grid, int elementSize) {
		this.grid = grid;
		this.elementSize = elementSize;

		initUI();
	}

	/**
	 * Inicializálja a grafikus megjelenítést
	 */
	private void initUI() {
		setPreferredSize(new Dimension(
				grid.getWidth() * elementSize, grid.getHeight() * elementSize));

		setBackground(Color.BLACK);
		setVisible(true);
		grabFocus();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		int x;
		int y;

		// háttér rács
		g.setColor(Color.DARK_GRAY);
		for (x = 0; x <= grid.getWidth(); x += 5) {
			g.drawLine(x * elementSize, 0,
					x * elementSize, grid.getHeight() * elementSize);
		}
		for (y = 0; y <= grid.getHeight(); y += 5) {
			g.drawLine(0, y * elementSize,
					grid.getWidth() * elementSize, y * elementSize);
		}

		// akadályok
		for (Obstacle obstacle : grid.getObstacles()) {
			x = obstacle.getX() * elementSize;
			y = obstacle.getY() * elementSize;
			int height = obstacle.getHeight() + 1;

			g.setColor(new Color(255 / height, 255 / height, 255 / height));
			g.fillRect(x, y, elementSize, elementSize);
		}

		// kliens csíkok
		for (Trail trail : grid.getTrails()) {
			x = trail.getX() * elementSize;
			y = trail.getY() * elementSize;

			Color trailColor = SwingRenderer
					.getColor(trail.getClient().getId());

			if (trail.isHead) {
				g.setColor(trailColor.brighter());
			} else {
				g.setColor(trailColor);
			}

			g.fillRect(x, y, elementSize, elementSize);
		}

		// döntetlen
		TieCrash tieCrash = grid.getTieCrash();
		if (tieCrash != null) {
			x = tieCrash.getX() * elementSize;
			y = tieCrash.getY() * elementSize;

			int ceil = (int) Math.ceil(elementSize / 2.0);
			int floor = (int) Math.floor(elementSize / 2.0);

			g.setColor(SwingRenderer.getColor(1).brighter());
			g.fillRect(x, y, elementSize, elementSize);

			g.setColor(SwingRenderer.getColor(2).brighter());
			g.fillRect(x + floor, y, ceil, ceil);
			g.fillRect(x, y + ceil, floor, floor);
		}

	}

}

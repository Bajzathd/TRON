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
 * J�t�kteret grafikusan reprezent�l� oszt�ly
 * 
 * @author D�vid Bajz�th
 */
public class GridView extends JPanel {

	/**
	 * J�t�kt�r
	 */
	private Grid grid;
	/**
	 * Egy elem m�rete (sz�less�g �s magass�g) pixelben
	 */
	private int elementSize;

	/**
	 * Kirajzolja a j�t�kt�r kezd� �ll�s�t
	 * 
	 * @param grid
	 *            j�t�kt�r
	 * @param elementSize
	 *            elemek m�rete pixelben
	 */
	public GridView(Grid grid, int elementSize) {
		this.grid = grid;
		this.elementSize = elementSize;

		initUI();
	}

	/**
	 * Inicializ�lja a grafikus megjelen�t�st
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
		int x;
		int y;
		
		super.paint(g);

		// h�tt�r r�cs
		g.setColor(Color.DARK_GRAY);
		for (x = 0; x <= grid.getWidth(); x += 5) {
			g.drawLine(x * elementSize, 0,
					x * elementSize, grid.getHeight() * elementSize);
		}
		for (y = 0; y <= grid.getHeight(); y += 5) {
			g.drawLine(0, y * elementSize,
					grid.getWidth() * elementSize, y * elementSize);
		}

		// akad�lyok
		for (Obstacle obstacle : grid.getObstacles()) {
			int height = obstacle.getHeight() + 1;
			x = obstacle.getX() * elementSize;
			y = obstacle.getY() * elementSize;

			g.setColor(new Color(255 / height, 255 / height, 255 / height));
			g.fillRect(x, y, elementSize, elementSize);
		}

		// kliens cs�kok
		for (Trail trail : grid.getTrails()) {
			Color trailColor = SwingRenderer
					.getColor(trail.getClient().getId());
			x = trail.getX() * elementSize;
			y = trail.getY() * elementSize;

			if (trail.isHead) {
				g.setColor(trailColor.brighter());
			} else {
				g.setColor(trailColor);
			}

			g.fillRect(x, y, elementSize, elementSize);
		}

		// d�ntetlen
		TieCrash tieCrash = grid.getTieCrash();
		if (tieCrash != null) {
			int ceil = (int) Math.ceil(elementSize / 2.0);
			int floor = (int) Math.floor(elementSize / 2.0);

			x = tieCrash.getX() * elementSize;
			y = tieCrash.getY() * elementSize;
			
			g.setColor(SwingRenderer.getColor(1).brighter());
			g.fillRect(x, y, elementSize, elementSize);

			g.setColor(SwingRenderer.getColor(2).brighter());
			g.fillRect(x + floor, y, ceil, ceil);
			g.fillRect(x, y + ceil, floor, floor);
		}

	}

}

package game.tron.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import game.tron.grid.Grid;
import game.tron.gridelement.Obstacle;
import game.tron.gridelement.Trail;

public class GridView extends JPanel {
	
	private static final long serialVersionUID = 2646266332355992797L;

	private Grid grid;
	private int elementSize;
	
	public GridView(Grid grid, int elementSize) {
		this.grid = grid;
		this.elementSize = elementSize;
		
		initUI();
	}
	
	private void initUI() {
		setPreferredSize(new Dimension(
			grid.getWidth() * elementSize,
			grid.getHeight() * elementSize
		));
		
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
			g.drawLine(x * elementSize, 0, x * elementSize, grid.getHeight() * elementSize);
//			g.drawLine(0, x * elementSize, grid.getWidth() * elementSize, x * elementSize);
		}
		for (y = 0; y <= grid.getHeight(); y += 5) {
			g.drawLine(0, y * elementSize, grid.getWidth() * elementSize, y * elementSize);
		}
        
        // akadályok
        for (Obstacle obstacle : grid.getObstacles()) {
        	x = obstacle.getX() * elementSize;
        	y = obstacle.getY() * elementSize;
        	int height = obstacle.getHeight() + 1;
        	
        	g.setColor(new Color(255 / height, 255 / height, 255 / height));
        	g.fillRect(x, y , elementSize, elementSize);
        }
        
        // kliens csíkok
        for (Trail trail : grid.getTrails()) {
        	x = trail.getX() * elementSize;
        	y = trail.getY() * elementSize;
        	
        	Color trailColor = SwingRenderer.getColor(trail.getClient().getId());
        	
        	if (trail.isHead) {
        		g.setColor(trailColor.brighter());
        	} else {
        		g.setColor(trailColor);
        	}
        	
        	g.fillRect(x, y , elementSize, elementSize);
        }
        
	}

}

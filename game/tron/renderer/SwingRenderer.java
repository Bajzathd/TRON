package game.tron.renderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;

import game.tron.client.Client;
import game.tron.grid.Grid;

import javax.swing.JFrame;

public class SwingRenderer extends JFrame implements Renderer {

	private static final long serialVersionUID = 3223398761623593996L;
	
	/**
	 * Element-ek mérete pixelben
	 */
	public static final int ELEMENT_SIZE = 8;
	/**
	 * Kliensek színei
	 */
	private static Color[] colors = {
		new Color(0x11a1ee),
		new Color(0xf79b08)
	};
	
	private GridView gridView;
	private InfoView infoView;
	
	public SwingRenderer(Grid grid) {
		this.gridView = new GridView(grid, ELEMENT_SIZE);
		this.infoView = new InfoView();
		initUI();
	}
	
	private void initUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Tron");
		setLayout(new BorderLayout());
		
		add(gridView, BorderLayout.CENTER);
		add(infoView, BorderLayout.PAGE_END);
		pack();
		
		setFocusable(true);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	@Override
	public void render() {
		gridView.repaint();
	}
	
	@Override
	public void showResults(List<Client> aliveClients) {
		infoView.showResults(aliveClients);
	}

	public static Color getColor(int clientId) {
		return colors[clientId - 1];
	}

}

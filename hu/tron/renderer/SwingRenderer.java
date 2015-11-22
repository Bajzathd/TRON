package hu.tron.renderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;

import hu.tron.client.Client;
import hu.tron.grid.Grid;

import javax.swing.JFrame;

/**
 * Rendere megvalósítása Java Swing-el.
 * 
 * @author Dávid Bajzáth
 */
public class SwingRenderer extends JFrame implements Renderer {

	/**
	 * Elemek mérete pixelben (szélesség és magasság)
	 */
	public static final int ELEMENT_SIZE = 20;
	/**
	 * Kliensek színei
	 */
	private static Color[] colors = { new Color(0x11a1ee), new Color(0xf79b08) };

	/**
	 * Játéktér megjelenítése
	 */
	private GridView gridView;
	/**
	 * Játéktér információinak megjelenítése
	 */
	private InfoView infoView;

	public SwingRenderer(Grid grid) {
		this.gridView = new GridView(grid, ELEMENT_SIZE);
		this.infoView = new InfoView();
		initUI();
	}

	/**
	 * Inicializálja a megjelenítést
	 */
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

	/**
	 * @param clientId
	 *            kliens id-ja
	 * @return kliens színe
	 */
	public static Color getColor(int clientId) {
		return colors[clientId - 1];
	}

}

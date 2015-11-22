package hu.tron.renderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;

import hu.tron.client.Client;
import hu.tron.grid.Grid;

import javax.swing.JFrame;

/**
 * Rendere megval�s�t�sa Java Swing-el.
 * 
 * @author D�vid Bajz�th
 */
public class SwingRenderer extends JFrame implements Renderer {

	/**
	 * Elemek m�rete pixelben (sz�less�g �s magass�g)
	 */
	public static final int ELEMENT_SIZE = 20;
	/**
	 * Kliensek sz�nei
	 */
	private static Color[] colors = { new Color(0x11a1ee), new Color(0xf79b08) };

	/**
	 * J�t�kt�r megjelen�t�se
	 */
	private GridView gridView;
	/**
	 * J�t�kt�r inform�ci�inak megjelen�t�se
	 */
	private InfoView infoView;

	public SwingRenderer(Grid grid) {
		this.gridView = new GridView(grid, ELEMENT_SIZE);
		this.infoView = new InfoView();
		initUI();
	}

	/**
	 * Inicializ�lja a megjelen�t�st
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
	 * @return kliens sz�ne
	 */
	public static Color getColor(int clientId) {
		return colors[clientId - 1];
	}

}

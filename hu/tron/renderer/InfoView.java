package hu.tron.renderer;

import java.awt.Color;
import java.util.List;

import hu.tron.client.Client;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * A j�t�kt�r inform�ci�it megjelen�t� label
 * 
 * @author D�vid Bajz�th
 */
public class InfoView extends JLabel {

	public InfoView() {
		super("Game started", SwingConstants.CENTER);
		setOpaque(true);
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);
		setVisible(true);
	}

	/**
	 * A j�t�k v�g�n az eredm�nyek megjelen�t�se
	 * 
	 * @param aliveClients
	 *            �letben maradt kliensek list�ja
	 */
	public void showResults(List<Client> aliveClients) {
		if (aliveClients.size() == 0) {
			setText("Tie");
		} else {
			Client winner = aliveClients.get(0);
			setForeground(SwingRenderer.getColor(winner.getId()));
			setText(winner + " won");
		}
	}

}

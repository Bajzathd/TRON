package hu.tron.renderer;

import java.awt.Color;
import java.util.List;

import hu.tron.client.Client;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * A játéktér információit megjelenítõ label
 * 
 * @author Dávid Bajzáth
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
	 * A játék végén az eredmények megjelenítése
	 * 
	 * @param aliveClients
	 *            életben maradt kliensek listája
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

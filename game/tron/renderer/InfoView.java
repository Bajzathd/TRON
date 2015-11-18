package game.tron.renderer;

import java.awt.Color;
import java.util.List;

import game.tron.client.Client;
import game.tron.client.ClientController;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class InfoView extends JLabel {

	private static final long serialVersionUID = -4807488365748387597L;
	
	public InfoView() {
		super("Game started", SwingConstants.CENTER);
		setOpaque(true);
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);
		setVisible(true);
	}
	
	public void showResults(List<ClientController> aliveClientControllers) {
		if (aliveClientControllers.size() == 0) {
			setText("Tie");
		} else {
			Client winner = aliveClientControllers.get(0).getClient();
			setForeground(SwingRenderer.getColor(winner.getId()));
			setText(winner+" won");
		}
	}

}

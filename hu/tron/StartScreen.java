package hu.tron;

import hu.tron.client.Client;
import hu.tron.client.ai.AIController;
import hu.tron.client.player.PlayerController;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Kezdõ képernyõt megvalósító osztály
 * 
 * @author Dávid Bajzáth
 */
public class StartScreen extends JFrame 
		implements ItemListener, ActionListener {

	/**
	 * Grid szélessége
	 */
	private static final int WIDTH = 30;
	/**
	 * Grid magassága
	 */
	private static final int HEIGHT = 20;
	/**
	 * Maximális akadály arány
	 */
	private static final double OBSTACLE_RATIO = 0.05;
	/**
	 * Szimuláció és játék közül választó checkbox
	 */
	private JCheckBox isSimulation = new JCheckBox("Simulation");
	/**
	 * Testkörök beállítását tároló panel
	 */
	private SimulationRoundsPanel roundsPanel;
	/**
	 * Elsõ kliens beállításai tartalmazó panel
	 */
	private ClientSettings client1 = new ClientSettings(1);
	/**
	 * Második kliens beállításait tartalmazó panel
	 */
	private ClientSettings client2 = new ClientSettings(2);
	/**
	 * Beállítások véglegesítésére szolgáló gomb
	 */
	private JButton submit = new JButton("Start");

	public StartScreen() {
		initUI();
	}

	/**
	 * Kezelõ felület inicializálása
	 */
	private void initUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Tron settings");
		setLayout(new BorderLayout(10, 10));

		JPanel testPanel = new JPanel();
		testPanel.add(isSimulation);
		testPanel.add(roundsPanel = new SimulationRoundsPanel());
		add(testPanel, BorderLayout.PAGE_START);

		add(client1, BorderLayout.WEST);
		add(client2, BorderLayout.EAST);
		add(submit, BorderLayout.PAGE_END);

		pack();

		isSimulation.addItemListener(this);
		submit.addActionListener(this);

		setFocusable(true);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

	/**
	 * Játék vagy szimuláció elindítását vézgõ metódus
	 */
	private void startGame() {
		Client client1 = this.client1.getClient();
		Client client2 = this.client2.getClient();

		if (isSimulation.isSelected()) {
			new TronSimulation(WIDTH, HEIGHT, OBSTACLE_RATIO, client1, client2,
					roundsPanel.getNumRounds());
		} else {
			new Tron(WIDTH, HEIGHT, OBSTACLE_RATIO, client1, client2);
		}

		setVisible(false);
		dispose();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getItemSelectable();

		if (source == isSimulation) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				roundsPanel.setEnabled(true);
				client1.setSimulation(true);
				client2.setSimulation(true);
			} else {
				roundsPanel.setEnabled(false);
				client1.setSimulation(false);
				client2.setSimulation(false);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == submit) {
			startGame();
		}
	}

	/**
	 * Szimuláció beállításait összefogó panelt reprezentáló osztály
	 * 
	 * @author Dávid Bajzáth
	 */
	private class SimulationRoundsPanel extends JPanel implements
			ChangeListener {

		/**
		 * Körök számát beállító csúszka
		 */
		private JSlider roundsSlider = new JSlider(10, 100, 10);
		/**
		 * Körök számát megjelenítõ label
		 */
		private JLabel roundsLabel = new JLabel("10");

		public SimulationRoundsPanel() {
			initUI();
		}

		/**
		 * Kezelõ felületet inicializálása
		 */
		private void initUI() {
			add(roundsSlider);
			add(roundsLabel);
			add(new JLabel("rounds"));

			roundsSlider.addChangeListener(this);

			setEnabled(false);
		}

		public int getNumRounds() {
			return roundsSlider.getValue();
		}

		@Override
		public void setEnabled(boolean isEnabled) {
			super.setEnabled(isEnabled);
			for (Component component : getComponents()) {
				component.setEnabled(isEnabled);
			}
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			Object source = e.getSource();

			if (source == roundsSlider) {
				roundsLabel.setText(Integer.toString(roundsSlider.getValue()));
			}
		}

	}

	/**
	 * Egy kliens beállításait összefogó panelt reprezentál
	 * 
	 * @author Dávid Bajzáth
	 */
	private class ClientSettings extends JPanel implements ActionListener {

		/**
		 * Kliens id
		 */
		private int id;
		/**
		 * Játékos-e a kliens típusa
		 */
		private JRadioButton player = new JRadioButton("Player");
		/**
		 * AI-e a kliens típusa
		 */
		private JRadioButton ai = new JRadioButton("AI");
		/**
		 * AI szintjét állító spinner
		 */
		private JSpinner aiLevel = new JSpinner(new SpinnerNumberModel(
				0, 0, 10, 2));

		public ClientSettings(int id) {
			this.id = id;
			initUI();
		}

		/**
		 * Kezelõ felületet inicializálja
		 */
		private void initUI() {
			setLayout(new BorderLayout(5, 5));
			add(new JLabel("Client#" + id + " settings", SwingConstants.CENTER),
					BorderLayout.PAGE_START);

			player.setSelected(true);
			player.addActionListener(this);

			ai.addActionListener(this);

			aiLevel.setEnabled(false);

			ButtonGroup group = new ButtonGroup();
			group.add(player);
			group.add(ai);

			add(player, BorderLayout.WEST);
			add(ai, BorderLayout.CENTER);
			add(aiLevel, BorderLayout.EAST);
		}

		/**
		 * Szimuláció állítása (szimuláció esetén nem választható játékos)
		 * 
		 * @param isSimulation
		 */
		public void setSimulation(boolean isSimulation) {
			if (isSimulation) {
				player.setEnabled(false);
				ai.setSelected(true);
				aiLevel.setEnabled(true);
			} else {
				player.setEnabled(true);
			}
		}

		/**
		 * Létrehozza a beállításoknak megfelelõ klienst
		 * 
		 * @return kliens
		 */
		public Client getClient() {
			if (player.isSelected()) {
				return PlayerController.getNewModel();
			} else {
				return AIController.getNewModel((int) aiLevel.getValue());
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (player.isSelected()) {
				aiLevel.setEnabled(false);
			} else {
				aiLevel.setEnabled(true);
			}
		}

	}

	public static void main(String[] args) {
		new StartScreen();
	}

}

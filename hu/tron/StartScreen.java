package hu.tron;

import hu.tron.client.Client;
import hu.tron.client.ai.AIController;
import hu.tron.client.player.PlayerController;
import hu.tron.heuristic.Heuristic;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
 * Kezd� k�perny�t megval�s�t� oszt�ly
 * 
 * @author D�vid Bajz�th
 */
public class StartScreen extends JFrame 
		implements ItemListener, ActionListener {

	/**
	 * Grid sz�less�ge
	 */
	private static final int WIDTH = 30;
	/**
	 * Grid magass�ga
	 */
	private static final int HEIGHT = 20;
	/**
	 * Maxim�lis akad�ly ar�ny
	 */
	private static final double OBSTACLE_RATIO = 0.05;
	/**
	 * Szimul�ci� �s j�t�k k�z�l v�laszt� checkbox
	 */
	private JCheckBox isSimulation = new JCheckBox("Simulation");
	/**
	 * Testk�r�k be�ll�t�s�t t�rol� panel
	 */
	private SimulationRoundsPanel roundsPanel;
	/**
	 * Els� kliens be�ll�t�sai tartalmaz� panel
	 */
	private ClientSettings client1 = new ClientSettings(1);
	/**
	 * M�sodik kliens be�ll�t�sait tartalmaz� panel
	 */
	private ClientSettings client2 = new ClientSettings(2);
	/**
	 * Be�ll�t�sok v�gleges�t�s�re szolg�l� gomb
	 */
	private JButton submit = new JButton("Start");

	public StartScreen() {
		initUI();
	}

	/**
	 * Kezel� fel�let inicializ�l�sa
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
	 * J�t�k vagy szimul�ci� elind�t�s�t v�zg� met�dus
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
	 * Szimul�ci� be�ll�t�sait �sszefog� panelt reprezent�l� oszt�ly
	 * 
	 * @author D�vid Bajz�th
	 */
	private class SimulationRoundsPanel extends JPanel implements
			ChangeListener {

		/**
		 * K�r�k sz�m�t be�ll�t� cs�szka
		 */
		private JSlider roundsSlider = new JSlider(10, 100, 10);
		/**
		 * K�r�k sz�m�t megjelen�t� label
		 */
		private JLabel roundsLabel = new JLabel("10");

		public SimulationRoundsPanel() {
			initUI();
		}

		/**
		 * Kezel� fel�letet inicializ�l�sa
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
	 * Egy kliens be�ll�t�sait �sszefog� panelt reprezent�l
	 * 
	 * @author D�vid Bajz�th
	 */
	private class ClientSettings extends JPanel implements ActionListener {

		/**
		 * Kliens id
		 */
		private int id;
		/**
		 * J�t�kos-e a kliens t�pusa
		 */
		private JRadioButton player = new JRadioButton("Player");
		/**
		 * Random AI-e a kliens t�pusa
		 */
		private JRadioButton ai = new JRadioButton("Random AI");
		/**
		 * Minimax AI-e a kliens t�pusa
		 */
		private JRadioButton minimaxAI = new JRadioButton("Minimax AI");
		/**
		 * Minimax AI-hoz haszn�lt heurisztika t�pusa
		 */
		private JComboBox<Heuristic.type> heuristic = 
				new JComboBox<Heuristic.type>(Heuristic.type.values());
		/**
		 * AI szintj�t �ll�t� spinner
		 */
		private JSpinner aiLevel = new JSpinner(new SpinnerNumberModel(
				2, 2, 10, 2));
		
		public ClientSettings(int id) {
			this.id = id;
			initUI();
		}

		/**
		 * Kezel� fel�letet inicializ�lja
		 */
		private void initUI() {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
			
			add(new JLabel("Client#" + id + " settings", 
					SwingConstants.CENTER));

			player.addActionListener(this);
			player.setAlignmentX(Component.LEFT_ALIGNMENT);

			ai.addActionListener(this);
			ai.setAlignmentX(Component.LEFT_ALIGNMENT);

			aiLevel.setEnabled(false);
			aiLevel.setAlignmentX(Component.LEFT_ALIGNMENT);
			
			minimaxAI.addActionListener(this);
			minimaxAI.setAlignmentX(Component.LEFT_ALIGNMENT);

			ButtonGroup group = new ButtonGroup();
			group.add(player);
			group.add(ai);
			group.add(minimaxAI);
			
			add(player);
			add(ai);
			add(minimaxAI);
			
			JPanel minimaxAIPanel = new JPanel();
			minimaxAIPanel.add(heuristic);
			minimaxAIPanel.add(aiLevel);
			minimaxAIPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			
			add(minimaxAIPanel);
			
			player.doClick();
		}

		/**
		 * Szimul�ci� �ll�t�sa (szimul�ci� eset�n nem v�laszthat� j�t�kos)
		 * 
		 * @param isSimulation
		 */
		public void setSimulation(boolean isSimulation) {
			if (isSimulation) {
				player.setEnabled(false);
				ai.doClick();
			} else {
				player.setEnabled(true);
			}
		}

		/**
		 * L�trehozza a be�ll�t�soknak megfelel� klienst
		 * 
		 * @return kliens
		 */
		public Client getClient() {
			if (player.isSelected()) {
				return PlayerController.getNewModel();
			} else if (ai.isSelected()) {
				return AIController.getNewModel();
			} else {
				return AIController.getNewModel(
						(Heuristic.type) heuristic.getSelectedItem(), 
						(int) aiLevel.getValue());
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (minimaxAI.isSelected()) {
				aiLevel.setEnabled(true);
				heuristic.setEnabled(true);
			} else {
				aiLevel.setEnabled(false);
				heuristic.setEnabled(false);
			}
		}

	}

	public static void main(String[] args) {
		new StartScreen();
	}

}

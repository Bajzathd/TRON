package game.tron;

import game.tron.client.Client;
import game.tron.client.ai.AI;
import game.tron.client.player.Player;

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

public class StartScreen extends JFrame implements ItemListener, ActionListener {
	
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

	private static final long serialVersionUID = 3079305794169282317L;
	
	private JCheckBox isTestRun = new JCheckBox("Test run");
	private RoundsPanel roundsPanel;
	
	private ClientSettings client1 = new ClientSettings(1);
	private ClientSettings client2 = new ClientSettings(2);
	
	private JButton submit = new JButton("Start");
	
	public StartScreen() {
		initUI();
	}
	
	private void initUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Tron settings");
		setLayout(new BorderLayout(10, 10));
		
		JPanel testPanel = new JPanel();
		testPanel.add(isTestRun);
		testPanel.add(roundsPanel = new RoundsPanel());
		add(testPanel, BorderLayout.PAGE_START);
		
		add(client1, BorderLayout.WEST);
		add(client2, BorderLayout.EAST);
		add(submit, BorderLayout.PAGE_END);
		
		pack();
		
		isTestRun.addItemListener(this);
		submit.addActionListener(this);
		
		setFocusable(true);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	private void startGame() {
		Client client1 = this.client1.getClient();
		Client client2 = this.client2.getClient();
		
		if (isTestRun.isSelected()) {
			new TronSimulation(WIDTH, HEIGHT, OBSTACLE_RATIO, 
					client1, client2, roundsPanel.getNumRounds());
		} else {
			new Tron(WIDTH, HEIGHT, OBSTACLE_RATIO, client1, client2);
		}
		
		setVisible(false);
		dispose();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getItemSelectable();
		
		if (source == isTestRun) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				roundsPanel.setEnabled(true);
				client1.setTest(true);
				client2.setTest(true);
			} else {
				roundsPanel.setEnabled(false);
				client1.setTest(false);
				client2.setTest(false);
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

	private class RoundsPanel extends JPanel implements ChangeListener {
		
		private static final long serialVersionUID = -7523016490692509652L;

		private JSlider roundsSlider = new JSlider(10, 100, 10);
		private JLabel roundsLabel = new JLabel("10");

		public RoundsPanel() {
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
	
	private class ClientSettings extends JPanel implements ActionListener {
		
		private static final long serialVersionUID = -1303094088287723037L;

		private int id;
		
		private JRadioButton player = new JRadioButton("Player");
		private JRadioButton ai = new JRadioButton("AI");
		private JSpinner aiLevel = new JSpinner(
				new SpinnerNumberModel(1, 1, 10, 1));
		
		public ClientSettings(int id) {
			this.id = id;
			initUI();
		}
		
		private void initUI() {
			setLayout(new BorderLayout(5, 5));
			add(new JLabel("Client#"+id+" settings", SwingConstants.CENTER), BorderLayout.PAGE_START);
			
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
		
		public void setTest(boolean isTest) {
			if (isTest) {
				player.setEnabled(false);
				ai.setSelected(true);
				aiLevel.setEnabled(true);
			} else {
				player.setEnabled(true);
			}
		}
		
		public Client getClient() {
			if (player.isSelected()) {
				return new Player(id);
			} else {
				return new AI(id, (int) aiLevel.getValue());
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

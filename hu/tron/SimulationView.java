package hu.tron;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

/**
 * Szimulált futtatások folyamatának megjelenítéséért felelõs osztály
 * 
 * @author Dávid Bajzáth
 */
public class SimulationView extends JFrame {

	/**
	 * Elindított szimulációk száma
	 */
	private int numSimulations;
	/**
	 * Státusz jelzése a felhasználó felé
	 */
	private JLabel status = new JLabel("Processing...", SwingConstants.CENTER);
	/**
	 * Befejezett szimulációk aránya az elindítottakhoz képest
	 */
	private JProgressBar progressBar;
	
	public SimulationView(int numSimulations) {
		this.numSimulations = numSimulations;
		
		initUI();
	}
	
	/**
	 * Kezelõ felületet inicializálása
	 */
	private void initUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Tron simulation");
		setLayout(new BorderLayout(10, 10));
		
		progressBar = new JProgressBar(0, numSimulations);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		
		add(status, BorderLayout.PAGE_START);
		add(progressBar, BorderLayout.CENTER);
		
		setSize(300, 100);
		
		setFocusable(true);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	/**
	 * Egy szimuláció végét jelzi a megjelenítés felé
	 */
	public void progress() {
		progressBar.setValue(progressBar.getValue() + 1);
	}
	
	/**
	 * Az összes szimuláció befejezõdött
	 */
	public void done() {
		progressBar.setValue(numSimulations);
		status.setText("Done.");
	}
	
}

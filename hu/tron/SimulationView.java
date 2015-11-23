package hu.tron;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

/**
 * Szimul�lt futtat�sok folyamat�nak megjelen�t�s��rt felel�s oszt�ly
 * 
 * @author D�vid Bajz�th
 */
public class SimulationView extends JFrame {

	/**
	 * Elind�tott szimul�ci�k sz�ma
	 */
	private int numSimulations;
	/**
	 * St�tusz jelz�se a felhaszn�l� fel�
	 */
	private JLabel status = new JLabel("Processing...", SwingConstants.CENTER);
	/**
	 * Befejezett szimul�ci�k ar�nya az elind�tottakhoz k�pest
	 */
	private JProgressBar progressBar;
	
	public SimulationView(int numSimulations) {
		this.numSimulations = numSimulations;
		
		initUI();
	}
	
	/**
	 * Kezel� fel�letet inicializ�l�sa
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
	 * Egy szimul�ci� v�g�t jelzi a megjelen�t�s fel�
	 */
	public void progress() {
		progressBar.setValue(progressBar.getValue() + 1);
	}
	
	/**
	 * Az �sszes szimul�ci� befejez�d�tt
	 */
	public void done() {
		progressBar.setValue(numSimulations);
		status.setText("Done.");
	}
	
}

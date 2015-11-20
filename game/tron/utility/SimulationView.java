package game.tron.utility;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class SimulationView extends JFrame {

	private static final long serialVersionUID = -5471897075503102838L;
	
	private int numSimulations;
	private JLabel status = new JLabel("Processing...");
	private JProgressBar progressBar;
	
	public SimulationView(int numSimulations) {
		this.numSimulations = numSimulations;
		
		initUI();
	}
	
	private void initUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Tron simulation");
		setLayout(new BorderLayout(10, 10));
		
		progressBar = new JProgressBar(0, numSimulations);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		
		add(status, BorderLayout.PAGE_START);
		add(progressBar, BorderLayout.CENTER);
		
		pack();
		
		setFocusable(true);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	public void progress() {
		progressBar.setValue(progressBar.getValue() + 1);
	}
	
	public void done() {
		status.setText("Done.");
	}
	
}

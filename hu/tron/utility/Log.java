package hu.tron.utility;

import hu.tron.client.Client;
import hu.tron.grid.Grid;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Eredm�nyek logol�s�t v�zg� oszt�ly
 * 
 * @author D�vid Bajz�th
 */
public class Log {

	/**
	 * Els� kliens
	 */
	private Client client1;
	/**
	 * M�sodik kliens
	 */
	private Client client2;

	/**
	 * Eredm�nyek<br/>
	 * 0: d�ntetlenek sz�ma<br/>
	 * 1: els� kliens nyert k�reinek sz�ma<br/>
	 * 2: m�sodik kliens nyert k�reinek sz�ma
	 */
	private int[] results = new int[3];
	
	private long[] stepTimes = {0, 0};
	private int[] steps = {0, 0};
	
	public Log(Client client1, Client client2) {
		this.client1 = client1;
		this.client2 = client2;
	}
	
	public void addStepTime(Client client, long stepTime) {
		int index = client.getId() - 1;
		stepTimes[index] += stepTime;
		steps[index]++;
	}

	/**
	 * Egy k�r eredm�ny�nek elt�rol�sa
	 * 
	 * @param grid
	 *            J�t�kt�r v�gs� �llapota
	 */
	public void addResult(Grid grid) {
		List<Client> aliveClients = grid.getAliveClients();
		if (aliveClients.size() == 0) {
			results[0]++;
		} else {
			int winnerId = aliveClients.get(0).getId();
			results[winnerId]++;
		}
	}

	/**
	 * Eredm�nyek log mapp�ban tal�lhat� f�jlba �r�sa.
	 */
	public void writeToFile() {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(Calendar.getInstance().getTime());
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("log/" + timeStamp + ".log"), 
					"utf-8"));

			writer.write("Ties: " + results[0] + "\n");
			writer.write(client1 + " won " + results[1] + " times\n");
			writer.write(client2 + " won " + results[2] + " times\n");
			
			writer.write("\nAverage step times:\n");
			writer.write(client1 + ": " + (stepTimes[0] / steps[0] / 1000000) 
					+ " ms\n");
			writer.write(client2 + ": " + (stepTimes[1] / steps[1] / 1000000) 
					+ " ms\n");
			
			int avgSteps = steps[0] / (results[0] + results[1] + results[2]);
			writer.write("\nAverage number of steps per round: " + avgSteps);
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}

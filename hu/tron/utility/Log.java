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
 * Eredmények logolását vézgõ osztály
 * 
 * @author Dávid Bajzáth
 */
public class Log {

	/**
	 * Elsõ kliens
	 */
	private Client client1;
	/**
	 * Második kliens
	 */
	private Client client2;

	/**
	 * Eredmények<br/>
	 * 0: döntetlenek száma<br/>
	 * 1: elsõ kliens nyert köreinek száma<br/>
	 * 2: második kliens nyert köreinek száma
	 */
	private int[] results = new int[3];

	public Log(Client client1, Client client2) {
		this.client1 = client1;
		this.client2 = client2;
	}

	/**
	 * Egy kör eredményének eltárolása
	 * 
	 * @param grid
	 *            Játéktér végsõ állapota
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
	 * Eredmények log mappában található fájlba írása.
	 */
	public void writeToFile() {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(Calendar.getInstance().getTime());
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("log/" + timeStamp + ".log"), "utf-8"));

			writer.write("Ties: " + results[0] + "\n");
			writer.write(client1 + " won " + results[1] + " times\n");
			writer.write(client2 + " won " + results[2] + " times");

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

package game.tron.utility;

import game.tron.client.Client;
import game.tron.grid.Grid;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Log {
	
	private Client client1;
	private Client client2;

	/**
	 * 0:	döntetlenek száma
	 * >0:	adott kliens nyert köreinek száma
	 */
	private int[] results = new int[3];
	
	public Log(Client client1, Client client2) {
		this.client1 = client1;
		this.client2 = client2;
	}
	
	public void addResult(Grid grid) {
		List<Client> aliveClients = grid.getAliveClients();
		if (aliveClients.size() == 0) {
			results[0]++;
		} else {
			int winnerId = aliveClients.get(0).getId();
			results[winnerId]++;
		}
	}
	
	public void writeToFile() {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		Writer writer = null;
		
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
		              new FileOutputStream("log/"+timeStamp+".log"), "utf-8"));
			
			writer.write("Ties: "+results[0]+"\n");
			writer.write(client1+" won "+results[1]+" times\n");
			writer.write(client2+" won "+results[2]+" times");
			
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
	
	public int[] getResults() {
		return results;
	}
	
}

package game.tron.utility;

import game.tron.client.Client;
import game.tron.grid.Grid;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Log {
	
	public boolean isInited = false;
	
	private List<Client> clients = new ArrayList<Client>();

	/**
	 * 0:	döntetlenek száma
	 * >0:	adott kliens nyert köreinek száma
	 */
	private int[] results;
	
	public void init(List<Client> clients) {
		this.clients = new ArrayList<Client>(clients);
		
		results = new int[clients.size() + 1];
		for (int i = 0; i < results.length; i++) {
			results[i] = 0;
		}
		
		isInited = true;
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
			Client client;
			
			writer.write("Ties: "+results[0]+"\n");
			for (int i = 0; i < clients.size(); i++) {
				client = clients.get(i);
				writer.write(client+" wins: "+results[client.getId()]+"\n");
			}
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

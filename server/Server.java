package server;

import java.awt.Point;
import java.util.Random;

import client.Client;
import map.TronMap;

public class Server {
	
	private TronMap map;
	private int width;
	private int height;
	
	private Client[] clients;
	private int numLivingClients;
	private int sumClientIds = 0;
	
	public Server(final Client[] clients){
		this.clients = clients;
		
		this.numLivingClients = clients.length;
		for( Client client : clients ){
			this.sumClientIds += client.getId();
		}
	}
	
	public void generateMap(final int width, final int height){
		this.width = width;
		this.height = height;
		
		System.out.println("Generating map..."); //TODO GUI
		this.map = new TronMap(width, height);
	}
	
	public void startGame(final int refreshInterval){
		Random rnd = new Random();
		Point p;
		
		for(Client client : this.clients){
			do {
				p = new Point(rnd.nextInt(this.width), rnd.nextInt(this.height));
			} while(this.map.getValue(p) != 0);
			
			client.setStart(p, rnd.nextInt(4));
			this.map.setValue(p, client.getId());
		}
		
		Thread mapRefresher = new Thread(new RefreshMap(this, refreshInterval));
		mapRefresher.start();
	}
	
	public void refreshMap(){
		for(Client client : clients){
			if(client.isAlive()){
				client.step();
				Point clientPosition = client.getPosition();
				
				if(
					!map.isInside(clientPosition)					//kiment a pályáról
					|| map.getValue(clientPosition) != TronMap.FREE	//nem üres mezõre lépett
				){
					this.killClient(client);
				} else {
					map.setValue(clientPosition, client.getId());
				}
			}
		}
		map.print();
	}
	
	private void killClient(final Client client){
		
		client.kill();
		
		numLivingClients--;
		sumClientIds -= client.getId();
	}
	
	public boolean isOver(){
		
		if( this.numLivingClients == 1 ){
			System.out.printf("Client #%d won!\n", this.sumClientIds); //TODO GUI
			return true;
		}
		return false;
	}
	
}

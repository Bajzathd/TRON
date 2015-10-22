package server;

import java.awt.Point;
import java.util.Random;

import client.Client;
import map.TronMap;

public class Server {
	
	private TronMap map;
	private Client[] clients;
	
	private int numLivingClients;
	private int sumClientIds = 0;
	
	private static final int WIDTH = 100;
	private static final int HEIGHT = 20;
	
	public Server(final int numberOfClients){
		
		System.out.println("Generating map...");
		this.map = new TronMap(WIDTH, HEIGHT); //TODO kivinni konstruktorból megadhatónak
		
		this.numLivingClients = numberOfClients;
		
		this.clients = new Client[numberOfClients];
		Random rnd = new Random();
		Point p;
		for(int id = 1; id <= this.clients.length; id++){
			do {
				p = new Point(rnd.nextInt(WIDTH), rnd.nextInt(HEIGHT));
			} while(map.getValue(p) != 0);
			clients[id - 1] = new Client(id, p, rnd.nextInt(4));
			this.sumClientIds += id;
		}
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
			System.out.printf("Client #%d won!\n", this.sumClientIds); //TODO kivinni gui-ba
			return true;
		}
		return false;
	}
	
}

package server;

import java.awt.Point;
import java.util.Random;

import client.Client;
import map.TronMap;

public class Server {
	
	private TronMap map;
	private Client[] clients;
	
	private static final int WIDTH = 150;
	private static final int HEIGHT = 30;
	
	public Server(final int numberOfClients){
		
		System.out.println("Generating map...");
		this.map = new TronMap(150, 30);
		
		this.clients = new Client[numberOfClients];
		Random rnd = new Random();
		Point p;
		for(int id = 1; id <= this.clients.length; id++){
			do {
				p = new Point(rnd.nextInt(WIDTH), rnd.nextInt(HEIGHT));
			} while(map.getValue(p) != 0);
			clients[id - 1] = new Client(id, p);
			clients[id - 1].setDirection(rnd.nextInt(4));
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
					client.kill();
				} else {
					map.setValue(clientPosition, client.getId());
				}
			}
		}
		map.print();
	}
	
	public boolean isOver(){
		
		for(Client client : clients){
			if(client.isAlive()){
				return false;
			}
		}
		return true;
	}
	
}

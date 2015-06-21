package server;

import java.awt.Point;
import java.util.Random;

import client.Client;
import map.TronMap;
import map.TronMap_withPoints;

public class Server extends Thread{
	
	private TronMap map;
	private Client[] clients;
	
	private static final int WIDTH = 150;
	private static final int HEIGHT = 30;
	private static final Point[] startPositions = new Point[]{
		new Point(WIDTH/3, HEIGHT/3)
		,new Point(2*WIDTH/3, HEIGHT/3)
		,new Point(WIDTH/3, 2*HEIGHT/3)
		,new Point(2*WIDTH/3, 2*HEIGHT/3)
	};
	
	public Server(final int numberOfClients){
		if(numberOfClients > startPositions.length){
			throw new IndexOutOfBoundsException();
		}
		this.map = new TronMap(150, 30);
		
		this.clients = new Client[numberOfClients];
		Random rnd = new Random();
		for(int id = 1; id <= this.clients.length; id++){
			clients[id - 1] = new Client(id, startPositions[id-1]);
			clients[id - 1].setDirection(rnd.nextInt(4));
		}
	}
	
	private void refreshMap(){
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
	
	public void run(){
		try{
			do{
				this.refreshMap();
				Thread.sleep(1000);
			} while(true);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
	}
	
}

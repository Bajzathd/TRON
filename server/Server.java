package server;

import gui.UiHandler;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import client.Client;
import map.TronMap;

public class Server {
	
	private TronMap map;
	private int width;
	private int height;
	
	private int refreshInterval = 400;
	
	private Client[] clients;
	private Map<Integer, Point> clientPositions = new HashMap<Integer, Point>(); //<clientId, clientPosition>
	private ArrayList<Client> aliveClients = new ArrayList<Client>();
	
	public Server(final Client[] clients){
		this.clients = clients;
		
		for( Client client : clients ){
			this.aliveClients.add(client);
		}
	}
	
	public void generateMap(final int width, final int height){
		this.width = width;
		this.height = height;
		
		UiHandler.generatingMap();
		this.map = new TronMap(width, height);
	}
	
	public void startGame(){
		Random rnd = new Random();
		Point p;
		
		for(Client client : this.aliveClients){
			do {
				p = new Point(rnd.nextInt(this.width), rnd.nextInt(this.height));
			} while(this.map.getValue(p) != TronMap.FREE);
			
			client.setStart(rnd.nextInt(4));
			this.clientPositions.put(client.getId(), p);
			this.map.setValue(p, client.getId());
		}
		
		UiHandler.drawMap(this.map);
		
		Thread mapRefresher = new Thread(new RefreshMap(this));
		mapRefresher.start();
	}
	
	public void refreshMap(){
		ArrayList<Point> newPositions = new ArrayList<Point>();
		ArrayList<Client> clientsToKill = new ArrayList<Client>();
		
		for(Client client : this.aliveClients){
			//minden élõ klienst léptetünk
			Point clientPosition = this.stepClient(client);
			
			if(this.map.getValue(clientPosition) != TronMap.FREE){
				clientsToKill.add(client);
				if(newPositions.contains(clientPosition)){
					//szembõl egymásnak mentek
					for(Client otherClient : this.aliveClients){
						if(this.clientPositions.get(otherClient) == clientPosition){
							clientsToKill.add(otherClient);
						}
					}
				}
			} else {
				newPositions.add(clientPosition);
				this.map.setValue(clientPosition, -client.getId());
			}
		}
		
		for(Client client : clientsToKill){
			this.killClient(client);
		}
		
		UiHandler.drawMap(map);
	}
	
	private void killClient(final Client client){
		client.kill();
		this.aliveClients.remove(client);
	}
	
	private Point stepClient(final Client client){
		Point clientPosition = clientPositions.get(client.getId());
		
		switch (client.getDirection()){
			case Client.DIRECTION_UP: 	
				clientPosition.translate(0, -1); 
				break;
			case Client.DIRECTION_RIGHT: 
				clientPosition.translate(1, 0); 
				break;
			case Client.DIRECTION_DOWN: 
				clientPosition.translate(0, 1); 
				break;
			case Client.DIRECTION_LEFT: 
				clientPosition.translate(-1, 0);
				break;
		}
		
		return clientPosition;
	}
	
	public boolean isOver(){
		switch(this.aliveClients.size()){
			case 0:
				UiHandler.tie();
				return true;
			case 1:
				UiHandler.win(this.aliveClients.get(0));
				return true;
			default:
				return false;
		}
	}
	
	public int getRefreshInterval(){
		return this.refreshInterval;
	}
	
}

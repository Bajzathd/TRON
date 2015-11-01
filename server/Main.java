package server;

import client.Client;

public class Main {
	
	public static void main(String [ ] args){
		final int numClients = 2;
		final int mapWidth = 150;
		final int mapHeight = 30;
		
		Client[] clients = new Client[numClients];
		
		for(int id = 1; id <= numClients; id++){
			clients[id - 1] = new Client(id);
		}
		
		Server server = new Server(clients);
		server.generateMap(mapWidth, mapHeight);
		server.startGame();
	}
	
}

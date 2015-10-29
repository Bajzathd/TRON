package gui;

import client.Client;
import map.TronMap;

public class UiHandler {

	public static void drawMap(TronMap map){
		for(int x = 0; x <= map.getWidth(); x++){
			System.out.print("#");
		}
		System.out.println("#");
		for(int y = 0; y < map.getHeight(); y++){
			System.out.print("#");
			for(int x = 0; x < map.getWidth(); x++){
				int mapValue = map.getValue(x, y);
				
				if( mapValue > 0 ){
					System.out.print("#");
				} else if( mapValue < 0 ){
					System.out.print(-mapValue);
				} else {
					System.out.print(" ");
				}
				
//				switch(mapValue){
//					case TronMap.FREE:
//						System.out.print(" ");
//						break;
//					case TronMap.OBSTACLE:
//						System.out.print("#");
//						break;
//					default:
//						System.out.print(mapValue);
//				}
			}
			System.out.println("#");
		}
		for(int x = -1; x <= map.getWidth(); x++){
			System.out.print("#");
		}
		System.out.println();
	}

	public static void tie() {
		System.out.println("Tie");
	}

	public static void win(Client client) {
		System.out.printf("Client #%d won!", client.getId());
	}

	public static void generatingMap() {
		System.out.println("Generating map...");
	}
	
}

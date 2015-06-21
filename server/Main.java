package server;

public class Main {

	public static void main(String [ ] args){
		Server server = new Server(2);
		Thread refresher = new Thread(new RefreshMap(server));
		
		refresher.start();
	}
	
}

package server;

public class RefreshMap implements Runnable {
	
	private Server server;
	
	public RefreshMap(Server server){
		this.server = server;
	}

	@Override
	public void run() {
		try{
			do{
				server.refreshMap();
				Thread.sleep(100);
			} while(!server.isOver());
			System.out.println("Game over");
			
		} catch(Exception ex) { }
	}

}

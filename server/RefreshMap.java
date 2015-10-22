package server;

public class RefreshMap implements Runnable {
	
	private Server server;
	private int refreshInterval;
	
	public RefreshMap(final Server server, final int refreshInterval){
		this.server = server;
		this.refreshInterval = refreshInterval;
	}

	@Override
	public void run() {
		if(this.refreshInterval > 0){
			try{
				do{
					this.server.refreshMap();
					Thread.sleep(this.refreshInterval);
				} while(!this.server.isOver());
				
			} catch(InterruptedException ex) {
				//valami megszakította a szál futását
			}
		} else {
			this.server.refreshMap();
		}
	}

}

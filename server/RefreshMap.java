package server;

public class RefreshMap implements Runnable {
	
	private Server server;
	
	public RefreshMap(final Server server){
		this.server = server;
	}

	@Override
	public void run() {
		if(this.server.getRefreshInterval() > 0){
			try{
				do{
					this.server.refreshMap();
					Thread.sleep(this.server.getRefreshInterval());
				} while(!this.server.isOver());
				
			} catch(InterruptedException ex) {
				//valami megszakította a szál futását
			}
		} else {
			this.server.refreshMap();
		}
	}

}

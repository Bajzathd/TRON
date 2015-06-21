package server;

import map.TronMap;
import map.TronMap_withPoints;

public class Server {
	
	public long sumTime = 0;
	public long sumTime_withPoints = 0;

	public static void main(String [ ] args){
		Server s = new Server();
		
		for(int i=0; i<10; i++){
			s.runTest_withPoints();
			s.runTest();
		}
		
		s.sumTime /= 10;
		s.sumTime_withPoints /= 10;
		
		System.out.printf("sumTime: %d, sumTime_withPoints: %d", s.sumTime, s.sumTime_withPoints);
	}
	
	public void runTest(){
	
		long startTime = System.currentTimeMillis();
		
		TronMap map = new TronMap(150, 30);
		
		long endTime = System.currentTimeMillis();
		
		sumTime += endTime - startTime;
			
	}
	
	public void runTest_withPoints(){
		
		long startTime = System.currentTimeMillis();
		
		TronMap_withPoints map = new TronMap_withPoints(150, 30);
		
		long endTime = System.currentTimeMillis();
		
		sumTime_withPoints += endTime - startTime;
			
	}
	
}

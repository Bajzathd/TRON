package server;

import map.TronMap;

public class Server_withPoints {

	public static void main(String [ ] args){
		Server_withPoints.runTests(10);
	}
	
	public static void runTests(int times){
		long sumTime = 0;
		
		for(int i=0; i<times; i++){
			
			long startTime = System.currentTimeMillis();
			
			TronMap map = new TronMap(150, 30);
			
			long endTime = System.currentTimeMillis();
			
			sumTime += endTime - startTime;
			
			System.out.println(i + " " + (endTime - startTime)+"ms");
			
			map.print();
		}
		
		sumTime /= times;
		
		System.out.println("Average execution time: " + sumTime );
	}
	
}

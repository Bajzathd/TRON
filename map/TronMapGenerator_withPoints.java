package map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class TronMapGenerator_withPoints {
	
	private int width;
	private int height;
	private Map<Point, Integer> map;
	
	private Random rnd;
	private List<Point> visitedPoints;
	private List<Point> obstacles;
	private final double limit = 0.05;

	public TronMapGenerator_withPoints() {
		
		this.map = new HashMap<Point, Integer>();
		
		this.rnd = new Random();
		this.visitedPoints = new ArrayList<Point>();
		this.obstacles = new ArrayList<Point>();
	}
	
	/**
	 * Legenerálja a pályát
	 * 0: játszható pályarész
	 * <0: akadály
	 * >0: játékos
	 */
	public Map<Point, Integer> generateMap(int width, int height){
		this.width = width;
		this.height = height;
		Point randomCenter;
		
		//minden mezõt játszhatóra állítunk
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				this.map.put(new Point(x, y), 0);
			}
		}
		
		do{
			randomCenter = new Point(this.rnd.nextInt(width), this.rnd.nextInt(height));
			this.pacagen(randomCenter);
			this.checkDrop();
//			System.out.printf("visited:%d sum:%d obstacles:%d isUnderLimit:%b\n", visitedPoints.size(), (width*height), obstacles.size(), isUnderLimit());
//			printMap();
		} while(this.isUnderLimit());
		
		return this.map;
	}
	
	private void pacagen(final Point p){
		if( !this.isValidPoint(p) ) return; // ne szomszédonként kelljen ifelni
		if( this.getPoint(p) != 0 ) return; // ezt a mezõt már láttuk vagy most (-2), vagy korábban (-1) -- lehetne úgy is, hogy a kép szélén van egy -1-es keret és akkor nem kell az elõzõ sor ifje
		if( rnd.nextDouble() < 0.6 ) return; // egy látott mezõt ez most épp 60% eséllyel elfoglal, 40% eséllyel nem
		this.setPoint(p, -2); // elfoglaltuk most
		obstacles.add(p);
		for(Point neighbor : this.getNeighbors(p)){
			this.pacagen(neighbor);
		}
//		pacaGen( x-1,y) ; pacaGen( x+1, y); pacaGen( x,y-1); pacaGen(x,y+1)
	}
	
	private boolean isUnderLimit(){
		return ( obstacles.size() / (double)(width*height)  ) <= limit;
	}
	
	private boolean isEveryPointVisited(){
		return visitedPoints.size() == ( (width*height) - obstacles.size() );
	}
	
	private void randomizeMap(final Point p){
		List<Point> neighbors = this.getNeighbors(p);
		
		for(Point neighbor : neighbors){	
			if(getPoint(neighbor) == 0 && rnd.nextInt(3) == 0){
				setPoint(neighbor, -2);
				obstacles.add(neighbor);
				randomizeMap(neighbor);
			}
		}
	}
	
	private void checkDrop(){
		try{
			floodFill();
			handleLastDrop(isEveryPointVisited());
			
		} catch(Exception e){ //TODO saját típus
			//megtelt a map
			handleLastDrop(false);
			return;
		}
	}
	
	private void floodFill() throws Exception{ //TODO saját típus
		Point startPoint = getFirstEmpty();
		visitedPoints.clear();
		visitNeighbors(startPoint);
	}
	
	private Point getFirstEmpty() throws Exception{ //TODO saját típus
		Point p;
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
				p = new Point(x, y);
				if(getPoint(p) == 0){
					return p;
				}
			}
		}
		throw new Exception("Nincs üres pont"); //TODO saját exception típus
	}
	
	private void visitNeighbors(final Point p){
		visitedPoints.add(p);
		for(Point neighbor : getNeighbors(p)){
			if(getPoint(neighbor) == 0 && !visitedPoints.contains(neighbor)){
				visitNeighbors(neighbor);
			}
		}
	}

	/**
	 * Az utolsó drop akadályainak kezelése
	 * @param isValid true esetén véglegesíti, false esetén visszavonja
	 */
	private void handleLastDrop(boolean isValid){
		int pointValue = isValid ? -1 : 0;
		List<Point> obstaclesClone = new ArrayList<Point>();
		obstaclesClone.addAll(obstacles);
		for(Point p : obstaclesClone){
			if(getPoint(p) == -2){
				setPoint(p, pointValue);
				if(!isValid){
					obstacles.remove(p);
				}
			}
		}
	}
	
	private Set<Point> getAllPoints(){
		return map.keySet();
	}
	
	private List<Point> getNeighbors(final Point p){
		List<Point> neighbors = new ArrayList<Point>();
		Point p2;
		
		p2 = (Point) p.clone();
		p2.translate(0, 1);
		if(isValidPoint(p2)){
			neighbors.add(p2);
		}
		
		p2 = (Point) p.clone();
		p2.translate(1, 0);
		if(isValidPoint(p2)){
			neighbors.add(p2);
		}
		
		p2 = (Point) p.clone();
		p2.translate(0, -1);
		if(isValidPoint(p2)){
			neighbors.add(p2);
		}
		
		p2 = (Point) p.clone();
		p2.translate(-1, 0);
		if(isValidPoint(p2)){
			neighbors.add(p2);
		}
		
		return neighbors;
	}
	
	private boolean isValidPoint(Point p){
		int  x = (int)p.getX()
				,y = (int)p.getY();
			
			if(x >= 0 && x < width && y >= 0 && y < height){
				return true;
			} else {
				return false;
			}
	}
	
	private void setPoint(final Point p, final int e){
		if(isValidPoint(p)){
			map.put(p, e);
		}
	}
	
	private int getPoint(final Point p){
		return map.get(p);
	}
	
	public void printMap(){
		Point p;
		
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				p = new Point(x,y);
				
				if(getPoint(p) == 0){
					System.out.print(" ");
				} else {
					System.out.print("#");
				}
//				System.out.printf("%d ", this.map.get(p));
				
			}
			System.out.println();
		}
	}

}

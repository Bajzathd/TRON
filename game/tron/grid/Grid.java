package game.tron.grid;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.omg.CosNaming.NamingContextPackage.NotFound;

import game.tron.client.AI;
import game.tron.client.Client;
import game.tron.client.Player;
import game.tron.grid.element.GridElement;
import game.tron.grid.element.Obstacle;
import game.tron.grid.element.Trail;
import game.tron.utility.Direction;

public class Grid {
	
	private Rectangle gridRectangle;
     
    private GridElement[][] elements;
    private int numFloors;
     
    private static Random random = new Random();
     
    private List<Player> players = new ArrayList<Player>();
    private List<AI> ais = new ArrayList<AI>();
    
    private List<Client> aliveClients = new ArrayList<Client>();
    
    private List<Obstacle> obstacles;
    private List<Trail> trails = new ArrayList<Trail>();
    
    public Grid(GridElement[][] elements, List<Obstacle> obstacles) {
    	 this.elements = elements;
    	 this.obstacles = obstacles;
    	 numFloors = elements[0].length * elements.length - obstacles.size();
    	 
    	 gridRectangle = new Rectangle(elements[0].length, elements.length);
     }
    
    public Grid clone() {
    	GridElement[][] elements = new GridElement[this.elements[0].length][];
    	for (int index = 0; index < this.elements.length; index++) {
    		elements[index] = Arrays.copyOf(this.elements[index], this.elements[index].length);
    	}
    	List<Obstacle> obstacles = new ArrayList<Obstacle>(this.obstacles);
    	
    	Grid clone = new Grid(elements, obstacles);
    	
    	clone.players = new ArrayList<Player>(players);
    	clone.ais = new ArrayList<AI>(ais);
    	clone.aliveClients = new ArrayList<Client>(aliveClients);
    	clone.trails = new ArrayList<Trail>(trails);
    	
    	return clone;
    }
     
     public void addPlayer(Player player) {
    	 players.add(player);
     }
     
     public void addAI(AI ai) {
    	 ais.add(ai);
     }
     
     public void start() {
    	 Point position;
    	 Direction direction;
    	 Point nextPosition;
    	 
    	 aliveClients.addAll(players);
    	 aliveClients.addAll(ais);
    	 
    	 for (Client client : aliveClients) {
    		 do {
	    		 position = new Point(
    				 random.nextInt(getWidth()),
    				 random.nextInt(getHeight())
				 );
	    		 direction = Direction.getRandom();
	    		 nextPosition = direction.getTranslatedPoint(position);
    		 } while (! isFloor(position) && ! isFloor(nextPosition));
    		 
    		 client.setStart(position, direction);
    		 elements[position.y][position.y] = new Trail(position, client.getId());
    		 numFloors--;
    	 }
     }
     
     public void killClient(Client client) {
    	 aliveClients.remove(client);
    	 client.kill();
     }
     
     public void addTrail(Trail trail) {
    	 trails.add(trail);
    	 elements[trail.getY()][trail.getX()] = trail;
    	 numFloors--;
     }
     
     public int getWidth() {
    	 return gridRectangle.width;
     }
     
     public int getHeight() {
    	 return gridRectangle.height;
     }
     
     public List<Client> getAliveClients() {
    	 return aliveClients;
     }

     public List<Player> getPlayers() {
    	 return players;
     }
     
     public List<AI> getAis() {
    	 return ais;
     }
     
     public Object[][] getElements() {
    	 return elements;
     }
     
     public List<Obstacle> getObstacles() {
    	 return obstacles;
     }
     
     public List<Trail> getTrails() {
    	 return trails;
     }
     
     public List<Direction> getValidDirections(Point p) throws NotFound {
    	 List<Direction> validDirections = new ArrayList<Direction>();
    	 
    	 for (Direction direction : Direction.getAllShuffled()) {
    		 if (isFloor(direction.getTranslatedPoint(p))) {
    			 validDirections.add(direction);
    		 }
    	 }
    	 
    	 if (validDirections.isEmpty()) {
    		 throw new NotFound();
    	 }
    	 
    	 return validDirections;
     }
     
     public GridElement getElement(Point p) {
    	 if (gridRectangle.contains(p)) {
    		 return elements[p.y][p.x];
    	 }
    	 return new Obstacle(p, 1);
     }
     
     public boolean isFloor(Point p) {
    	 return getElement(p) == null;
     }
     
     public int getNumFloors() {
    	 return numFloors;
     }
     
}

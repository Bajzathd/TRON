package game.tron.utility;

import game.tron.client.Client;
import game.tron.grid.GridController;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.omg.CosNaming.NamingContextPackage.NotFound;

public class MinimaxTree {
	
    private Node root;

    public MinimaxTree(GridController gridController, int clientId) {
        root = new Node(null);
        root.isMyTurn = true;
        root.clientId = clientId;
        root.gridController = gridController.clone();
    }
    
    public Node getRoot() {
    	return root;
    }
    
    public Direction getDirection() {
    	Direction direction = null;
    	double maxGrade = Integer.MIN_VALUE;
    	
    	System.out.println(this);
    	evaluate(root, -Double.MAX_VALUE, Double.MAX_VALUE);
    	root.orderChildren();
    	System.out.println(this);
    	
    	for (Node child : root.getChildren()) {
    		if (child.grade != null && child.grade > maxGrade) {
    			maxGrade = child.grade;
    			direction = child.direction;
    		}
    	}
    	
    	resetGrade(root);
    	
    	return direction;
    }
    
    private void resetGrade(Node node) {
    	node.grade = null;
    	for (Node child : node.children) {
    		resetGrade(child);
    	}
    }
    
    /**
     * Alfa-béta vágás algoritmus
     * @param node állás
     * @param alpha legjobb kliens lépés értéke
     * @param beta legjobb ellenfél lépés értéke
     * @return állás értéke a kliens szemszögéből
     */
    private double evaluate(Node node, double alpha, double beta) {
    	if (node.grade != null) {
    		return node.grade;
    	}
    	if (node.isMyTurn) {
    		for (Node child : node.children) {
    			alpha = Math.max(alpha, evaluate(child, alpha, beta));
    			if (beta <= alpha) {
    				node.grade = beta;
        			return beta;
        		}
    		}
    		node.grade = alpha;
    		return alpha;
    	} else {
    		for (Node child : node.children) {
    			beta = Math.min(beta, evaluate(child, alpha, beta));
    			if (beta <= alpha) {
    				node.grade = alpha;
        			return alpha;
        		}
    		}
    		node.grade = beta;
    		return beta;
    	}
    }
    
    public String toString() {
    	return root.print("", true);
    }
    
    public class Node {
        private Direction direction;
        private int clientId;
        private Node parent;
        private boolean isMyTurn;
        private List<Node> children = new ArrayList<Node>();
        
        public GridController gridController;
        public Double grade = null;
        
        private Node(Direction direction) {
        	this.direction = direction;
        }
        
        public void setChildren() {
        	if (! children.isEmpty()) {
        		return;
        	}
        	
        	List<Direction> validDirections;
        	Client client;
        	
			try {
				if (isMyTurn) {
					client = gridController.getGrid().getClient(clientId);
				} else {
					client = gridController.getGrid().getEnemy(clientId);
				}
				
				validDirections = gridController.getGrid().getValidDirections(
						client.getPosition());
				
				for (Direction direction : validDirections) {
					Node node = new Node(direction);
					
		        	node.parent = this;
		        	node.clientId = clientId;
		        	node.isMyTurn = ! isMyTurn;
		        	node.gridController = gridController.clone();
		        	
		        	if (isMyTurn) {
						client = node.gridController.getGrid().getClient(clientId);
					} else {
						client = node.gridController.getGrid().getEnemy(clientId);
					}
		        	client.trySetDirection(direction);
		        	client.step();
		        	node.gridController.evaluate();
		        	
		        	children.add(node);
				}
			} catch (NotFound e) {
				// nincs jó választás
			}
        }
        
        public void grade() {
        	int numAliveClients = gridController.getNumAliveClients();
        	int numFloors = gridController.getGrid().getNumFloors();
        	
        	if (numAliveClients == 0) {
        		grade = -numFloors / 2.0; // TIE
        	} else if (numAliveClients == 1) {
        		Client winner = gridController.getGrid().getAliveClients().get(0);
        		if (winner.getId() == clientId) {
        			grade = (double) numFloors; // WIN
        		} else {
        			grade = (double) -numFloors; // LOSE
        		}
        	} else {
        		Client client = gridController.getGrid().getClient(clientId);
        		Client enemy = gridController.getGrid().getEnemy(clientId);
        		
        		List<Point> clientAccessableFloors = gridController.getGrid().
        				getAccessableFloors(client);
        		List<Point> enemyAccessableFloors = gridController.getGrid().
        				getAccessableFloors(enemy);
        		
        		boolean isSeparated = clientAccessableFloors.size() != 
        				enemyAccessableFloors.size();
        		
        		if (! isSeparated) {
        			// nincsenek elválasztva egymástól
        			double distance = client.getPosition().distance(enemy.getPosition());
        			// közelítsünk az ellenfélhez
        			grade = (double) numFloors / distance;
        		} else {
        			// el vannak választva
        			int difference = clientAccessableFloors.size() - enemyAccessableFloors.size();
        			// mennyivel ér el több mezőt mint az ellenfél
        			grade = (double) difference;
        		}
        	}
        }
        
        public void orderChildren() {
        	Collections.sort(children, new CompareNodes(isMyTurn));
        	for (Node child : children) {
        		child.orderChildren();
        	}
        }
        
        public Direction getDirection() {
        	return direction;
        }
        
        public Node getParent() {
        	return parent;
        }
        
        public List<Node> getChildren() {
        	return children;
        }
        
        private String print(String prefix, boolean isTail) {
        	String node = prefix + (isTail ? "'--- " : "|--- ") + 
            		(isMyTurn ? "Enemy " : "Client ") + "can go " + direction + "(" + grade + ")\n";
        	
            for (int i = 0; i < children.size() - 1; i++) {
                node += children.get(i).print(prefix + (isTail ? "    " : "|   "), false);
            }
            if (children.size() > 0) {
                node += children.get(children.size() - 1).print(
                		prefix + (isTail ? "    " : "|   "), true);
            }
            
            return node;
        }
        
    }
    
    private class CompareNodes implements Comparator<Node> {
    	
    	private short yes = -1;
    	private short no = 1;
    	
    	public CompareNodes(boolean isMyTurn) {
			if (! isMyTurn) {
				yes = 1;
				no = -1;
			}
		}
    	
		@Override
		public int compare(Node node1, Node node2) {
			if (node1.grade == null && node2.grade == null) {
				return 0;
			}
			if (node1.grade == null) {
				return 1;
			}
			if (node2.grade == null) {
				return -1;
			}
			return (node1.grade > node2.grade) ? yes : no;
		}
    }
}
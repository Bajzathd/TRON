package game.tron.utility;

import game.tron.client.Client;
import game.tron.grid.GridController;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.omg.CosNaming.NamingContextPackage.NotFound;

public class StepTree {
	
    private Node root;

    public StepTree(GridController gridController, int clientId) {
        root = new Node(null);
        root.isMyTurn = true;
        root.clientId = clientId;
        root.gridController = gridController.clone();
    }
    
    public Node getRoot() {
    	return root;
    }
    
    public void grade() {
    	evaluate(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    	root.orderChildren();
    	root.print("", true);
    }
    
    private int evaluate(Node node, int alpha, int beta) {
    	if (node.grade != null) {
    		return node.grade;
    	}
    	if (node.isMyTurn) {
    		for (Node child : node.children) {
    			beta = Math.min(beta, evaluate(child, alpha, beta));
    			if (beta <= alpha) {
    				node.grade = alpha;
        			return alpha;
        		}
    		}
    		node.grade = beta;
    		return beta;
    	} else {
    		for (Node child : node.children) {
    			alpha = Math.max(alpha, evaluate(child, alpha, beta));
    			if (beta <= alpha) {
    				node.grade = beta;
        			return beta;
        		}
    		}
    		node.grade = alpha;
    		return alpha;
    	}
    }
    
    public Direction getDirection() {
    	Direction direction = null;
    	int maxGrade = Integer.MIN_VALUE;
    	
    	for (Node child : root.getChildren()) {
    		if (child.grade > maxGrade) {
    			maxGrade = child.grade;
    			direction = child.direction;
    		}
    	}
    	
    	return direction;
    }
    
    public class Node {
        private Direction direction;
        private int clientId;
        private Node parent;
        private boolean isMyTurn;
        private List<Node> children = new ArrayList<Node>();
        
        public GridController gridController;
        public Integer grade = null;
        
        private Node(Direction direction) {
        	this.direction = direction;
        }
        
        public void setChildren() {
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
		        	
		        	children.add(node);
				}
			} catch (NotFound e) {
				// nincs jó választás
			}
        }
        
        public void grade() {
        	int numAliveClients = gridController.getNumAliveClients();
        	
        	if (numAliveClients == 0) {
        		System.out.println("TIE");
        		grade = -gridController.getGrid().getNumFloors() / 2; // TIE
        	} else if (numAliveClients == 1) {
        		Client winner = gridController.getGrid().getAliveClients().get(0);
        		if (winner.getId() == clientId) {
        			System.out.println("WIN");
        			grade = gridController.getGrid().getNumFloors(); // WIN
        		} else {
        			System.out.println("LOSE");
        			grade = -gridController.getGrid().getNumFloors(); // LOSE
        		}
        	} else {
        		Client client = gridController.getGrid().getClient(clientId);
        		Client enemy = gridController.getGrid().getEnemy(clientId);
        		
        		List<Point> clientAccessableFloors = gridController.getGrid().
        				getAccessableFloors(client.getPosition());
        		Point nextEnemyPosition = enemy.getDirection().getTranslatedPoint(enemy.getPosition());
        		
        		if (clientAccessableFloors.contains(nextEnemyPosition)) {
        			// nincsenek elválasztva egymástól
        			double distance = client.getPosition().distance(enemy.getPosition());
        			// közelítsünk az ellenfélhez
        			System.out.println("KÖZELÍT");
        			grade = (int) (gridController.getGrid().getNumFloors() / distance);
        		} else {
        			// el vannak választva
        			List<Point> enemyAccessableFloors = gridController.getGrid().
            				getAccessableFloors(enemy.getPosition());
        			// mennyivel ér el több mezőt mint az ellenfél
        			System.out.println("SZEPARÁLT");
        			grade = clientAccessableFloors.size() - enemyAccessableFloors.size();
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
        
        private void print(String prefix, boolean isTail) {
            System.out.println(prefix + (isTail ? "'--- " : "|--- ") + direction + "(" + grade + ")");
            for (int i = 0; i < children.size() - 1; i++) {
                children.get(i).print(prefix + (isTail ? "    " : "|   "), false);
            }
            if (children.size() > 0) {
                children.get(children.size() - 1).print(prefix + (isTail ?"    " : "|   "), true);
            }
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
			} else if (node1.grade == null) {
				return yes;
			} else if (node2.grade == null) {
				return no;
			}
			return (node1.grade > node2.grade) ? yes : no;
		}
    }
}
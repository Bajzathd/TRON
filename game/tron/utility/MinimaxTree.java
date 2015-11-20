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
	
    private State startState;

    public MinimaxTree(GridController gridController, int clientId) {
        startState = new State(null);
        startState.isMyTurn = true;
        startState.clientId = clientId;
        startState.gridController = gridController.clone();
    }
    
    public State getStartState() {
    	return startState;
    }
    
    public Direction getDirection() {
    	Direction direction = null;
    	double maxGrade = Integer.MIN_VALUE;
    	
    	evaluate(startState, -Double.MAX_VALUE, Double.MAX_VALUE);
    	startState.orderChildren();
    	
    	for (State child : startState.getChildren()) {
    		if (child.grade != null && child.grade > maxGrade) {
    			maxGrade = child.grade;
    			direction = child.direction;
    		}
    	}
    	
    	resetGrade(startState);
    	
    	return direction;
    }
    
    private void resetGrade(State state) {
    	state.grade = null;
    	for (State child : state.children) {
    		resetGrade(child);
    	}
    }
    
    /**
     * Alfa-béta vágás algoritmus
     * @param state állás
     * @param alpha legjobb kliens lépés értéke
     * @param beta legjobb ellenfél lépés értéke
     * @return állás értéke a kliens szemszögéből
     */
    private double evaluate(State state, double alpha, double beta) {
    	if (state.grade != null) {
    		return state.grade;
    	}
    	if (state.isMyTurn) {
    		for (State child : state.children) {
    			alpha = Math.max(alpha, evaluate(child, alpha, beta));
    			if (beta <= alpha) {
    				state.grade = beta;
        			return beta;
        		}
    		}
    		state.grade = alpha;
    		return alpha;
    	} else {
    		for (State child : state.children) {
    			beta = Math.min(beta, evaluate(child, alpha, beta));
    			if (beta <= alpha) {
    				state.grade = alpha;
        			return alpha;
        		}
    		}
    		state.grade = beta;
    		return beta;
    	}
    }
    
    public String toString() {
    	return startState.print("", true);
    }
    
    public class State {
    	
        private Direction direction;
        private int clientId;
        private State parent;
        private boolean isMyTurn;
        private List<State> children = new ArrayList<State>();
        
        public GridController gridController;
        public Double grade = null;
        
        private State(Direction direction) {
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
					State state = new State(direction);
					
		        	state.parent = this;
		        	state.clientId = clientId;
		        	state.isMyTurn = ! isMyTurn;
		        	state.gridController = gridController.clone();
		        	
		        	if (isMyTurn) {
						client = state.gridController.getGrid().getClient(clientId);
					} else {
						client = state.gridController.getGrid().getEnemy(clientId);
					}
		        	client.trySetDirection(direction);
		        	client.step();
		        	state.gridController.evaluate();
		        	
		        	children.add(state);
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
        	Collections.sort(children, new CompareStates(isMyTurn));
        	for (State child : children) {
        		child.orderChildren();
        	}
        }
        
        public Direction getDirection() {
        	return direction;
        }
        
        public State getParent() {
        	return parent;
        }
        
        public List<State> getChildren() {
        	return children;
        }
        
        private String print(String prefix, boolean isTail) {
        	String state = prefix + (isTail ? "'--- " : "|--- ") + 
            		(isMyTurn ? "Enemy " : "Client ") + "can go " + direction + "(" + grade + ")\n";
        	
            for (int i = 0; i < children.size() - 1; i++) {
                state += children.get(i).print(prefix + (isTail ? "    " : "|   "), false);
            }
            if (children.size() > 0) {
                state += children.get(children.size() - 1).print(
                		prefix + (isTail ? "    " : "|   "), true);
            }
            
            return state;
        }
        
    }
    
    private class CompareStates implements Comparator<State> {
    	
    	private short yes = -1;
    	private short no = 1;
    	
    	public CompareStates(boolean isMyTurn) {
			if (! isMyTurn) {
				yes = 1;
				no = -1;
			}
		}
    	
		@Override
		public int compare(State state1, State state2) {
			if (state1.grade == null && state2.grade == null) {
				return 0;
			}
			if (state1.grade == null) {
				return 1;
			}
			if (state2.grade == null) {
				return -1;
			}
			return (state1.grade > state2.grade) ? yes : no;
		}
    }
}
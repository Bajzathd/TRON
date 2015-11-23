package hu.tron.utility;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.omg.CosNaming.NamingContextPackage.NotFound;

import hu.tron.client.Client;
import hu.tron.grid.Grid;
import hu.tron.grid.GridController;

/**
 * Minimax játék fát reprezentáló osztály {@link State} típusú csúcsokkal
 * 
 * @author Dávid Bajzáth
 */
public class MinimaxTree {
	/*
	 * Minimax algoritmusra épülő AI stratégia alfa-béta vágással optimalizálva
	 */

	/**
	 * Az AI id-ja amihez készül a játékfa (tulajdonos)
	 */
	private int aiId;
	/**
	 * A játék jelenlegi állása, ahonnan indul a lehetséges állások generálása
	 */
	private State startState;
	/**
	 * Az az irány amely felé haladva a legmagasabb a nyerési esély
	 */
	private Direction bestDirection;

	/**
	 * Adott játékálláshoz inicializálja a játékfát az adott id-val jelölt AI
	 * szempontjából
	 * 
	 * @param grid
	 *            játékállás
	 * @param aiId
	 *            AI id-ja
	 */
	public MinimaxTree(Grid grid, int aiId, int depth) {
		this.aiId = aiId;
		startState = new State(grid);
		
		set(depth);
	}

	/**
	 * Kezdő állásból adott mélységig elkészíti a fát
	 * 
	 * @param depth
	 *            mélység
	 */
	private void set(int depth) {
		handleState(startState, depth);
		setBestDirection();
	}

	/**
	 * Adott állásból adott mélységig rekurzívan elkészíti a részfát
	 * 
	 * @param state
	 *            állás
	 * @param limit
	 *            mélység
	 */
	private void handleState(State state, int limit) {
		if (limit <= 0) { // elérte a megadott mélységet
			state.grade();
			return;
		}

		state.setChildren();

		if (state.children.isEmpty()) { // levélhez ért
			state.grade();
		} else {
			Iterator<State> it = state.children.iterator();

			// elkészíti a lehetséges állások részfáit 1-el kisebb mélységgel
			while (it.hasNext()) {
				handleState(it.next(), limit - 1);
			}
		}
	}

	/**
	 * Beállítja a jelenleg ismert legjobb lépés irányát
	 */
	private void setBestDirection() {
		double bestGrade = -Double.MAX_VALUE; // ismert legmagasabb értékelés

		evaluate(startState, -Double.MAX_VALUE, Double.MAX_VALUE);
		startState.orderChildren();

		for (State child : startState.children) {
			if (child.grade != null && child.grade > bestGrade) {
				
				// jobb lépést talált
				bestGrade = child.grade;
				bestDirection = child.direction;
			}
		}

		resetGrades(startState);
	}

	/**
	 * Null-ra állítja az adott csúcs gyökerű részfa minden csúcsának értékét
	 * 
	 * @param state
	 *            részfa gyökere
	 */
	private void resetGrades(State state) {
		state.grade = null;

		for (State child : state.children) {
			resetGrades(child);
		}
	}

	public State getStartState() {
		return startState;
	}

	public Direction getBestDirection() {
		return bestDirection;
	}

	/**
	 * Alfa-béta vágás algoritmus megvalósítása a játék fára
	 * 
	 * @param state
	 *            állás
	 * @param alpha
	 *            legjobb kliens lépés értéke
	 * @param beta
	 *            legjobb ellenfél lépés értéke
	 * @return állás értéke
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

	/**
	 * Egy állást reprezentáló csúcs a {@link MinimaxTree} osztályban
	 * 
	 * @author Dávid Bajzáth
	 */
	private class State {

		/**
		 * Az irány amerre lépve a szülő állásból ebbe jutott
		 */
		protected Direction direction;
		/**
		 * A fában beállított kliens következik-e
		 */
		protected boolean isMyTurn;
		/**
		 * Az állásból egy lépéssel elérhető állások listája
		 */
		protected List<State> children = new ArrayList<State>();
		/**
		 * A játék jelenlegi állása
		 */
		protected Grid grid;
		/**
		 * Az állás értékelése
		 */
		protected Double grade = null;

		/**
		 * Kezdő állás (fa gyökere) inicializálása
		 * 
		 * @param grid
		 *            kezdő állás
		 */
		public State(Grid grid) {
			this.grid = grid.clone();
			isMyTurn = true;
		}

		/**
		 * Állás inicializálása
		 * 
		 * @param parent
		 *            szülő állás
		 * @param direction
		 *            irány amerre lépve kaptuk ezt az állást a szülőből
		 */
		public State(State parent, Direction direction) {
			Client client;

			this.direction = direction;

			/*
			 * Szülő állásból a megadott irányba lépteti a szülő körében soron
			 * következő klienst hogy elérje a jelnlegi kör állását
			 */

			isMyTurn = !parent.isMyTurn;
			grid = parent.grid.clone();

			client = (parent.isMyTurn) ? grid.getClient(aiId) : grid
					.getEnemy(aiId);

			client.trySetDirection(direction);
			client.step();

			new GridController(grid).evaluate();
		}

		/**
		 * Beállítja az elérhető állásokat
		 */
		public void setChildren() {
			if (!children.isEmpty()) { // már be lettek állítva
				return;
			}

			List<Direction> validDirections;
			Client client = (isMyTurn)
					? grid.getClient(aiId)
					: grid.getEnemy(aiId);

			try {
				validDirections = grid.getValidDirections(client.getPosition());

				for (Direction direction : validDirections) {
					children.add(new State(this, direction));
				}
			} catch (NotFound e) {}
		}

		/**
		 * Értékeli az állást
		 */
		public void grade() {
			int numAliveClients = grid.getAliveClients().size();
			int numFloors = grid.getNumFloors();

			if (numAliveClients == 0) {
				grade = -numFloors / 2.0; // TIE
			} else if (numAliveClients == 1) {
				Client winner = grid.getAliveClients().get(0);

				grade = (winner.getId() == aiId) ? (double) numFloors // WIN
						: (double) -numFloors; // LOSE
			} else {
				Client client = grid.getClient(aiId);
				Client enemy = grid.getEnemy(aiId);
				List<Point> accessableFloors = grid.getAccessableFloors(client);

				/*
				 * ha az ellenfél pozíciójának valamelyik szomszédját eléri 
				 * akkor nincs elszeparálva
				 */
				boolean isSeparated = true;
				for (Direction d : Direction.values()) {
					if (accessableFloors.contains(d.getTranslatedPoint(
							enemy.getPosition()))) {
						isSeparated = false;
						break;
					}
				}

				if (!isSeparated) {
					double distance = client.getPosition().distance(
							enemy.getPosition());

					/*
					 * minnél közelebb van hozzá (csak heurisztikus távolságot
					 * mér, légvonalban milyen távol vannak egymástól a
					 * kliensek)
					 */
					grade = (double) numFloors / distance;
				} else {
					int difference = accessableFloors.size() - 
							(numFloors - accessableFloors.size());

					// mennyivel ér el több mezőt mint az ellenfél
					grade = (numFloors / 2.0) + difference;
				}
			}
		}

		/**
		 * Értékük szerint rendezi az elérhető állásokat, annak függvényében
		 * hogy melyik kliens következik
		 */
		public void orderChildren() {
			Collections.sort(children, new CompareStates(isMyTurn));

			for (State child : children) {
				child.orderChildren();
			}
		}

		/**
		 * Adott állásból kiinduló részfa String reprezentációja
		 */
		public String toString() {
			return print("", false);
		}

		/**
		 * Részfa String-jét generáló rekurzív algoritmus
		 * 
		 * @param prefix
		 *            ősökből örökölt String prefix
		 * @param isLeaf
		 *            levél-e
		 * @return
		 */
		public String print(String prefix, boolean isLeaf) {
			String state = prefix + (isLeaf ? "'--- " : "|--- ")
					+ (isMyTurn ? "Enemy " : "Client ") + "can go " + direction
					+ "(" + grade + ")\n";

			for (int i = 0; i < children.size() - 1; i++) {
				state += children.get(i).print(
						prefix + (isLeaf ? "    " : "|   "), false);
			}
			if (children.size() > 0) {
				state += children.get(children.size() - 1).print(
						prefix + (isLeaf ? "    " : "|   "), true);
			}

			return state;
		}

	}

	/**
	 * {@link State} objektumokat összehasonlító osztály
	 * 
	 * @author Dávid Bajzáth
	 */
	private class CompareStates implements Comparator<State> {
		/**
		 * Nagyobb értékű állás esetén visszaadott érték
		 */
		private int greater;
		/**
		 * Nemnagyobb értékű állás esetén visszaadott érték
		 */
		private int less;

		/**
		 * Konstruktor az összehasonlításhoz
		 * 
		 * @param isMyTurn
		 *            a fa tulajdonosának köre-e
		 */
		public CompareStates(boolean isMyTurn) {
			greater = (isMyTurn) ? -1 : 1;
			less = -greater;
		}

		/**
		 * Összehasonlítja két {@link State} objektum értékét
		 */
		@Override
		public int compare(State state1, State state2) {

			/*
			 * Nem veszi figyelembe az egyenlőséget. A null-ok hátrébb kerülnek
			 * minden rendezési sorrendben.
			 */
			if (state1.grade == null) {
				return 1;
			}
			if (state2.grade == null) {
				return -1;
			}
			return (state1.grade > state2.grade) ? greater : less;
		}
	}

}
package dai.llew.game;

import dai.llew.ui.CellPosition;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by daiLlew on 23/01/2016.
 */
public class Player {

	/**
	 * Types to distinguish which player is playing.
	 */
	public enum PlayerType {
		/**
		 * A Human player.
		 */
		HUMAN,

		/**
		 * The computer AI.
		 */
		COMPUTER
	}

	public enum Symbol {
		/**
		 * The Noughts Symbol.
		 */
		NOUGHTS,

		/**
		 * The Crosses Symbol.
		 */
		CROSSES;

		public static final int WIDTH = 60;
	}

	private Set<CellPosition> occupiedCells;
	private final PlayerType playerType;
	private final Symbol symbol;

	public Player(PlayerType playerType, Symbol symbol) {
		this.playerType = playerType;
		this.occupiedCells = new HashSet<>();
		this.symbol = symbol;
	}

	public PlayerType getPlayerType() {
		return playerType;
	}

	public Symbol getSymbol() {
		return symbol;
	}

	public Set<CellPosition> getOccupiedCells() {
		return occupiedCells;
	}
}

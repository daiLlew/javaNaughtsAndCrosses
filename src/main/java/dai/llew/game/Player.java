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

	private Set<CellPosition> occupiedCells;
	private PlayerType playerType;

	public Player(PlayerType playerType) {
		this.playerType = playerType;
		this.occupiedCells = new HashSet<>();
	}

	public PlayerType getPlayerType() {
		return playerType;
	}

	public Set<CellPosition> getOccupiedCells() {
		return occupiedCells;
	}
}

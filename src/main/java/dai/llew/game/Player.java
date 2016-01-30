package dai.llew.game;

import dai.llew.ui.CellPosition;

import java.util.HashSet;
import java.util.Set;

import static dai.llew.game.GameConstants.PlayerType;
import static dai.llew.game.GameConstants.Symbol;

public class Player {

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

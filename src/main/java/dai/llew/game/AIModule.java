package dai.llew.game;

import dai.llew.ui.Board;
import dai.llew.ui.BoardCell;
import dai.llew.ui.CellPosition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static dai.llew.game.Player.Symbol;
import static dai.llew.ui.CellPosition.BOTTOM_LEFT;
import static dai.llew.ui.CellPosition.BOTTOM_MID;
import static dai.llew.ui.CellPosition.BOTTOM_RIGHT;
import static dai.llew.ui.CellPosition.MID_LEFT;
import static dai.llew.ui.CellPosition.MID_MID;
import static dai.llew.ui.CellPosition.MID_RIGHT;
import static dai.llew.ui.CellPosition.TOP_LEFT;
import static dai.llew.ui.CellPosition.TOP_MID;
import static dai.llew.ui.CellPosition.TOP_RIGHT;

/**
 * Created by daiLlew on 24/01/2016.
 */
public class AIModule {

	List<List<CellPosition>> winningCombinations = null;

	private static AIModule instance = null;

	public static AIModule getInstance() {
		if (instance == null) {
			instance = new AIModule();
		}
		return instance;
	}

	private AIModule() {
		// All possible winning combinations
		winningCombinations = new ArrayList<>();
		winningCombinations.add(Arrays.asList(new CellPosition[]{TOP_LEFT, TOP_MID, TOP_RIGHT}));
		winningCombinations.add(Arrays.asList(new CellPosition[]{MID_LEFT, MID_MID, MID_RIGHT}));
		winningCombinations.add(Arrays.asList(new CellPosition[]{BOTTOM_LEFT, BOTTOM_MID, BOTTOM_RIGHT}));
		winningCombinations.add(Arrays.asList(new CellPosition[]{TOP_LEFT, MID_LEFT, BOTTOM_LEFT}));
		winningCombinations.add(Arrays.asList(new CellPosition[]{TOP_MID, MID_MID, BOTTOM_MID}));
		winningCombinations.add(Arrays.asList(new CellPosition[]{TOP_RIGHT, MID_RIGHT, BOTTOM_RIGHT}));
		winningCombinations.add(Arrays.asList(new CellPosition[]{TOP_LEFT, MID_MID, BOTTOM_RIGHT}));
		winningCombinations.add(Arrays.asList(new CellPosition[]{TOP_RIGHT, MID_MID, BOTTOM_LEFT}));
	}

	private CellPosition findWinningMove(Board board, Symbol opponentSymbol) {
		for (List<CellPosition> combination : winningCombinations) {

			List<BoardCell> currentValues = getCurrentValues(board, combination);

			long opponentOccupiedCount = currentValues.stream()
					.filter(boardCell -> boardCell.isFilled() && opponentSymbol.equals(boardCell.getSymbol()))
					.count();

			if (opponentOccupiedCount == 2) {
				List<BoardCell> freeCells = currentValues.stream()
						.filter(boardCell -> !boardCell.isFilled() && !opponentSymbol.equals(boardCell.getSymbol()))
						.collect(Collectors.toList());

				if (freeCells.size() == 0) {
					// No free cells skip to next combination.
					continue;
				}
				return freeCells.get(0).getPosition();
			}
		}
		return null;
	}

	private List<BoardCell> getCurrentValues(Board board, List<CellPosition> positions) {
		List<BoardCell> currentValues = new ArrayList<>();
		positions.stream()
				.forEach(cellPosition -> currentValues.add(board.getCell(cellPosition)));

		return currentValues;
	}

	/**
	 * Check if the player has 3 cells in a row.
	 */
	public boolean isWinner(Board board, Player player) {
		for (List<CellPosition> combination : winningCombinations) {
			if (3 == combination.stream()
					.filter(cellPosition -> player.getSymbol().equals(board.getCell(cellPosition).getSymbol()))
					.count()) {
				return true;
			}
		}
		return false;
	}

	public boolean isDraw(Board board) {
		return board.getCells().values().stream().filter(boardCell -> !boardCell.isFilled()).count() == 0;
	}

	public void takeTurn(Board board, Player computerPlayer, Player opponent) throws InterruptedException {
		// Defend first... if the player can win with their next move block it.
		CellPosition nextMove = findWinningMove(board, opponent.getSymbol());

		if (nextMove == null) {
			// Otherwise can the computer win with its next move?
			nextMove = findWinningMove(board, computerPlayer.getSymbol());
		}

		if (nextMove == null) {
			// Choose a random cell.
			List<BoardCell> available = board.getCells().values()
					.stream()
					.filter(cell -> !cell.isFilled())
					.collect(Collectors.toList());

			Collections.shuffle(available);
			nextMove = available.get(0).getPosition();
		}
		Thread.sleep(1000);
		board.getCell(nextMove).fill(computerPlayer.getSymbol());
	}
}

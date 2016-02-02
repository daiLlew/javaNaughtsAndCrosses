package dai.llew.game;

import dai.llew.ui.BoardCell;
import dai.llew.ui.CellPosition;
import dai.llew.ui.StrikeLine;
import dai.llew.ui.views.BoardView;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static dai.llew.game.GameConstants.Symbol;
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
 * Provides the AI for the computers turn and other calculations required by the {@link GameManager}.
 */
public class AIModule {

	private List<List<CellPosition>> winningCombinations = null;
	private List<List<CellPosition>> horizontals = null;
	private List<List<CellPosition>> verticals = null;
	private List<List<CellPosition>> diagonalTopLeft = null;
	private List<List<CellPosition>> diagonalTopRight = null;

	private static AIModule instance = null;

	/**
	 * @return a Singleton instance of the {@link AIModule}.
	 */
	public static AIModule getInstance() {
		if (instance == null) {
			instance = new AIModule();
		}
		return instance;
	}

	/**
	 * Construct a new AIModule.
	 */
	private AIModule() {
		horizontals = new ArrayList<>();
		horizontals.add(Arrays.asList(new CellPosition[]{TOP_LEFT, TOP_MID, TOP_RIGHT}));
		horizontals.add(Arrays.asList(new CellPosition[]{MID_LEFT, MID_MID, MID_RIGHT}));
		horizontals.add(Arrays.asList(new CellPosition[]{BOTTOM_LEFT, BOTTOM_MID, BOTTOM_RIGHT}));

		verticals = new ArrayList<>();
		verticals.add(Arrays.asList(new CellPosition[]{TOP_LEFT, MID_LEFT, BOTTOM_LEFT}));
		verticals.add(Arrays.asList(new CellPosition[]{TOP_MID, MID_MID, BOTTOM_MID}));
		verticals.add(Arrays.asList(new CellPosition[]{TOP_RIGHT, MID_RIGHT, BOTTOM_RIGHT}));

		diagonalTopLeft = new ArrayList<>();
		diagonalTopLeft.add(Arrays.asList(new CellPosition[]{TOP_LEFT, MID_MID, BOTTOM_RIGHT}));

		diagonalTopRight = new ArrayList<>();
		diagonalTopRight.add(Arrays.asList(new CellPosition[]{BOTTOM_LEFT, MID_MID, TOP_RIGHT}));

		winningCombinations = new ArrayList<>();
		winningCombinations.addAll(horizontals);
		winningCombinations.addAll(verticals);
		winningCombinations.addAll(diagonalTopLeft);
		winningCombinations.addAll(diagonalTopRight);
	}

	/**
	 * Checks if it is possible for the game to be won in the next move. There are 8 possible ways to win the game:
	 * <ul>
	 * <li>Top horizontal.</li>
	 * <li>Middle horizontal.</li>
	 * <li>Bottom horizontal.</li>
	 * <li>Left veritcal.</li>
	 * <li>Middle veritcal.</li>
	 * <li>Right vertical.</li>
	 * <li>Diagonal top left to bottom right.</li>
	 * <li>Diagonal top right to bottom left.</li>
	 * </ul>
	 * <p>
	 * Each of these potential <i>winning combinations</i> is checked to see if any exits that contain 2
	 * {@link BoardCell}'s with opponent's {@Link Symbol} and one blank {@link BoardCell}. If <b>Yes</b> then the blank
	 * cell is chosen as either the <i>winning move</i> or a <i>win preventing</i> move. If the above does not exist
	 * then no winning move exists at this time.
	 *
	 * @param boardView
	 * @param opponentSymbol
	 * @return
	 */
	private Optional<CellPosition> findWinningMove(BoardView boardView, Symbol opponentSymbol) {
		for (List<CellPosition> combination : winningCombinations) {

			List<BoardCell> currentValues = getCurrentValues(boardView, combination);

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
				return Optional.of(freeCells.get(0).getPosition());
			}
		}
		return Optional.empty();
	}

	private List<BoardCell> getCurrentValues(BoardView boardView, List<CellPosition> positions) {
		List<BoardCell> currentValues = new ArrayList<>();
		positions.stream()
				.forEach(cellPosition -> currentValues.add(boardView.getCell(cellPosition)));

		return currentValues;
	}

	/**
	 * Check if the player has 3 cells in a row.
	 */
	public Optional<List<CellPosition>> isWinner(BoardView boardView, Player player) {
		for (List<CellPosition> combination : winningCombinations) {
			if (3 == combination.stream()
					.filter(cellPosition -> player.getSymbol().equals(boardView.getCell(cellPosition).getSymbol()))
					.count()) {
				return Optional.of(combination);
			}
		}
		return Optional.empty();
	}

	public boolean isDraw(BoardView boardView) {
		return boardView.getCells().values().stream().filter(boardCell -> !boardCell.isFilled()).count() == 0;
	}

	/**
	 * The AI for the computer player's turn.
	 * <p>First checks if the computer can win with its next move. If yes then it will choose that {@link BoardCell} for
	 * it's next move.</p>
	 * <p>If the computer cannot win the game with it's next move it will check if the opponent can with with their next
	 * move. If yes then it will block the opponents victory and choose the opponents winning {@link BoardCell} as its
	 * next move.</p>
	 * <p>Otherwise the computer will pick a free {@link BoardCell} at random.</p>
	 *
	 * @param boardView
	 * @param computerPlayer
	 * @param opponent
	 * @throws InterruptedException
	 */
	public void takeTurn(BoardView boardView, Player computerPlayer, Player opponent) throws InterruptedException {
		// Can the computer win with its next move?
		Optional<CellPosition> nextMove = findWinningMove(boardView, computerPlayer.getSymbol());

		if (!nextMove.isPresent()) {
			// Otherwise block the player if they can win with their next move.
			nextMove = findWinningMove(boardView, opponent.getSymbol());
		}

		if (!nextMove.isPresent()) {
			// Choose a random cell.
			List<BoardCell> available = boardView.getCells().values()
					.stream()
					.filter(cell -> !cell.isFilled())
					.collect(Collectors.toList());

			Collections.shuffle(available);
			nextMove = Optional.of(available.get(0).getPosition());
		}
		Thread.sleep(1000);
		boardView.getCell(nextMove.get()).fill(computerPlayer.getSymbol());
	}

	/**
	 * Calculate the correct start and end X Y coordinates of for the winners strike through line based on the
	 * orientation of winning combination.
	 *
	 * @param winningCombo the combination of cells that won the game.
	 * @return a {@link StrikeLine} containing the coordinate data required to draw the line.
	 */
	public StrikeLine getStrikeLine(List<CellPosition> winningCombo) {
		int startX, startY, endX, endY;
		CellPosition startCell = winningCombo.get(0);
		CellPosition endCell = winningCombo.get(2);

		if (horizontals.contains(winningCombo)) {
			startX = startCell.getX();
			startY = startCell.getY() + startCell.getHeight() / 2;

			endX = endCell.getX() + endCell.getWidth();
			endY = startCell.getY() + startCell.getHeight() / 2;
		} else if (verticals.contains(winningCombo)) {
			startX = startCell.getX() + startCell.getWidth() / 2;
			startY = startCell.getY();

			endX = endCell.getX() + endCell.getWidth() / 2;
			endY = endCell.getY() + endCell.getHeight();
		} else if (diagonalTopLeft.contains(winningCombo)) {
			startX = startCell.getX();
			startY = startCell.getY();
			endX = endCell.getX() + endCell.getWidth();
			endY = endCell.getY() + endCell.getHeight();
		} else {
			startX = startCell.getX();
			startY = startCell.getY() + startCell.getHeight();
			endX = endCell.getX() + endCell.getWidth();
			endY = endCell.getY();
		}
		return new StrikeLine(new Point(startX, startY), new Point(endX, endY));
	}
}

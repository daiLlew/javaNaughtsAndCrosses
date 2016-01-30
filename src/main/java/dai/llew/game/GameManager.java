package dai.llew.game;

import dai.llew.ui.Board;
import dai.llew.ui.CellPosition;
import dai.llew.ui.GUI;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static dai.llew.game.GameConstants.GameStatus;
import static dai.llew.game.GameConstants.GameStatus.DRAWN;
import static dai.llew.game.GameConstants.GameStatus.IN_PLAY;
import static dai.llew.game.GameConstants.GameStatus.WON;
import static dai.llew.game.GameConstants.PlayerType.COMPUTER;
import static dai.llew.game.GameConstants.PlayerType.HUMAN;
import static dai.llew.game.GameConstants.Symbol.CROSSES;
import static dai.llew.game.GameConstants.Symbol.NOUGHTS;

/**
 * Game manager is responsible:
 * <ul>
 * <li>Running the game</li>
 * <li>Tracking which player's turn it is.</li>
 * <li>Checking if there is a winner.</li>
 * </ul>
 */
public class GameManager {

	private GUI gui;
	private Player currentPlayer;
	private Player humanPlayer = new Player(HUMAN, NOUGHTS);
	private Player computerPlayer = new Player(COMPUTER, CROSSES);
	private Board board;
	private AIModule aiModule;
	private GameStatus gameStatus = IN_PLAY;
	private List<CellPosition> winningCombination;

	/**
	 * Create a new GameManager.
	 */
	public GameManager() {
		this.currentPlayer = humanPlayer;
		this.aiModule = AIModule.getInstance();
		final Supplier<Player> getCurrentPlayer = () -> this.getCurrentPlayer();
		final Runnable turnCompleted = () -> checkGameState();
		this.board = new Board.Builder()
				.currentPlayerSupplier(getCurrentPlayer)
				.turnCompleted(turnCompleted)
				.build();
		this.gui = new GUI(board);
	}

	/**
	 * @return the {@link Player} who's turn it is.
	 */
	public Player getCurrentPlayer() {
		return this.currentPlayer;
	}

	/**
	 * Updates which {@link Player}'s turn it is.
	 */
	public void updatePlayer() {
		this.currentPlayer = currentPlayer.getPlayerType().equals(HUMAN) ? computerPlayer : humanPlayer;
	}

	public void play() {
		playGame();
	}

	/**
	 * The main <i>Game Loop</i> orchestrating the running of the game.
	 */
	private void playGame() {
		Thread gameThread = new Thread(() -> {
			while (gameStatus.equals(IN_PLAY)) {
				try {
					this.board.repaint();
					if (currentPlayer.getPlayerType().equals(COMPUTER)) {
						aiModule.takeTurn(board, computerPlayer, humanPlayer);
						checkGameState();
					} else {
						Thread.sleep(100);
					}
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
			if (gameStatus.equals(WON)) {
				//board.disableMouseListeners();
				highlightWinningCells();
				this.board.repaint();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
		});
		gameThread.start();
	}

	/**
	 * <p>Check if the current {@link Player} has won (has 3 occupied cells in a line). If so updates the {@link GameStatus}
	 * to {@link GameStatus#WON} and saves the winning cell combination.</p>
	 * <p>If there is not yet a winner check if it is a draw. If so updates the {@link GameStatus}
	 * to {@link GameStatus#DRAWN}.</p>
	 * <p>Else update the current player.</p>
	 */
	private void checkGameState() {
		Optional<List<CellPosition>> winningCombination = aiModule.isWinner(board, currentPlayer);
		if (winningCombination.isPresent()) {
			this.gameStatus = WON;
			this.winningCombination = winningCombination.get();
		} else if (aiModule.isDraw(board)) {
			this.gameStatus = DRAWN;
		} else {
			updatePlayer();
		}
	}

	/**
	 * Add a highlight to each of the winning {@link dai.llew.ui.BoardCell}'s to show the user game is won.
	 */
	private void highlightWinningCells() {
		winningCombination.stream().forEach(cellPosition -> board.getCell(cellPosition).winningCell());
	}

	public static void main(String[] args) throws Exception {
		new GameManager().play();
	}
}

package dai.llew.game;

import dai.llew.ui.views.BoardView;
import dai.llew.ui.CellPosition;
import dai.llew.ui.GUI;
import dai.llew.ui.views.GameCompletedView;
import dai.llew.ui.views.WelcomeView;

import java.util.List;
import java.util.Optional;

import static dai.llew.game.GameConstants.GameStatus;
import static dai.llew.game.GameConstants.GameStatus.DRAWN;
import static dai.llew.game.GameConstants.GameStatus.GAME_OVER;
import static dai.llew.game.GameConstants.GameStatus.IN_PLAY;
import static dai.llew.game.GameConstants.GameStatus.START_MENU;
import static dai.llew.game.GameConstants.GameStatus.WON;
import static dai.llew.game.GameConstants.PlayerType.COMPUTER;
import static dai.llew.game.GameConstants.PlayerType.HUMAN;
import static dai.llew.game.GameConstants.Symbol;
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
public class GameManager implements GameHelper {

	private GUI gui;
	private Player currentPlayer;
	private Player humanPlayer;
	private Player computerPlayer;
	private Optional<Player> winner = Optional.empty();
	private BoardView boardView;
	private AIModule aiModule;
	private GameStatus gameStatus = START_MENU;
	private List<CellPosition> winningCombination;

	/**
	 * Run the game.
	 */
	public static void main(String[] args) throws Exception {
		new GameManager().startGame();
	}

	/**
	 * Create a new GameManager.
	 */
	public GameManager() {
		this.currentPlayer = humanPlayer;
		this.aiModule = AIModule.getInstance();
		boardView = new BoardView(this);
		WelcomeView view = new WelcomeView(this);
		this.gui = new GUI(view);
	}

	private void execute(Runnable instructions) {
		new Thread(instructions).start();
	}

	/**
	 * Start a new game.
	 */
	public void startGame() {
		execute(() -> {
			while (gameStatus.equals(START_MENU)) {
				gui.getMainFrame().repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	/**
	 * The main <i>Game Loop</i>.
	 */
	public void playGame() throws Exception {
		execute(() -> {
			try {
				while (gameStatus.equals(IN_PLAY)) {
					gameInPlay();
				}
				if (gameStatus.equals(WON)) {
					gameWon();
				}
				if (gameStatus.equals(DRAWN)) {
					gameDrawn();
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		});
	}

	public void symbolSelected(Symbol playerSymbol) {
		try {
			Thread.sleep(500);

			switch (playerSymbol) {
				case NOUGHTS:
					this.humanPlayer = new Player(HUMAN, NOUGHTS);
					this.computerPlayer = new Player(COMPUTER, CROSSES);
					this.currentPlayer = humanPlayer;
					break;
				default:
					this.computerPlayer = new Player(COMPUTER, NOUGHTS);
					this.humanPlayer = new Player(HUMAN, CROSSES);
					this.currentPlayer = computerPlayer;
			}

			newGame();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void newGame() throws Exception {
		gameStatus = IN_PLAY;
		boardView = new BoardView(this);
		gui.updateDisplay(boardView);
		winner = Optional.empty();
		this.playGame();
	}

	@Override
	public Optional<Player> getWinner() {
		return winner;
	}

	/**
	 * @return the {@link Player} who's turn it is.
	 */
	@Override
	public Player getCurrentPlayer() {
		return this.currentPlayer;
	}

	@Override
	public Player getHumanPlayer() {
		return humanPlayer;
	}

	@Override
	public Player getComputerPlayer() {
		return computerPlayer;
	}

	/**
	 * Updates which {@link Player}'s turn it is.
	 */
	private void updatePlayer() {
		currentPlayer = currentPlayer.getPlayerType().equals(HUMAN) ? computerPlayer : humanPlayer;
	}

	private void gameInPlay() throws InterruptedException {
		gui.getMainFrame().repaint();
		if (currentPlayer.getPlayerType().equals(COMPUTER)) {
			aiModule.takeTurn(boardView, computerPlayer, humanPlayer);
			turnCompleted();
		} else {
			Thread.sleep(100);
		}
	}

	private void gameWon() throws InterruptedException {
		winner = Optional.of(currentPlayer);
		strikeThroughWinningCells();
		gui.getMainFrame().repaint();
		Thread.sleep(1500);
		gameStatus = GAME_OVER;
		gameOver();
	}

	private void gameDrawn() throws InterruptedException {
		gui.getMainFrame().repaint();
		Thread.sleep(1500);
		gameStatus = GAME_OVER;
		gameOver();
	}

	private void gameOver() {
		gui.updateDisplay(new GameCompletedView(this));
		execute(() -> {
			while (gameStatus.equals(GAME_OVER)) {
				gui.getMainFrame().repaint();
				try {
					Thread.sleep(100);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	/**
	 * <p>Check if the current {@link Player} has won (has 3 occupied cells in a line). If so updates the {@link GameStatus}
	 * to {@link GameStatus#WON} and saves the winning cell combination.</p>
	 * <p>If there is not yet a winner check if it is a draw. If so updates the {@link GameStatus}
	 * to {@link GameStatus#DRAWN}.</p>
	 * <p>Else update the current player.</p>
	 */
	public void turnCompleted() {
		Optional<List<CellPosition>> winningCombination = aiModule.isWinner(boardView, currentPlayer);
		if (winningCombination.isPresent()) {
			this.gameStatus = WON;
			this.winningCombination = winningCombination.get();
		} else if (aiModule.isDraw(boardView)) {
			this.gameStatus = DRAWN;
		} else {
			updatePlayer();
		}
	}

	/**
	 * Add a highlight to each of the winning {@link dai.llew.ui.BoardCell}'s to show the user game is won.
	 */
	private void strikeThroughWinningCells() {
		boardView.setStrike(aiModule.getStrikeLine(winningCombination));
	}
}

package dai.llew.game;

import dai.llew.ui.Board;
import dai.llew.ui.GUI;

import java.awt.Dimension;
import java.util.function.Supplier;

import static dai.llew.game.Player.PlayerType.COMPUTER;
import static dai.llew.game.Player.PlayerType.HUMAN;
import static dai.llew.game.Player.Symbol.CROSSES;
import static dai.llew.game.Player.Symbol.NOUGHTS;

public class GameManager {

	private GUI gui;
	private Player currentPlayer;
	private Player humanPlayer = new Player(HUMAN, NOUGHTS);
	private Player computerPlayer = new Player(COMPUTER, CROSSES);
	private Board board;
	private AIModule aiModule;
	private final Dimension gameDimensions = new Dimension(550, 550);
	private Outcome gameState = Outcome.IN_PLAY;

	public GameManager() {
		this.currentPlayer = humanPlayer;

		final Supplier<Player> getCurrentPlayer = () -> this.getCurrentPlayer();

		final Runnable turnCompleted = () -> {
			checkWinner();
		};

		this.board = new Board(gameDimensions, getCurrentPlayer, turnCompleted);
		this.gui = new GUI(board);
		this.aiModule = AIModule.getInstance();
	}

	public Player getCurrentPlayer() {
		return this.currentPlayer;
	}

	public void updatePlayer() {
		this.currentPlayer = currentPlayer.getPlayerType().equals(HUMAN) ? computerPlayer : humanPlayer;
	}

	public Outcome play() {
		Thread gameThread = new Thread(() -> {
			while (gameState.equals(Outcome.IN_PLAY)) {
				this.board.repaint();
				try {
					if (this.currentPlayer.getPlayerType().equals(COMPUTER)) {
						aiModule.takeTurn(board, computerPlayer, humanPlayer);
						checkWinner();
					} else {
						Thread.sleep(50);
					}
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		});
		gameThread.start();
		return this.gameState;
	}

	private void checkWinner() {
		if (aiModule.isWinner(board, currentPlayer)) {
			this.gameState = Outcome.WON;
		}
		else if (aiModule.isDraw(board)) {
			this.gameState = Outcome.DRAW;
		} else {
			updatePlayer();
		}
	}

	public static void main(String[] args) throws Exception {
		GameManager gm = new GameManager();
		Outcome outcome = gm.play();
		if (outcome.equals(Outcome.WON)) {
			System.out.println(gm.getCurrentPlayer().getPlayerType().name() + " Wins!");
		} else {
			System.out.println("Draw.");
		}
	}
}

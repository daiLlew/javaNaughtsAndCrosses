package dai.llew.game;

import dai.llew.ui.Board;
import dai.llew.ui.GUI;

import java.awt.Dimension;

import static dai.llew.game.Player.PlayerType.COMPUTER;
import static dai.llew.game.Player.PlayerType.HUMAN;
import static dai.llew.game.Player.Symbol.CROSSES;
import static dai.llew.game.Player.Symbol.NAUGHTS;

public class GameManager {

	private GUI gui;
	private Player currentPlayer;
	private Player humanPlayer = new Player(HUMAN, NAUGHTS);
	private Player computerPlayer = new Player(COMPUTER, CROSSES);
	private Board board;
	private AIModule ai;
	private final Dimension gameDimensions = new Dimension(550, 550);

	public GameManager() {
		this.currentPlayer = humanPlayer;
		this.board = new Board(gameDimensions,
				() -> getCurrentPlayer(),
				(position) -> {
					updatePlayer();
				});
		this.gui = new GUI(board);
		this.ai = AIModule.getInstance();
	}

	public Player getCurrentPlayer() {
		return this.currentPlayer;
	}

	public void updatePlayer() {
		this.currentPlayer = currentPlayer.getPlayerType().equals(HUMAN) ? computerPlayer : humanPlayer;
	}

	public void play() {
		Thread gameThread = new Thread(() -> {
			while (true) {
				this.board.repaint();
				try {
					if (this.currentPlayer.getPlayerType().equals(COMPUTER)) {
						ai.takeTurn(board, computerPlayer);
						updatePlayer();
					} else {
						Thread.sleep(50);
					}
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		});
		gameThread.start();
	}

	public static void main(String[] args) throws Exception {
		new GameManager().play();
	}
}

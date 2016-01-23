package dai.llew.game;

import dai.llew.ui.Board;
import dai.llew.ui.CellPosition;
import dai.llew.ui.GUI;

import java.awt.Dimension;
import static dai.llew.game.Player.PlayerType.HUMAN;
import static dai.llew.game.Player.PlayerType.COMPUTER;
import dai.llew.game.Player.PlayerType;

public class GameManager {

	private GUI gui;
	private PlayerType currentPlayer;
	private Board board;
	private final Dimension gameDimensions = new Dimension(550, 550);

	public GameManager() {
		this.currentPlayer = HUMAN;
		this.board = new Board(gameDimensions,
				() -> getCurrentPlayer(),
				(position) -> {
					updatePlayer(position);
				});
		this.gui = new GUI(board);
	}

	public PlayerType getCurrentPlayer() {
		return this.currentPlayer;
	}

	public void updatePlayer(CellPosition position) {
		System.out.println(currentPlayer.name() + " chose " + position.name());
		switch (getCurrentPlayer()) {
			case HUMAN:
				this.currentPlayer = COMPUTER;
				break;
			default:
				this.currentPlayer = HUMAN;
		}
	}

	public void play() {
		Thread gameThread = new Thread(() -> {
			while (true) {
				this.board.repaint();
				try {
					if (this.currentPlayer.equals(COMPUTER)) {
						computerTurn();
					} else {
						Thread.sleep(500);
					}
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		});
		gameThread.start();
	}

	private void computerTurn() throws InterruptedException {
		System.out.println("computer turn so sleeping for a bit.");
		Thread.sleep(3000);
		System.out.println("Awake... now its players turn");
		updatePlayer(CellPosition.BOTTOM_LEFT);
	}

	public static void main(String[] args) throws Exception {
		new GameManager().play();
	}
}

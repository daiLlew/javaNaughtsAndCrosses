package dai.llew.ui.views;

import dai.llew.game.GameHelper;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import static dai.llew.game.GameConstants.HIGHLIGHT_COLOR;
import static dai.llew.ui.CellPosition.MID_LEFT;
import static dai.llew.ui.CellPosition.MID_RIGHT;
import static dai.llew.ui.CellPosition.TOP_MID;

/**
 * Screen displayed after a game has been completed.
 */
public class GameCompletedView extends GameView {

	private static final String WINNER_MSG = "%s wins!";
	private static final String DRAW_MSG = "Draw";
	private static final String MSG_1 = "Would you like play again?";
	private static final String MSG_2 = "Yes";
	private static final String MSG_3 = "No";
	private Point mousePoint = MouseInfo.getPointerInfo().getLocation();

	private Rectangle yesArea = new Rectangle(MID_LEFT.getX(), MID_LEFT.getY(), MID_LEFT.getWidth(), MID_LEFT.getHeight());
	private Rectangle noArea = new Rectangle(MID_RIGHT.getX(), MID_RIGHT.getY(), MID_RIGHT.getWidth(), MID_RIGHT.getHeight());

	public GameCompletedView(GameHelper gameHelper) {
		super(gameHelper);
	}

	@Override
	protected void updateDisplay(Graphics2D g) {
		if (gameHelper.getWinner().isPresent()) {
			String msg = String.format(WINNER_MSG, gameHelper.getWinner().get().getSymbol().get());
			writeCentered(g, msg, 60, Color.WHITE);
		} else {
			writeCentered(g, DRAW_MSG, 60, Color.WHITE);
		}

		writeMessage(g, MSG_1, TOP_MID, Color.WHITE);

		if (yesArea.contains(mousePoint)) {
			highlightCell(g, MID_LEFT);
			writeMessage(g, MSG_2, MID_LEFT, HIGHLIGHT_COLOR);
		} else {
			writeMessage(g, MSG_2, MID_LEFT, Color.WHITE);
		}

		if (noArea.contains(mousePoint)) {
			highlightCell(g, MID_RIGHT);
			writeMessage(g, MSG_3, MID_RIGHT, HIGHLIGHT_COLOR);
		} else {
			writeMessage(g, MSG_3, MID_RIGHT, Color.WHITE);
		}
	}

	@Override
	protected void handleMouseMoved(MouseEvent e) {
		this.mousePoint = e.getPoint();
	}

	@Override
	protected void handleMouseClicked(MouseEvent e) {
		if (noArea.contains(mousePoint)) {
			System.exit(0);
		}
		if (yesArea.contains(mousePoint)) {
			try {
				gameHelper.newGame();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}

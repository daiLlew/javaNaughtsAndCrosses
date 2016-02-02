package dai.llew.ui.views;

import dai.llew.game.GameHelper;
import dai.llew.ui.CellPosition;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static dai.llew.game.GameConstants.GAME_DIMENSIONS;
import static dai.llew.game.GameConstants.Symbol;
import static dai.llew.game.GameConstants.WINDOW_WIDTH;

public class WelcomeView extends GameView {

	private static final String MSG_1 = "Please select your symbol";

	private Rectangle noughtsArea;
	private Rectangle crossesArea;
	private Point mousePoint = MouseInfo.getPointerInfo().getLocation();
	private GameHelper gameHelper;

	public WelcomeView(GameHelper helper) {
		super();
		this.gameHelper = helper;

		CellPosition pos = CellPosition.MID_LEFT;
		noughtsArea = new Rectangle(pos.getX(), pos.getY(), pos.getWidth(), pos.getHeight());

		pos = CellPosition.MID_RIGHT;
		crossesArea = new Rectangle(pos.getX(), pos.getY(), pos.getWidth(), pos.getHeight());
	}

	@Override
	public void updateDisplay(Graphics2D g) {
		writeCentered(g, MSG_1, 100, Color.WHITE);

		drawNought(g, CellPosition.MID_LEFT);
		if (noughtsArea.contains(mousePoint)) {
			highlightCell(g, CellPosition.MID_LEFT);
		}
		drawCross(g, CellPosition.MID_RIGHT);
		if (crossesArea.contains(mousePoint)) {
			highlightCell(g, CellPosition.MID_RIGHT);
		}
	}

	@Override
	protected void handleMouseMoved(MouseEvent e) {
		mousePoint = e.getPoint();
	}

	@Override
	protected void handleMouseClicked(MouseEvent e) {
		try {
			if (noughtsArea.contains(e.getPoint())) {
				gameHelper.symbolSelected(Symbol.NOUGHTS);
			} else if (crossesArea.contains(e.getPoint())) {
				gameHelper.symbolSelected(Symbol.CROSSES);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

package dai.llew.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.function.Consumer;

import static dai.llew.game.GameConstants.GAME_DIMENSIONS;
import static dai.llew.game.GameConstants.Symbol;
import static dai.llew.game.GameConstants.WINDOW_WIDTH;

/**
 * Created by daiLlew on 30/01/2016.
 */
public class WelcomeDisplay extends GameDisplay {

	private Rectangle noughtsArea;
	private Rectangle crossesArea;
	private Point mousePoint = MouseInfo.getPointerInfo().getLocation();
	private Consumer<Symbol> symbolSelectedCallback;

	public WelcomeDisplay(Consumer<Symbol> symbolSelectedCallback) {
		super();

		this.symbolSelectedCallback = symbolSelectedCallback;

		CellPosition pos = CellPosition.MID_LEFT;
		noughtsArea = new Rectangle(pos.getX(), pos.getY(), pos.getWidth(), pos.getHeight());

		pos = CellPosition.MID_RIGHT;
		crossesArea = new Rectangle(pos.getX(), pos.getY(), pos.getWidth(), pos.getHeight());

		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				mousePoint = e.getPoint();
			}
		});

		addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (noughtsArea.contains(e.getPoint())) {
					symbolSelectedCallback.accept(Symbol.NOUGHTS);
				} else if (crossesArea.contains(e.getPoint())) {
					symbolSelectedCallback.accept(Symbol.CROSSES);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});
	}

	@Override
	public void updateDisplay(Graphics2D g) {
		Rectangle background = new Rectangle(GAME_DIMENSIONS);
		g.setPaint(Color.BLACK);
		g.fill(background);

		g.setPaint(Color.WHITE);
		g.setFont(new Font("Courier New", Font.BOLD, 24));

		String message = "Welcome to Noughts and Crosses";
		FontMetrics fm = g.getFontMetrics();

		int x = (WINDOW_WIDTH - fm.stringWidth(message)) / 2;
		g.drawString(message, x, 60);

		String selectASide = "Select your symbol";
		x = (WINDOW_WIDTH - fm.stringWidth(selectASide)) / 2;
		g.drawString(selectASide, x, 120);

		drawNought(g, CellPosition.MID_LEFT);
		if (noughtsArea.contains(mousePoint)) {
			highlightCell(g, noughtsArea);
		}
		drawCross(g, CellPosition.MID_RIGHT);
		if (crossesArea.contains(mousePoint)) {
			highlightCell(g, crossesArea);
		}
	}
}

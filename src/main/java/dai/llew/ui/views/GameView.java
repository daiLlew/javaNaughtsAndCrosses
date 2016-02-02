package dai.llew.ui.views;

import dai.llew.game.GameHelper;
import dai.llew.ui.CellPosition;

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static dai.llew.game.GameConstants.GAME_DIMENSIONS;
import static dai.llew.game.GameConstants.HIGHLIGHT_COLOR;
import static dai.llew.game.GameConstants.MEDIUM_STROKE;
import static dai.llew.game.GameConstants.SYMBOL_COLOR;
import static dai.llew.game.GameConstants.SYMBOL_SIZE;
import static dai.llew.game.GameConstants.THIN_STROKE;
import static dai.llew.game.GameConstants.WINDOW_WIDTH;
import static dai.llew.ui.CellPosition.TOP_MID;

/**
 * Defines common behaviour for Game View implementations.
 */
public abstract class GameView extends JPanel {

	protected Font font = new Font("Courier New", Font.BOLD, 24);
	protected Rectangle background = new Rectangle(GAME_DIMENSIONS);
	protected GameHelper gameHelper;

	public GameView(GameHelper gameHelper) {
		super();
		this.gameHelper = gameHelper;
		setSize(GAME_DIMENSIONS);
		setVisible(true);

		addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				handleMouseClicked(e);
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

		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				handleMouseMoved(e);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.fill(background);
		updateDisplay(g2d);
	}

	/**
	 *
	 * @param g
	 */
	protected abstract void updateDisplay(Graphics2D g);

	protected abstract void handleMouseMoved(MouseEvent e);

	protected abstract void handleMouseClicked(MouseEvent e);

	/**
	 * Draw a Cross at the specified cell position.
	 */
	protected void drawCross(Graphics2D g, CellPosition pos) {
		g.setPaint(SYMBOL_COLOR);
		g.setStroke(MEDIUM_STROKE);
		Point topLeft = pos.topLeft();
		Point bottomLeft = pos.bottomLeft();

		Point bottomRight = pos.bottomRight();
		Point topRight = pos.topRight();

		g.drawLine(topLeft.x, topLeft.y, bottomRight.x, bottomRight.y);
		g.drawLine(bottomLeft.x, bottomLeft.y, topRight.x, topRight.y);
	}

	/**
	 * Draw a Nought at the specified position.
	 */
	protected void drawNought(Graphics2D g, CellPosition pos) {
		g.setPaint(SYMBOL_COLOR);
		g.setStroke(MEDIUM_STROKE);
		Point topLeft = pos.topLeft();
		g.drawOval(topLeft.x, topLeft.y, SYMBOL_SIZE, SYMBOL_SIZE);
	}

	protected void highlightCell(Graphics2D g, CellPosition pos) {
		g.setPaint(HIGHLIGHT_COLOR);
		g.setStroke(THIN_STROKE);
		g.draw(pos.getRect());
	}

	protected void writeMessage(Graphics2D g, String message, CellPosition position, Color color) {
		g.setPaint(color);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();

		int x = ((position.getWidth() - fm.stringWidth(message)) / 2) + position.getX();
		int y = ((position.getHeight() - fm.getHeight()) / 2) + fm.getAscent() + position.getY();
		g.drawString(message, x, y);
	}

	protected void writeCentered(Graphics2D g, String message, int y, Color color) {
		g.setPaint(color);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();

		int x = ((TOP_MID.getWidth() - fm.stringWidth(message)) / 2) + TOP_MID.getX();
		g.drawString(message, x, y);
	}
}

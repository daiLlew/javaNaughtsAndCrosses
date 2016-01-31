package dai.llew.ui;

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import static dai.llew.game.GameConstants.GAME_DIMENSIONS;
import static dai.llew.game.GameConstants.HIGHLIGHT_COLOR;
import static dai.llew.game.GameConstants.MEDIUM_STROKE;
import static dai.llew.game.GameConstants.SYMBOL_COLOR;
import static dai.llew.game.GameConstants.SYMBOL_SIZE;
import static dai.llew.game.GameConstants.THIN_STROKE;
import static dai.llew.game.GameConstants.WINDOW_WIDTH;

/**
 * Defines common behaviour for Game Display implementations.
 */
public abstract class GameDisplay extends JPanel {

	protected Font font = new Font("Courier New", Font.BOLD, 24);

	public GameDisplay() {
		super();
		setSize(GAME_DIMENSIONS);
		setVisible(true);
	}

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

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		updateDisplay(g2d);
	}

	protected void writeMessage(Graphics2D g, String message, int y) {
		g.setPaint(Color.WHITE);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
		int x = (WINDOW_WIDTH - fm.stringWidth(message)) / 2;
		g.drawString(message, x, y);
	}

	protected abstract void updateDisplay(Graphics2D g);

}

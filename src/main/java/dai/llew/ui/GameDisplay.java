package dai.llew.ui;

import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import static dai.llew.game.GameConstants.GAME_DIMENSIONS;
import static dai.llew.game.GameConstants.HIGHLIGHT_COLOR;
import static dai.llew.game.GameConstants.MEDIUM_STROKE;
import static dai.llew.game.GameConstants.SYMBOL_COLOR;
import static dai.llew.game.GameConstants.SYMBOL_SIZE;
import static dai.llew.game.GameConstants.THIN_STROKE;

/**
 * Defines common behaviour for Game Display implementations.
 */
public abstract class GameDisplay extends JPanel {

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

	protected void highlightCell(Graphics2D g, Rectangle rectangle) {
		g.setPaint(HIGHLIGHT_COLOR);
		g.setStroke(THIN_STROKE);
		g.draw(rectangle);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		updateDisplay(g2d);
	}

	protected abstract void updateDisplay(Graphics2D g);

}

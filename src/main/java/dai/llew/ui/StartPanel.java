package dai.llew.ui;

import javax.swing.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import static dai.llew.game.GameConstants.GAME_DIMENSIONS;
import static dai.llew.game.GameConstants.STROKE_SIZE;
import static dai.llew.game.GameConstants.SYMBOL_SIZE;
import static dai.llew.game.GameConstants.WINDOW_WIDTH;

/**
 * Created by daiLlew on 30/01/2016.
 */
public class StartPanel extends JPanel {

	public StartPanel() {
		super();
		setVisible(true);
		setOpaque(true);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		drawStartPanel(g2d);
	}

	public void drawStartPanel(Graphics2D g2d) {
		Rectangle background = new Rectangle(GAME_DIMENSIONS);
		g2d.setPaint(Color.BLACK);
		g2d.fill(background);

		g2d.setPaint(Color.WHITE);
		g2d.setFont(new Font("Courier New", Font.BOLD, 24));

		String message = "Welcome to Noughts and Crosses";
		FontMetrics fm = g2d.getFontMetrics();

		int x = (WINDOW_WIDTH - fm.stringWidth(message)) / 2;
		g2d.drawString(message, x, 60);

		String selectASide = "Select your symbol";
		x = (WINDOW_WIDTH - fm.stringWidth(selectASide)) / 2;
		g2d.drawString(selectASide, x, 120);

		drawNought(g2d, CellPosition.MID_LEFT);
		drawCross(g2d, CellPosition.MID_RIGHT);
	}

	private void drawNought(Graphics2D g2d, CellPosition pos) {
		drawRect(g2d, pos);

		g2d.setStroke(new BasicStroke(STROKE_SIZE));
		Point topLeft = pos.topLeft();
		g2d.drawOval(topLeft.x, topLeft.y, SYMBOL_SIZE, SYMBOL_SIZE);
	}

	private void drawRect(Graphics2D g2d, CellPosition pos) {
		g2d.setStroke(new BasicStroke(3));
		Rectangle rect = new Rectangle(pos.getX(), pos.getY(), pos.getWidth(), pos.getHeight());
		g2d.draw(rect);
	}

	private void drawCross(Graphics2D g, CellPosition pos) {
		drawRect(g, pos);

		Point topLeft = pos.topLeft();
		Point bottomLeft = pos.bottomLeft();
		Point bottomRight = pos.bottomRight();
		Point topRight = pos.topRight();

		g.setStroke(new BasicStroke(STROKE_SIZE));
		g.drawLine(topLeft.x, topLeft.y, bottomRight.x, bottomRight.y);
		g.drawLine(bottomLeft.x, bottomLeft.y, topRight.x, topRight.y);
	}
}

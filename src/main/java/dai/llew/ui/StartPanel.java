package dai.llew.ui;

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.function.Consumer;

import static dai.llew.game.GameConstants.GAME_DIMENSIONS;
import static dai.llew.game.GameConstants.HIGHLIGHT_COLOR;
import static dai.llew.game.GameConstants.SYMBOL_COLOR;
import static dai.llew.game.GameConstants.SYMBOL_SIZE;
import static dai.llew.game.GameConstants.Symbol;
import static dai.llew.game.GameConstants.MEDIUM_STROKE;
import static dai.llew.game.GameConstants.THIN_STROKE;
import static dai.llew.game.GameConstants.WINDOW_WIDTH;

/**
 * Created by daiLlew on 30/01/2016.
 */
public class StartPanel extends JPanel {

	private Rectangle noughtsArea;
	private Rectangle crossesArea;
	private Point mousePoint = MouseInfo.getPointerInfo().getLocation();
	private Consumer<Symbol> symbolSelectedCallback;

	public StartPanel(Consumer<Symbol> symbolSelectedCallback) {
		super();
		setVisible(true);
		setOpaque(true);

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
		if (noughtsArea.contains(mousePoint)) {
			drawRect(g2d, noughtsArea);
		}
		g2d.setPaint(SYMBOL_COLOR);
		g2d.setStroke(MEDIUM_STROKE);
		Point topLeft = pos.topLeft();
		g2d.drawOval(topLeft.x, topLeft.y, SYMBOL_SIZE, SYMBOL_SIZE);
	}

	private void drawRect(Graphics2D g2d, Rectangle rectangle) {
		g2d.setPaint(HIGHLIGHT_COLOR);
		g2d.setStroke(THIN_STROKE);
		g2d.draw(rectangle);
	}

	private void drawCross(Graphics2D g, CellPosition pos) {
		if (crossesArea.contains(mousePoint)) {
			drawRect(g, crossesArea);
		}

		Point topLeft = pos.topLeft();
		Point bottomLeft = pos.bottomLeft();
		Point bottomRight = pos.bottomRight();
		Point topRight = pos.topRight();

		g.setPaint(SYMBOL_COLOR);
		g.setStroke(MEDIUM_STROKE);
		g.drawLine(topLeft.x, topLeft.y, bottomRight.x, bottomRight.y);
		g.drawLine(bottomLeft.x, bottomLeft.y, topRight.x, topRight.y);
	}
}

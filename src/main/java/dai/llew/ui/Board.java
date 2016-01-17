package dai.llew.ui;

import javax.swing.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

import static dai.llew.ui.CellPosition.*;

/**
 * Created by daiLlew on 17/01/2016.
 */
public class Board extends JPanel {

	private static final int MARGIN = 10;

	private Dimension dimension;
	private Rectangle2D background;

	private Map<CellPosition, BoardCell> cells;

	public Board(Dimension dimension) {
		super();
		setSize(dimension);
		this.dimension = dimension;
		this.cells = new HashMap<>();

		cells.put(TOP_LEFT, new BoardCell(TOP_LEFT));
		cells.put(TOP_MID, new BoardCell(TOP_MID));
		cells.put(TOP_RIGHT, new BoardCell(TOP_RIGHT));

		cells.put(MID_LEFT, new BoardCell(MID_LEFT));
		cells.put(MID_MID, new BoardCell(MID_MID));
		cells.put(MID_RIGHT, new BoardCell(MID_RIGHT));

		cells.put(BOTTOM_LEFT, new BoardCell(BOTTOM_LEFT));
		cells.put(BOTTOM_MID, new BoardCell(BOTTOM_MID));
		cells.put(BOTTOM_RIGHT, new BoardCell(BOTTOM_RIGHT));

		background = new Rectangle(100, 100, 320, 320);

		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				updateMousePos(MouseInfo.getPointerInfo().getLocation());
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}

		});

		addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				for (BoardCell cell : cells.values()) {
					Point2D mouse = (Point2D)MouseInfo.getPointerInfo().getLocation();
					if (cell.getRect().contains(mouse)) {
						System.out.println("\n\n\n\n\nPressed " + cell.getPosition().name());
						cell.fill();
						break;
					}
				}
			}
			@Override
			public void mouseClicked(MouseEvent e) {
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

	public void updateMousePos(Point point) {
		for (BoardCell boardCell : cells.values()) {
			boardCell.updateColor(point);
		}

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(10));
		this.drawBoard(g2);
	}

	private void drawBoard(Graphics2D g) {
		g.setPaint(Color.BLACK);
		g.fill(background);

		for (BoardCell cell : this.cells.values()) {
			g.setPaint(cell.getColor());
			g.fill(cell.getRect());

			if (cell.isFilled()) {
				g.setPaint(Color.BLACK);
				int x = cell.getPosition().getX();
				int y = cell.getPosition().getY();

				g.drawLine(x, y, x + cell.getPosition().getWidth(), y + cell.getPosition().getHeight());
				g.drawLine(x + cell.getPosition().getWidth(), y, x, y + cell.getPosition().getHeight());

			}
		}
	}

}

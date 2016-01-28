package dai.llew.ui;

import dai.llew.game.Player;
import dai.llew.game.Player.Symbol;

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
import java.util.function.Supplier;

import static dai.llew.game.Player.PlayerType.HUMAN;
import static dai.llew.game.Player.Symbol.CROSSES;
import static dai.llew.ui.CellPosition.BOTTOM_LEFT;
import static dai.llew.ui.CellPosition.BOTTOM_MID;
import static dai.llew.ui.CellPosition.BOTTOM_RIGHT;
import static dai.llew.ui.CellPosition.MID_LEFT;
import static dai.llew.ui.CellPosition.MID_MID;
import static dai.llew.ui.CellPosition.MID_RIGHT;
import static dai.llew.ui.CellPosition.TOP_LEFT;
import static dai.llew.ui.CellPosition.TOP_MID;
import static dai.llew.ui.CellPosition.TOP_RIGHT;

/**
 * Created by daiLlew on 17/01/2016.
 */
public class Board extends JPanel {

	private Dimension dimension;
	private Rectangle2D background;
	private Runnable turnCompleted;
	private Supplier<Player> currentPlayer;

	private Map<CellPosition, BoardCell> cells;

	public Board(Dimension dimension, Supplier<Player> currentPlayer, Runnable turnCompleted) {
		super();
		setSize(dimension);
		this.dimension = dimension;
		this.turnCompleted = turnCompleted;
		this.currentPlayer = currentPlayer;
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
				Player player = currentPlayer.get();

				for (BoardCell cell : cells.values()) {
					Point2D mouse = (Point2D) MouseInfo.getPointerInfo().getLocation();
					if (cell.getRect().contains(mouse) && isHuman(player)) {
						cell.fill(player.getSymbol());
						turnCompleted.run();
						break;
					}
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				updateMousePos(MouseInfo.getPointerInfo().getLocation());
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
		cells.values().stream().forEach(boardCell -> boardCell.updateColor(point, currentPlayer.get()));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(15));
		this.drawBoard(g2);
	}

	/**
	 * Draw the board in its current state.
	 */
	private void drawBoard(Graphics2D g) {
		g.setPaint(Color.BLACK);
		g.fill(background);

		this.cells.values().stream().forEach(cell -> {
			g.setPaint(cell.getColor());
			g.fill(cell.getRect());
			if (cell.isFilled()) {
				if (CROSSES.equals(cell.getSymbol())) {
					drawCross(g, cell.getPosition());
				} else {
					drawNaught(g, cell.getPosition());
				}
			}
		});
	}

	/**
	 * Draw a Cross at the specified cell position.
	 */
	private void drawCross(Graphics2D g, CellPosition pos) {
		g.setPaint(Color.BLUE);
		Point topLeft = pos.topLeft();
		Point bottomLeft = pos.bottomLeft();

		Point bottomRight = pos.bottomRight();
		Point topRight = pos.topRight();

		g.drawLine(topLeft.x, topLeft.y, bottomRight.x, bottomRight.y);
		g.drawLine(bottomLeft.x, bottomLeft.y, topRight.x, topRight.y);
	}

	/**
	 * Draw a Naught at the specified cell position.
	 */
	private void drawNaught(Graphics2D g, CellPosition pos) {
		g.setPaint(Color.GREEN);
		Point topLeft = pos.topLeft();
		g.drawOval(topLeft.x, topLeft.y, Symbol.WIDTH, Symbol.WIDTH);
	}

	private boolean isHuman(Player player) {
		return HUMAN.equals(player.getPlayerType());
	}

	public Dimension getDimension() {
		return dimension;
	}

	public Map<CellPosition, BoardCell> getCells() {
		return cells;
	}

	public BoardCell getCell(CellPosition position) {
		return cells.get(position);
	}
}

package dai.llew.ui;

import static dai.llew.game.GameConstants.BOARD_DIMENSIONS;
import static dai.llew.game.GameConstants.STROKE_SIZE;
import static dai.llew.game.GameConstants.SYMBOL_SIZE;
import dai.llew.game.Player;

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
import java.util.Optional;
import java.util.function.Supplier;

import static dai.llew.game.GameConstants.PlayerType.HUMAN;
import static dai.llew.game.GameConstants.Symbol.CROSSES;
import static dai.llew.game.GameConstants.GAME_DIMENSIONS;
import static dai.llew.game.GameConstants.BOARD_COORD;
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

	private Rectangle2D background;
	private Runnable turnCompleted;
	private Supplier<Player> currentPlayerSupplier;
	private Map<CellPosition, BoardCell> cells;
	private MouseMotionListener mouseMotionListener;
	private MouseListener mouseListener;

	private Board(Supplier<Player> currentPlayerSupplier, Runnable turnCompleted) {
		super();
		setSize(GAME_DIMENSIONS);

		this.turnCompleted = turnCompleted;
		this.currentPlayerSupplier = currentPlayerSupplier;

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

		background = new Rectangle(BOARD_COORD, BOARD_COORD, BOARD_DIMENSIONS.width, BOARD_DIMENSIONS.height);

		this.mouseMotionListener = new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				updateMousePos(MouseInfo.getPointerInfo().getLocation());
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}
		};

		this.mouseListener = new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				Player player = currentPlayerSupplier.get();
				Point2D mouse = MouseInfo.getPointerInfo().getLocation();

				Optional<BoardCell> mouseCell = cells.values()
						.stream()
						.filter((cell) -> cell.getRect().contains(mouse) && isHuman(player))
						.findFirst();

				if (mouseCell.isPresent()) {
					mouseCell.get().fill(player.getSymbol());
					turnCompleted.run();
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
		};

		addMouseMotionListener(mouseMotionListener);
		addMouseListener(mouseListener);
		setVisible(true);
	}

	public void updateMousePos(Point point) {
		cells.values().stream().forEach(boardCell -> boardCell.updateColor(point, currentPlayerSupplier.get()));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(STROKE_SIZE));
		this.drawBoard(g2);
	}

	/**
	 * Draw the board in its current state.
	 */
	private void drawBoard(Graphics2D g) {
		g.setPaint(Color.WHITE);
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
		g.setPaint(Color.WHITE);
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
		g.setPaint(Color.WHITE);
		Point topLeft = pos.topLeft();
		g.drawOval(topLeft.x, topLeft.y, SYMBOL_SIZE, SYMBOL_SIZE);
	}

	private boolean isHuman(Player player) {
		return HUMAN.equals(player.getPlayerType());
	}

	public Map<CellPosition, BoardCell> getCells() {
		return cells;
	}

	public BoardCell getCell(CellPosition position) {
		return cells.get(position);
	}

	/**
	 *
	 */
	public static class Builder {
		private Board board;
		private Supplier<Player> currentPlayerSupplier;
		private Runnable turnCompleted;

		public Builder currentPlayerSupplier(Supplier<Player> currentPlayerSupplier) {
			this.currentPlayerSupplier = currentPlayerSupplier;
			return this;
		}

		public Builder turnCompleted(Runnable turnCompleted) {
			this.turnCompleted = turnCompleted;
			return this;
		}

		public Board build() {
			return new Board(this.currentPlayerSupplier, this.turnCompleted);
		}
	}
}

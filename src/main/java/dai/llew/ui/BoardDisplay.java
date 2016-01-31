package dai.llew.ui;

import dai.llew.game.Player;

import java.awt.Color;
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

import static dai.llew.game.GameConstants.BOARD_COORD;
import static dai.llew.game.GameConstants.BOARD_DIMENSIONS;
import static dai.llew.game.GameConstants.GAME_DIMENSIONS;
import static dai.llew.game.GameConstants.HIGHLIGHT_COLOR;
import static dai.llew.game.GameConstants.MEDIUM_STROKE;
import static dai.llew.game.GameConstants.PlayerType.HUMAN;
import static dai.llew.game.GameConstants.THICK_STROKE;
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
 * Class defines the surface area the game is played on.
 */
public class BoardDisplay extends GameDisplay {

	private Rectangle2D background;
	private Runnable turnCompleted;
	private Supplier<Player> currentPlayerSupplier;
	private Map<CellPosition, BoardCell> cells;
	private MouseMotionListener mouseMotionListener;
	private MouseListener mouseListener;
	private Point mousePoint = MouseInfo.getPointerInfo().getLocation();
	private Optional<StrikeLine> strikeThrough = Optional.empty();

	/**
	 * Construct the game BoardDisplay.
	 *
	 * @param currentPlayerSupplier callback function to get the current {@link Player}.
	 * @param turnCompleted         callback function to notifying when the current {@link Player} has completed their turn.
	 */
	private BoardDisplay(Supplier<Player> currentPlayerSupplier, Runnable turnCompleted) {
		super();

		this.turnCompleted = turnCompleted;
		this.currentPlayerSupplier = currentPlayerSupplier;
		this.background = new Rectangle(BOARD_COORD, BOARD_COORD, BOARD_DIMENSIONS.width, BOARD_DIMENSIONS.height);

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

		this.mouseMotionListener = new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				//updateMousePos(e.getPoint());
				mousePoint = e.getPoint();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}
		};

		this.mouseListener = new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				Player player = currentPlayerSupplier.get();
				Point2D mouse = e.getPoint();

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
				//updateMousePos(e.getPoint());
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
	}

	public void updateMousePos(Point point) {
		cells.values().stream().forEach(boardCell -> boardCell.updateColor(point, currentPlayerSupplier.get()));
	}

	/**
	 * Draw the boardDisplay in its current state.
	 */
	@Override
	protected void updateDisplay(Graphics2D g) {
		g.setPaint(Color.BLACK);
		g.fill(new Rectangle(GAME_DIMENSIONS));

		g.setPaint(Color.WHITE);
		g.fill(background);

		this.cells.values().stream().forEach(cell -> {
			g.setPaint(Color.BLACK);
			g.fill(cell.getRect());
			if (cell.getRect().contains(mousePoint) && !cell.isFilled()) {
				highlightCell(g, cell.getRect());
			}
			if (cell.isFilled()) {
				drawSymbol(g, cell);
			}
		});
		if (strikeThrough.isPresent()) {
			drawStrikeThrough(g);
		}
		revalidate();
	}

	/**
	 * Set the {@link StrikeLine} to use to show the winning combination.
	 */
	public void setStrike(StrikeLine line) {
		this.strikeThrough = Optional.of(line);
	}

	/**
	 * Draw {@link dai.llew.game.GameConstants.Symbol} in the specified {@link BoardCell}.
	 */
	private void drawSymbol(Graphics2D g, BoardCell cell) {
		g.setStroke(MEDIUM_STROKE);
		switch (cell.getSymbol()) {
			case CROSSES:
				drawCross(g, cell.getPosition());
				break;
			case NOUGHTS:
				drawNought(g, cell.getPosition());
				break;
		}
	}

	/**
	 * Draws a line through the winning cell combination.
	 */
	private void drawStrikeThrough(Graphics2D g) {
		g.setStroke(THICK_STROKE);
		g.setPaint(HIGHLIGHT_COLOR);
		StrikeLine line = strikeThrough.get();
		g.drawLine(line.getStart().x, line.getStart().y, line.getEnd().x, line.getEnd().y);
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
	 * Builder for creating new {@link BoardDisplay}.
	 */
	public static class Builder {
		private BoardDisplay boardDisplay;
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

		public BoardDisplay build() {
			return new BoardDisplay(this.currentPlayerSupplier, this.turnCompleted);
		}
	}
}

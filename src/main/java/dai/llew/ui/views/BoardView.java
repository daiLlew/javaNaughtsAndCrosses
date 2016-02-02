package dai.llew.ui.views;

import dai.llew.game.GameHelper;
import dai.llew.game.Player;
import dai.llew.ui.BoardCell;
import dai.llew.ui.CellPosition;
import dai.llew.ui.StrikeLine;

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

import static dai.llew.game.GameConstants.BOARD_COORD;
import static dai.llew.game.GameConstants.BOARD_DIMENSIONS;
import static dai.llew.game.GameConstants.GAME_DIMENSIONS;
import static dai.llew.game.GameConstants.HIGHLIGHT_COLOR;
import static dai.llew.game.GameConstants.MEDIUM_STROKE;
import static dai.llew.game.GameConstants.PlayerType.HUMAN;
import static dai.llew.game.GameConstants.THICK_STROKE;
import static dai.llew.game.GameConstants.WINDOW_WIDTH;
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
public class BoardView extends GameView {

	private static final String WHOS_WHO = "Player: %s\t\tComputer: %s";

	private Rectangle2D background;
	private Map<CellPosition, BoardCell> cells;
	private Point mousePoint = MouseInfo.getPointerInfo().getLocation();
	private Optional<StrikeLine> strikeThrough = Optional.empty();

	/**
	 * Construct the game BoardView.
	 */
	public BoardView(GameHelper helper) {
		super(helper);

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
	}

	/**
	 * Draw the boardDisplay in its current state.
	 */
	@Override
	protected void updateDisplay(Graphics2D g) {
		g.setPaint(Color.WHITE);
		g.fill(background);

		this.cells.values().stream().forEach(cell -> {
			g.setPaint(Color.BLACK);
			g.fill(cell.getRect());
			if (cell.getRect().contains(mousePoint) && !cell.isFilled()) {
				highlightCell(g, cell.getPosition());
			}
			if (cell.isFilled()) {
				drawSymbol(g, cell);
			}
		});
		if (strikeThrough.isPresent()) {
			drawStrikeThrough(g);
		}
		String message = String.format(WHOS_WHO, gameHelper.getHumanPlayer().getSymbol().get(),
				gameHelper.getComputerPlayer().getSymbol().get());

		writeCentered(g, message, 60, Color.WHITE);
		revalidate();
	}

	@Override
	protected void handleMouseMoved(MouseEvent e) {
		this.mousePoint = e.getPoint();
	}

	@Override
	protected void handleMouseClicked(MouseEvent e) {
		Player player = gameHelper.getCurrentPlayer();
		Point2D mouse = e.getPoint();

		Optional<BoardCell> mouseCell = cells.values()
				.stream()
				.filter((cell) -> cell.getRect().contains(mouse) && isHuman(player))
				.findFirst();

		if (mouseCell.isPresent()) {
			mouseCell.get().fill(player.getSymbol());
			gameHelper.turnCompleted();
		}
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
}

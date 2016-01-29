package dai.llew.ui;

import dai.llew.game.Player;
import dai.llew.game.GameConstants.PlayerType;
import static dai.llew.game.GameConstants.Symbol.CROSSES;
import dai.llew.game.GameConstants.Symbol;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

/**
 * Created by daiLlew on 17/01/2016.
 */
public class BoardCell {

	private CellPosition position;
	private Rectangle2D rect;
	private Color color;
	private boolean isFilled = false;
	private Symbol symbol;

	public BoardCell(CellPosition position) {
		this.position = position;
		this.rect = new Rectangle(position.getX(), position.getY(), position.getWidth(), position.getHeight());
		this.color = Color.WHITE;
		this.symbol = null;
	}

	public void updateColor(Point point, Player player) {
		switch (player.getPlayerType()) {
			case HUMAN:
				if (this.rect.contains(point) && !this.isFilled()) {
					this.color = Color.GRAY;
				} else {
					this.color = Color.WHITE;
				}
				break;
			default:
				// Is the computer.
				this.color = Color.WHITE;
		}
	}

	public Rectangle2D getRect() {
		return rect;
	}

	public Color getColor() {
		return color;
	}

	public void winningCell() {
		this.color = Color.YELLOW;
	}

	public CellPosition getPosition() {
		return position;
	}

	public void fill(Symbol symbol) {
		this.symbol = symbol;
		this.isFilled = true;
	}

	public boolean isFilled() {
		return isFilled;
	}

	public Symbol getSymbol() {
		return symbol;
	}
}

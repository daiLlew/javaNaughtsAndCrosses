package dai.llew.ui;

import dai.llew.game.GameConstants.Symbol;

import java.awt.Rectangle;

public class BoardCell {

	private CellPosition position;
	private Rectangle rect;
	private boolean isFilled = false;
	private Symbol symbol;

	public BoardCell(CellPosition position) {
		this.position = position;
		this.rect = position.getRect();
		this.symbol = null;
	}

	public Rectangle getRect() {
		return rect;
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

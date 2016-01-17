package dai.llew.ui;

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

	public BoardCell(CellPosition position) {
		this.position = position;
		this.rect = new Rectangle(position.getX(), position.getY(), position.getWidth(), position.getHeight());
		this.color = Color.WHITE;
	}

	public void updateColor(Point point) {
		if (this.rect.contains(point)) {
			this.color = Color.GRAY;
		} else {
			this.color = Color.WHITE;
		}
	}

	public Rectangle2D getRect() {
		return rect;
	}

	public Color getColor() {
		return color;
	}

	public CellPosition getPosition() {
		return position;
	}

	public void fill() {
		this.isFilled = true;
	}

	public boolean isFilled() {
		return isFilled;
	}
}

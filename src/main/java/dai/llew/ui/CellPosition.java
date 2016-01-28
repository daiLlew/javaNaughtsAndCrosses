package dai.llew.ui;

import java.awt.Point;

/**
 * Created by daiLlew on 17/01/2016.
 */
public enum CellPosition {

	TOP_LEFT(100, 100),
	TOP_MID(210, 100),
	TOP_RIGHT(320, 100),

	MID_LEFT(100, 210),
	MID_MID(210, 210),
	MID_RIGHT(320, 210),

	BOTTOM_LEFT(100, 320),
	BOTTOM_MID(210, 320),
	BOTTOM_RIGHT(320, 320);

	private static final int WIDTH = 100;
	private static final int HEIGHT = 100;
	private static final int PADDING = 20;

	private final int x;
	private final int y;
	private final int width;
	private final int height;

	CellPosition(int x, int y) {
		this.x = x;
		this.y = y;
		this.width = WIDTH;
		this.height = HEIGHT;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	/**
	 * @return X, Y for top right corner for the symbole in this cell pos.
	 */
	public Point topLeft() {
		return new Point(
				(x + PADDING),
				(y + PADDING)
		);
	}

	public Point topRight() {
		return new Point(
				(x + getWidth() - PADDING),
				(y + PADDING)
		);
	}

	public Point bottomLeft() {
		return new Point(
				(x + PADDING),
				(y + getHeight() - PADDING)
		);
	}

	public Point bottomRight() {
		return new Point(
				(x + getWidth() - PADDING),
				(y + getHeight() - PADDING)
		);
	}

	@Override
	public String toString() {
		return " [" + name() + "] ";
	}
}

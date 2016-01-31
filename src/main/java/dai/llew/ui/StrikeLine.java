package dai.llew.ui;

import java.awt.Point;

/**
 * Created by daiLlew on 31/01/2016.
 */
public class StrikeLine {
	Point start;
	Point end;

	public StrikeLine(Point start, Point end) {
		this.start = start;
		this.end = end;
	}

	public Point getStart() {
		return start;
	}

	public Point getEnd() {
		return end;
	}
}

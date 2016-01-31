package dai.llew.game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;

public class GameConstants {

	/**
	 * The size of the O's & X's
	 */
	public static final int SYMBOL_SIZE = 60;
	/**
	 * The main window width.
	 */
	public static final int WINDOW_WIDTH = 520;
	/**
	 * The main window height.
	 */
	public static final int WINDOW_HEIGHT = 520;
	/**
	 * The main window dimensions.
	 */
	public static final Dimension GAME_DIMENSIONS = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);
	/**
	 * The X/Y coordinate of the top left corner of the board.
	 */
	public static final int BOARD_COORD = 100;
	/**
	 * The board dimensions.
	 */
	public static final Dimension BOARD_DIMENSIONS = new Dimension(320, 320);
	/**
	 * Thick Stoke.
	 */
	public static final BasicStroke THICK_STROKE = new BasicStroke(15);
	/**
	 * Thick Stoke.
	 */
	public static final BasicStroke MEDIUM_STROKE = new BasicStroke(20);
	/**
	 * Thin Stroke.
	 */
	public static final BasicStroke THIN_STROKE = new BasicStroke(3);
	/**
	 * Color to use when highlighting an item.
	 */
	public static final Color HIGHLIGHT_COLOR = new Color(0/255.0f, 120/255.0f, 255/255.0f, 0.6f);
	/**
	 * The color for the O's & X's symbols.
	 */
	public static final Color SYMBOL_COLOR = Color.WHITE;

	/**
	 * Types to classify if the game is still in play or if it is completed.
	 */
	public enum GameStatus {
		/**
		 *
		 */
		START_MENU,

		/**
		 * The game is still in play.
		 */
		IN_PLAY,

		/**
		 * A {@link Player} won the game.
		 */
		WON,

		/**
		 * Neither {@link Player} won the game.
		 */
		DRAWN;
	}

	/**
	 * Types to distinguish which player is playing.
	 */
	public enum PlayerType {
		/**
		 * A Human player.
		 */
		HUMAN,

		/**
		 * The computer AI.
		 */
		COMPUTER
	}

	public enum Symbol {
		/**
		 * The Noughts Symbol.
		 */
		NOUGHTS,

		/**
		 * The Crosses Symbol.
		 */
		CROSSES;
	}
}

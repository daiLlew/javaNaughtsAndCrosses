package dai.llew.game;

public class GameConstants {

	/**
	 * Type for the outcome of a game.
	 */
	public enum GameResult {
		/**
		 * A {@link Player} won the game.
		 */
		WON,

		/**
		 * Neither {@link Player} won the game.
		 */
		DRAW
	}

	/**
	 * Types to classify if the game is still in play or if it is completed.
	 */
	public enum GameState {

		/**
		 * The game is still in play.
		 */
		PENDING,

		/**
		 * The game is over.
		 */
		COMPLETED
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

		public static final int WIDTH = 60;
	}
}

package dai.llew.game;

import dai.llew.ui.Board;

/**
 * Created by daiLlew on 24/01/2016.
 */
public class AIModule {

	private static AIModule instance = null;

	public static AIModule getInstance() {
		if (instance == null) {
			instance = new AIModule();
		}
		return instance;
	}

	private AIModule() {
		// Hide the constructor.
	}

	public void takeTurn(Board board, Player computerPlayer) throws InterruptedException {
		Thread.sleep(3000);
		board.getCells().values()
				.stream()
				.filter((cell) -> !cell.isFilled()).findFirst()
				.ifPresent((cell) -> {
					System.out.println("\n\n\nComputer chooses " + cell.getPosition().name());
					cell.fill(computerPlayer.getSymbol());
				});
	}
}

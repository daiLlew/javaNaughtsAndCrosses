package dai.llew.game;

import static dai.llew.game.GameConstants.Symbol;

/**
 * Created by daiLlew on 31/01/2016.
 */
public interface GameHelper {

	Player getCurrentPlayer();

	Player humanPlayer();

	Player computerPlayer();

	void turnTaken();

	void symbolSelected(GameConstants.Symbol symbol);
}

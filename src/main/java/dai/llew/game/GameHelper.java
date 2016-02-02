package dai.llew.game;

import java.util.Optional;

public interface GameHelper {

	Player getCurrentPlayer();

	Player getHumanPlayer();

	Player getComputerPlayer();

	void turnCompleted();

	void symbolSelected(GameConstants.Symbol symbol);

	void newGame() throws Exception;

	Optional<Player> getWinner();
}

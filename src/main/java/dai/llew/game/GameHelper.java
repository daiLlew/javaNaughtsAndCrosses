package dai.llew.game;

public interface GameHelper {

	Player getCurrentPlayer();

	Player getHumanPlayer();

	Player getComputerPlayer();

	void turnCompleted();

	void symbolSelected(GameConstants.Symbol symbol) throws Exception;
}

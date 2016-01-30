package dai.llew.ui;

import javax.swing.*;

import static dai.llew.game.GameConstants.GAME_DIMENSIONS;

/**
 * Created by daiLlew on 17/01/2016.
 */
public class GUI {

	private static final String TITLE = "Java Naughts & Crosses";

	private JFrame mainFrame;
	private Board board;
	private StartPanel startPanel;

	public GUI(Board board) {
		this.board = board;
		this.mainFrame = new JFrame(TITLE);
		this.mainFrame.setSize(GAME_DIMENSIONS);
		this.mainFrame.setResizable(false);
		this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.startPanel = new StartPanel();

		this.mainFrame.getContentPane().add(board);
		//this.mainFrame.getContentPane().add(startPanel);
		this.mainFrame.setVisible(true);

	}

	public Board getBoard() {
		return this.board;
	}

	public JFrame getMainFrame() {
		return this.mainFrame;
	}

	public StartPanel getStartPanel() {
		return startPanel;
	}

	public void setStartPanel(StartPanel startPanel) {
		this.startPanel = startPanel;
	}
}

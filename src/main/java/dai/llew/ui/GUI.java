package dai.llew.ui;

import javax.swing.*;

/**
 * Created by daiLlew on 17/01/2016.
 */
public class GUI {

	private static final String TITLE = "Java Naughts & Crosses";

	private JFrame mainFrame;
	private Board board;

	public GUI(Board board) {
		this.board = board;
		this.mainFrame = new JFrame(TITLE);
		this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.mainFrame.setSize(board.getDimension());
		this.mainFrame.setVisible(true);
		this.mainFrame.getContentPane().add(board);
	}

	public Board getBoard() {
		return this.board;
	}

	public JFrame getMainFrame() {
		return this.mainFrame;
	}
}

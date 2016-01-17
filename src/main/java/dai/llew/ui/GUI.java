package dai.llew.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by daiLlew on 17/01/2016.
 */
public class GUI {

	private static final String TITLE = "Java Naughts & Crosses";
	private static final Dimension DIMENSION = new Dimension(550, 550);

	private JFrame mainFrame;
	private Board board;

	public GUI() {
		this.mainFrame = new JFrame(TITLE);
		this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.mainFrame.setSize(DIMENSION);
		this.mainFrame.setVisible(true);

		this.board = new Board(DIMENSION);
		this.mainFrame.getContentPane().add(board);
	}

	public Board getBoard() {
		return this.board;
	}

	public JFrame getMainFrame() {
		return this.mainFrame;
	}
}

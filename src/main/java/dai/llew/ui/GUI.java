package dai.llew.ui;

import javax.swing.*;

import static dai.llew.game.GameConstants.GAME_DIMENSIONS;

/**
 * Created by daiLlew on 17/01/2016.
 */
public class GUI {

	private static final String TITLE = "Java Naughts & Crosses";

	private JFrame mainFrame;
	private JPanel canvas;

	public GUI(JPanel canvas) {
		this.mainFrame = new JFrame(TITLE);
		this.mainFrame.setSize(GAME_DIMENSIONS);
		this.mainFrame.setResizable(false);
		this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.canvas = canvas;
		this.mainFrame.getContentPane().add(canvas);
		this.mainFrame.setVisible(true);

	}

	public void updateDisplay(JPanel update) {
		this.mainFrame.getContentPane().remove(canvas);
		this.mainFrame.getContentPane().add(update);
		this.mainFrame.revalidate();
	}

	public JFrame getMainFrame() {
		return this.mainFrame;
	}
}

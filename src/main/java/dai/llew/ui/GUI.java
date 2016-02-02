package dai.llew.ui;

import dai.llew.ui.views.GameView;

import javax.swing.*;

import static dai.llew.game.GameConstants.GAME_DIMENSIONS;

/**
 * Created by daiLlew on 17/01/2016.
 */
public class GUI {

	private static final String TITLE = "Java Naughts & Crosses";

	private JFrame mainFrame;
	private JPanel currentView;

	public GUI(GameView display) {
		this.mainFrame = new JFrame(TITLE);
		this.mainFrame.setSize(GAME_DIMENSIONS);
		this.mainFrame.setResizable(false);
		this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.currentView = display;
		this.mainFrame.getContentPane().add(display);
		this.mainFrame.setVisible(true);

	}

	public void updateDisplay(GameView newView) {
		this.mainFrame.getContentPane().remove(currentView);
		this.currentView = newView;
		this.mainFrame.getContentPane().add(newView);
		this.mainFrame.revalidate();
	}

	public JFrame getMainFrame() {
		return this.mainFrame;
	}
}

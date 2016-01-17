package dai.llew;

import dai.llew.ui.GUI;

/**
 * Created by daiLlew on 17/01/2016.
 */
public class Game {

	public static void main(String[] args) throws Exception {
		GUI gui = new GUI();

		Thread gameThread = new Thread(() -> {
			while (true) {
				gui.getBoard().repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		});
		gameThread.start();
	}
}

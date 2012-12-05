package zuulproject;

import zuulproject.model.GameSystem;
import zuulproject.outercontroller.GameController;
import zuulproject.view.GameView;

/**
 * Launch the game from here
 * @author Alok
 *
 */
class GameMain {
	// main method, sets up the game, controller, and view
	public static void main(String[] args) {
		GameSystem g = new GameSystem();
		GameView v = new GameView(g);
		g.addGameListener(v);

		@SuppressWarnings("unused")
		GameController c = new GameController(v, g);

		v.setVisible(true);
		v.setLocationRelativeTo(null);
	}
}
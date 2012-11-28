package zuulproject.outercontroller;

import zuulproject.model.*;
import zuulproject.view.*;
import java.awt.Point;
import java.awt.event.*;

/**
 * The interpreter - this is where all the user's inputs are processed. Some events change the model (when user tells player to go)
 * and some change the view (when the user clicks to open a new window)
 */

public class GameController {
	
	protected GameSystem model;
	protected GameView view;
	
	public GameController(GameView v, GameSystem g) {
		model = g;
		view = v;
			
		view.addQuitGameListener(new QuitGameListener());
		view.addNewGameListener(new NewGameListener());
		view.addCommandListener(new CommandListener());
		view.addHelpGameListener(new HelpGameListener());
		view.addDrawingMouseListener(new DrawingMouseListener());
		view.addInventoryListener(new InventoryListener());
		view.addRoomItemListener(new RoomItemListener());
		view.addCommandListButtonListener(new CommandListButtonListener());
	}

	/*
	 * Checks to see if the user has entered a game command, then sends it off to the game to process
	 */
	class CommandListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String userInput = view.getUserInput();
			if (userInput.length()>0) {
				view.dspMessage(dspUserInput(userInput));
				model.processCmd(userInput);
				view.resetUserInput();
			} else {
				view.showError("You did not enter a command!");
			}
		}
		private String dspUserInput(String input) {
			return "\n** You typed '" + input + "'";
		}
	}
	
	/*
	 * Creates a new game and tells the view to enable its buttons
	 */
	class NewGameListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			view.enableCommandPanel();
			view.enableGameButtons();
			model.newGame();
			view.createGameFrames();
			view.createCommandListFrame();
			model.addGameListener(view.getInventoryView());
			model.addGameListener(view.getRoomItemView());
			model.addGameListener(view.getCommandListView());
		}
	}
	
	/*
	 * Closes down, and shuts down everything
	 */
	class QuitGameListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(1);
		}
	}
	
	/*
	 * Does the same thing as the help command when you type "help" into command box
	 */
	class HelpGameListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			view.dspMessage("\n** You clicked 'help'");
			model.processCmd("help");
		}
	}
	
	// Makes the frame that displays all items in the inventory visible
	class InventoryListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			view.getInventoryView().setVisible(true);
		}
	}

	// Makes the frame that displays all items in the room visible
	class RoomItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			view.getRoomItemView().setVisible(true);
		}
	}

	// Makes the frame that displays all commands in the game visible
	class CommandListButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			view.getCommandListView().setVisible(true);
		}
	}
	
	// A listener made to listen to what happens when a certain area in the 3D view is clicked
	class DrawingMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent eve) {
			String exitClicked = view.get3DPanel().pointInExit(new Point(eve.getX(), eve.getY())); //Checks to see if a shape(proper exit) is clicked
			if (exitClicked != null) model.processCmd("go " + exitClicked);
		}
		
		@Override
		public void mouseEntered(MouseEvent arg0) {	
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
	}
}

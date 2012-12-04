package zuulproject.view;

import zuulproject.event.*;
import zuulproject.event.GameActionFailEvent.FailedAction;
import zuulproject.event.GameOverEvent.GameResult;
import zuulproject.model.*;
import zuulproject.model.innercontroller.CommandTypes;
import zuulproject.model.innercontroller.CommandWords;
import zuulproject.model.innercontroller.battle.Combat;
import zuulproject.model.innercontroller.battle.innerbattlecontroller.BattleCommandTypes;
import zuulproject.model.item.*;
import zuulproject.model.itemholder.*;
import zuulproject.view.graphicalview.*;
import zuulproject.outercontroller.*;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.util.List;

/**
 * The entire display for the user to interact with. This class makes a nice
 * representation of the model, and listens to the model so it gets updated when
 * any changes occur
 */

public class GameView extends JFrame implements GameEventListener {

	private static final long serialVersionUID = 1L;

	private static final String commandFail = "Command Failed: ";
	private static final String gameLose = "You have lost the game :(";
	private static final String gameWin = "Congratulations! You have won!";
	private static final String gameEnd = "Game Over. Begin a new game: Game > New Game";	
	
	private static final String newline = "\n";

	// making the menu bar and its items
	private JMenuBar menuBar = new JMenuBar();

	private JMenu gameMenu = new JMenu("Game");
	private JMenuItem newGame = new JMenuItem("New Game");
	private JMenuItem saveAsGame = new JMenuItem("Save As");
	private JMenuItem saveGame = new JMenuItem("Save");
	private JMenuItem openGame = new JMenuItem("Open");
	private JMenuItem quitGame = new JMenuItem("Quit");

	private JMenu editMenu = new JMenu("Edit");
	private JMenuItem undoGame = new JMenuItem("Undo");
	private JMenuItem redoGame = new JMenuItem("Redo");

	private JMenu helpMenu = new JMenu("Help");
	private JMenuItem helpGame = new JMenuItem("Detailed Help");

	// making the command box where all the inputs are going to be processed
	private JTextField commandInput = new JTextField(25);

	private JButton commandButton = new JButton("Process Command");
	private JButton commandListButton = new JButton("Command List");

	// making the panels where all the components will be placed
	private JPanel mainPanel;
	private JPanel picturePanel;
	private JPanel commandPanel;

	private JTextArea messageDisplayer;
	private JScrollPane scrollPane;

	// the model
	private GameSystem game_model;

	// the 3d and 2d views (panels)
	private DrawingArea drawing2D;
	private Drawing3DArea drawing3D;

	// Buttons that opens the item frames
	private JButton playerInventory = new JButton("Inventory");
	private JButton roomInventory = new JButton("Room Contents");

	// the frames that are connected to the view. when the view clicks a button
	// to display the model's contents,
	// these frames open up
	private InventoryFrame inventoryView;
	private RoomItemFrame roomItemView;
	private CommandListFrame commandListView;

	// This is for the dialog boxes
	JTextField savePath = new JTextField();
	JTextField openPath = new JTextField();
	
	final JComponent[] saveInputs = new JComponent[] {
			new JLabel("Save Path"),
			savePath
	};
	
	final JComponent[] openInputs = new JComponent[] {
			new JLabel("Open Path"),
			openPath
	};
	
	public GameView(GameSystem g) {

		// instanciate the model, and create the 2D and 3D views
		game_model = g;
		drawing2D = new DrawingArea(game_model);
		drawing3D = new Drawing3DArea(game_model, new Dimension(320, 320));

		// putting together the menu bar
		gameMenu.add(newGame);
		gameMenu.add(saveGame);
		saveGame.setEnabled(false);
		gameMenu.add(saveAsGame);
		saveAsGame.setEnabled(false);
		gameMenu.add(openGame);
		gameMenu.add(quitGame);

		editMenu.add(undoGame);
		editMenu.add(redoGame);

		helpMenu.add(helpGame);

		menuBar.add(gameMenu);
		menuBar.add(editMenu);
		menuBar.add(helpMenu);

		// the main panel contains all the other panels: picture panel (where
		// the 2d, 3d, and buttons that open frames that show items are placed),
		// command panel (where all the commands happen)
		mainPanel = new JPanel();

		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		// setting up the panels
		picturePanel = new JPanel();
		commandPanel = new JPanel();

		messageDisplayer = new JTextArea(8, 30);
		messageDisplayer.setEditable(false);
		messageDisplayer.setBorder(BorderFactory.createEtchedBorder());
		scrollPane = new JScrollPane(messageDisplayer);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		commandPanel.add(commandListButton);
		commandListButton.setEnabled(false);
		commandPanel.add(new JLabel("Command:"));
		commandPanel.add(commandInput);
		commandInput.setEditable(false);
		commandPanel.add(commandButton);
		commandButton.setEnabled(false);

		// /////////////////////////////////////////////
		drawing2D.setBackground(Color.white);
		drawing2D.setBorder(BorderFactory.createEtchedBorder());

		// Properly putting items in the Picturepanel
		picturePanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;

		picturePanel.add(drawing3D, c);

		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 1;

		picturePanel.add(drawing2D, c);

		c.weightx = 0;
		c.weighty = 1;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;

		picturePanel.add(playerInventory, c);

		c.weightx = 0;
		c.weighty = 1;
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;

		picturePanel.add(roomInventory, c);

		// ///////////////////////////////////////////

		mainPanel.add(picturePanel);
		mainPanel.add(scrollPane);
		mainPanel.add(commandPanel);

		disableGameButtons();

		this.setLayout(new FlowLayout());
		this.setJMenuBar(menuBar);
		this.setContentPane(mainPanel);
		this.pack();
		this.setTitle("Zuul");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// this.setResizable(false);
		dspMessage("Game > New Game to begin your adventure!");
	}

	// sets the string in the command box
	public void setCommandInput(String s) {
		this.commandInput.setText(s);
	}

	// appends a string to the command box
	public void appendCommandInput(String s) {
		this.commandInput.setText(commandInput.getText() + s);
	}

	// returns the 3D view
	public Drawing3DArea get3DPanel() {
		return drawing3D;
	}

	// gets the user input
	public String getUserInput() {
		return commandInput.getText();
	}

	// enable all the items in the command box
	public void enableCommandPanel() {
		commandListButton.setEnabled(true);
		commandInput.setEditable(true);
		commandButton.setEnabled(true);
	}

	// enable all the buttons that need to be enabled in the menu, and other
	// game related buttons
	public void enableGameButtons() {
		saveGame.setEnabled(true);
		saveAsGame.setEnabled(true);
		undoGame.setEnabled(true);
		redoGame.setEnabled(true);
		helpGame.setEnabled(true);
		playerInventory.setEnabled(true);
		roomInventory.setEnabled(true);
	}

	// disable all buttons that require an instance of a game to function
	public void disableGameButtons() {
		saveGame.setEnabled(false);
		saveAsGame.setEnabled(false);
		undoGame.setEnabled(false);
		redoGame.setEnabled(false);
		helpGame.setEnabled(false);
		playerInventory.setEnabled(false);
		roomInventory.setEnabled(false);
	}

	// disable the command box (no more user inputs)
	public void disableCommandPanel() {
		commandListButton.setEnabled(false);
		commandInput.setEditable(false);
		commandButton.setEnabled(false);
	}

	// reset command box
	public void resetUserInput() {
		commandInput.setText("");
	}

	// an error pop up box
	public void showError(String errMessage) {
		JOptionPane.showMessageDialog(this, errMessage);
	}

	// The following functions are associating all the buttons/panels with the
	// appropriate listeners
	public void addCommandListener(ActionListener listener) {
		commandButton.addActionListener(listener);
		commandInput.addActionListener(listener);
	}

	public void addNewGameListener(ActionListener listener) {
		newGame.addActionListener(listener);
	}
	
	public void addQuitGameListener(ActionListener listener) {
		quitGame.addActionListener(listener);
	}
	
	public void addUndoGameListener(ActionListener listener) {
		undoGame.addActionListener(listener);
	}

	public void addRedoGameListener(ActionListener listener) {
		redoGame.addActionListener(listener);
	}
	
	public void addHelpGameListener(ActionListener listener) {
		helpGame.addActionListener(listener);
	}
	
	public void addSaveGameListener(ActionListener listener) {
		saveGame.addActionListener(listener);
	}

	public void addOpenGameListener(ActionListener listener) {
		openGame.addActionListener(listener);
	}
	
	public void addDrawingMouseListener(MouseListener listener) {
		drawing3D.addMouseListener(listener);
	}

	public void addInventoryListener(ActionListener listener) {
		playerInventory.addActionListener(listener);
	}

	public void addRoomItemListener(ActionListener listener) {
		roomInventory.addActionListener(listener);
	}

	public void addCommandListButtonListener(ActionListener listener) {
		commandListButton.addActionListener(listener);
	}

	// display a message to the user
	public void dspMessage(String message) {
		messageDisplayer.append(message + newline);
		messageDisplayer.setCaretPosition(messageDisplayer.getDocument()
				.getLength());
	}
	
	public void refreshTextBox() {
		messageDisplayer.setText("");
	}

	// disable all buttons
	public void gameEnded() {
		disableCommandPanel();
		disableGameButtons();
	}

	// return the frame with the command words
	public CommandListFrame getCommandListView() {
		return commandListView;
	}

	// create the frame with the command words (only after an instance of the
	// game is created)
	public void createCommandListFrame() {
		commandListView = new CommandListFrame(this, game_model);
	}

	// returns frame with inventory
	public InventoryFrame getInventoryView() {
		return inventoryView;
	}

	// returns frame with room items
	public RoomItemFrame getRoomItemView() {
		return roomItemView;
	}

	// creates frames associated with the buttons to show the inventory and room
	// items
	public void createGameFrames() {
		inventoryView = new InventoryFrame(this, game_model);
		roomItemView = new RoomItemFrame(this, game_model);
	}

	public void closeGameFrames() {
		inventoryView.dispose();
		roomItemView.dispose();
		commandListView.dispose();
	}

	public void refreshViews() {
		this.drawing2D.repaint();
		this.drawing3D.repaint();
	}

	@Override
	public void gameBegins(GameEvent e) {
		enableCommandPanel();
		enableGameButtons();

		createGameFrames();
		createCommandListFrame();
		((Game) e.getSource()).addGameListener(this);
		((Game) e.getSource()).addGameListener(getInventoryView());
		((Game) e.getSource()).addGameListener(getRoomItemView());
		((Game) e.getSource()).addGameListener(getCommandListView());
		refreshViews();
		refreshTextBox();
		dspMessage(((Game)e.getSource()).getGameDescription());
	}

	@Override
	public void gameCmdProcessed(GameChangeEvent e) {
		if (!(e.getSuccess())) {
			dspMessage(dspCommandFail(e.getCommand()));
		} else {
			if (e.getCommand() == CommandTypes.GO) {
				dspMessage(((Player) e.getSource()).getName() + " is "
						+ ((Player) e.getSource()).getRoom().getDescription());
			} else if (e.getCommand() == CommandTypes.PICKUP) {
				dspMessage("Item has been picked up");
			} else if (e.getCommand() == CommandTypes.CONSUME) {
				dspMessage("Potion has been consumed");
			} else if (e.getCommand() == CommandTypes.APPLY) {
				dspMessage("Powerup has been applied");
			} else if (e.getCommand() == CommandTypes.EQUIP) {
				dspMessage("Weapon has been equipped");
			} else if (e.getCommand() == CommandTypes.DEEQUIP) {
				dspMessage("Weapon has been deequipped");
			} else if (e.getCommand() == CommandTypes.UNDO) {
				dspMessage("Move is undone");
			} else if (e.getCommand() == CommandTypes.REDO) {
				dspMessage("Move is redone");
			}
		}
		refreshViews();
	}

	public String dspCommandFail(CommandTypes command) {
		String s;
		s = commandFail + command.toString() + newline + "Proper use of '"
				+ command.toString() + "' command: ";
		if (command == CommandTypes.GO)
			s += "'go [direction]";
		else if (command == CommandTypes.PICKUP)
			s += "'pickup [item name]'";
		else if (command == CommandTypes.CONSUME)
			s += "'pickup [potion name]'";
		else if (command == CommandTypes.APPLY)
			s += "'apply [powerup name]'";
		else if (command == CommandTypes.EQUIP)
			s += "'equip [weapon name]";
		else if (command == CommandTypes.DEEQUIP)
			s += "'deequip' (Must have weapon equipped)";
		else if (command == CommandTypes.UNDO)
			s += "'undo' (No more moves to undo)";
		else if (command == CommandTypes.REDO)
			s += "'redo' (No more moves to redo)";
		else if (command == CommandTypes.EXAMINE)
			s += "'examine [item name]'";
		return s;
	}

	@Override
	public void gameInfoRequested(GameInfoEvent e) {
		if (e.getCommand() == CommandTypes.LOOK) {
			dspMessage("Exits:" + newline + dspListString(((Room)e.getSource()).getExitList()));
			dspMessage("Room Content:" + newline + dspListString(((Room)e.getSource()).getItemListString()));
		} else if (e.getCommand() == CommandTypes.INVENTORY) {
			dspMessage("Inventory:" + newline + dspListString(((Player)e.getSource()).getItemListString()));
		} else if (e.getCommand() == CommandTypes.STATUS) {
			dspMessage(creatureStatus((Player)e.getSource()) + newline + "Attack Power: " + ((Player)e.getSource()).getAttackPower() + "+" + ((Player)e.getSource()).getBonusAttack());
		} else if (e.getCommand() == CommandTypes.EXAMINE) {
			GameInfoConditionalEvent event = (GameInfoConditionalEvent)e;
			if ((event.getConditionObject()) == null) {
				dspCommandFail(event.getCommand());
			} else {
				String temp = "";			
				if ((event.getConditionObject()) instanceof Weapon) temp+= "Type: Weapon";
				else if ((event.getConditionObject()) instanceof Consumable) temp+= "Type: Consumable";
				else if ((event.getConditionObject()) instanceof Powerup) temp+= "Type: Powerup";
				else temp+= "Type: Item";
				temp+= ", Description: " + ((Item)(event.getConditionObject())).getDescription();
				dspMessage(temp);
			}
		} else if (e.getCommand() == CommandTypes.HELP) {
			dspMessage("Command List:" + newline + dspListString(((CommandWords)e.getSource()).getCommandList()));
		} else if (e.getCommand() == CommandTypes.UNKNOWN) {
			dspMessage("Command Unknown - Check Command List");
		}
	}

	private String creatureStatus(Creature creature) {
		return creature.getName() + " HP: " + creature.getCurrentHP() + "/" + creature.getMaxHP();
	}
	
	private String dspListString(List<String> list) {
		if(list.isEmpty()) return "-None-";
		String temp = "";
		for (String s : list) {
			temp+= " " + s;
		}
		return temp;
	}
	
	@Override
	public void gameBattleCmdProcessed(GameBattleChangeEvent e) {
		if (e.getCommand() == BattleCommandTypes.FIGHT) {
			dspCreatureHP(((Combat)e.getSource()).getPlayer(), ((Combat)e.getSource()).getMonster());
		} else if (e.getCommand() == BattleCommandTypes.IDLE) {
			dspCreatureHP(((Combat)e.getSource()).getPlayer(), ((Combat)e.getSource()).getMonster());
		} else if (e.getCommand() == BattleCommandTypes.FLEE) {
			if (e.getSuccess()) {
				dspMessage("Flee was successful!");
			} else {
				dspMessage("You were blocked by the monster");
				dspCreatureHP(((Combat)e.getSource()).getPlayer(), ((Combat)e.getSource()).getMonster());
			}
		}
	}
	
	private void dspCreatureHP(Player p, Monster m) {
		dspMessage(creatureStatus(p));
		dspMessage(creatureStatus(m));
	}
	
	@Override
	public void gameBattleInfoRequested(GameBattleInfoEvent e) {
		if (e.getCommand() == BattleCommandTypes.STATUS) {
			dspMessage(creatureStatus(((Combat)e.getSource()).getPlayer()) + newline + "Attack Power: " + ((Combat)e.getSource()).getPlayer().getAttackPower() + "+" + ((Combat)e.getSource()).getPlayer().getBonusAttack());
			dspMessage(creatureStatus(((Combat)e.getSource()).getMonster()) + newline + "Attack Power: " + ((Combat)e.getSource()).getMonster().totalAttack());
		} else if (e.getCommand() == BattleCommandTypes.HELP) {
			dspMessage("Command List:" + newline + dspListString(((CommandWords)e.getSource()).getBattleCommandList()));
		} else if (e.getCommand() == BattleCommandTypes.UNKNOWN) {
			dspMessage("Command Unknown - Check Command List");
		}
	}

	@Override
	public void gameEnded(GameOverEvent e) {
		closeGameFrames();
		if (e.getGameResult() == GameResult.LOSE) dspMessage(gameLose);
		else if (e.getGameResult() == GameResult.WIN) dspMessage(gameWin);
		dspMessage(gameEnd);
		gameEnded();
	}

	@Override
	public void gameBattleEnded(GameEvent e) {
		saveGame.setEnabled(true);
		saveAsGame.setEnabled(true);
		this.drawing2D.repaint();
		this.drawing3D.repaint();
	}

	@Override
	public void gameBattleBegins(GameEvent e) {
		saveGame.setEnabled(false);
		saveAsGame.setEnabled(false);
		dspMessage(game_model.getGame().getPlayer().getName() + " has encountered " + game_model.getGame().getPlayer().getRoom().getMonster().getName());
	}
	
	@Override
	public void gameActionFailed(GameActionFailEvent e) {
		if (e.getFailedAction() == FailedAction.OPENFILE) showError("File Not Found");
		else if (e.getFailedAction() == FailedAction.PARSEFILE) showError("Save File has an error");
		else if (e.getFailedAction() == FailedAction.SAVEFILE) showError("Must 'Save As' before 'Save'");
		else if (e.getFailedAction() == FailedAction.SAVECOMBAT) showError("Cannot Save during a fight!\n(That's cheating)");
	}

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

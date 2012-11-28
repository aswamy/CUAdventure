package zuulproject.view;

import zuulproject.event.*;
import zuulproject.model.*;
import zuulproject.view.graphicalview.*;
import zuulproject.outercontroller.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * The entire display for the user to interact with. This class makes a nice representation of the model, and listens to the model
 * so it gets updated when any changes occur
 */

public class GameView extends JFrame implements GameListener {
	
	private static final long serialVersionUID = 1L;

	private static final String newline = "\n";
	
	// making the menu bar and its items
	private JMenuBar menuBar = new JMenuBar();

	private JMenu gameMenu = new JMenu("Game");
	private JMenuItem newGame = new JMenuItem("New Game");
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
	
	// the frames that are connected to the view. when the view clicks a button to display the model's contents, 
	// these frames open up
	private InventoryFrame inventoryView;
	private RoomItemFrame roomItemView;
	private CommandListFrame commandListView;
	
	public GameView(GameSystem g) {
		
		// instanciate the model, and create the 2D and 3D views
		game_model = g;
		drawing2D = new DrawingArea(game_model);
		drawing3D = new Drawing3DArea(game_model, new Dimension(320, 320));
		
		// putting together the menu bar
		gameMenu.add(newGame);
		gameMenu.add(saveGame);
		saveGame.setEnabled(false);
		gameMenu.add(openGame);
		gameMenu.add(quitGame);
		
		editMenu.add(undoGame);
		editMenu.add(redoGame);
		
		helpMenu.add(helpGame);
		
		menuBar.add(gameMenu);
		menuBar.add(editMenu);
		menuBar.add(helpMenu);
		
		
		// the main panel contains all the other panels: picture panel (where the 2d, 3d, and buttons that open frames that show items are placed), command panel (where all the commands happen)
		mainPanel = new JPanel();
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		// setting up the panels
		picturePanel = new JPanel();
		commandPanel = new JPanel();
		
		messageDisplayer = new JTextArea(8, 30);
		messageDisplayer.setEditable(false);
		messageDisplayer.setBorder(BorderFactory.createEtchedBorder());
		scrollPane = new JScrollPane(messageDisplayer);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		
		commandPanel.add(commandListButton);
		commandListButton.setEnabled(false);
		commandPanel.add(new JLabel("Command:"));
		commandPanel.add(commandInput);
		commandInput.setEditable(false);
		commandPanel.add(commandButton);
		commandButton.setEnabled(false);

		///////////////////////////////////////////////
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
		
		/////////////////////////////////////////////
		
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
		//this.setResizable(false);
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
	
	// enable all the buttons that need to be enabled in the menu, and other game related buttons
	public void enableGameButtons() {
		undoGame.setEnabled(true);
		redoGame.setEnabled(true);
		helpGame.setEnabled(true);
		playerInventory.setEnabled(true);
		roomInventory.setEnabled(true);
	}
	
	// disable all buttons that require an instance of a game to function
	public void disableGameButtons() {
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
	
	// The following functions are associating all the buttons/panels with the appropriate listeners
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
	
	public void addHelpGameListener(ActionListener listener) {
		helpGame.addActionListener(listener);
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
        messageDisplayer.setCaretPosition(messageDisplayer.getDocument().getLength());		
	}
	
	// update view when game changes	
	public void commandProcessed(GameEvent e) {
		this.drawing2D.repaint();
		this.drawing3D.repaint();
		dspMessage(e.getGameStatus());
	}
	
	// disable all buttons
	public void endGame() {
		disableCommandPanel();
		disableGameButtons();
	}

	// return the frame with the command words
	public CommandListFrame getCommandListView() {
		return commandListView;
	}
	
	// create the frame with the command words (only after an instance of the game is created)
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
	
	
	// creates frames associated with the buttons to show the inventory and room items
	public void createGameFrames() {
		inventoryView = new InventoryFrame(this, game_model);
		roomItemView = new RoomItemFrame(this, game_model);
	}
	
	// main method, sets up the game, controller, and view
	public static void main(String[] args) {
		GameSystem g = new GameSystem();
		GameView v= new GameView(g);
		g.addGameListener(v);
		@SuppressWarnings("unused")
		GameController c = new GameController(v, g);
		
		v.setVisible(true);
		v.setLocationRelativeTo(null);
	}
}

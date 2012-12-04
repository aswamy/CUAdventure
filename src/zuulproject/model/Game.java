package zuulproject.model;

import org.w3c.dom.*;

import javax.xml.parsers.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import zuulproject.event.*;
import zuulproject.event.GameOverEvent.GameResult;
import zuulproject.model.innercontroller.*;
import zuulproject.model.item.*;
import zuulproject.model.itemholder.*;
import zuulproject.model.innercontroller.battle.*;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.07.31
 */

/**
 * The game class has been modied from the original Zuul
 * 
 * The game processes the commands received from the parsers Currently, the game
 * handles the 'everyday' commands, and the battle commands
 * 
 * Author: Alok Swamy
 */

public class Game {

	private static final String DEFAULT_DESCRIPTION = "Welcome to CUAdventure. Become strong, survive challenges, and defeat the CU Dragon to win!";
	private static final String DEFAULT_GAMEPATH = String.format("%s\\%s",
			System.getProperty("user.dir"),
			"src\\zuulproject\\model\\saves\\level1.xml");

	private boolean gameOver;
	private Parser parser;
	private Player p1;
	private String gameDescription;
	private String gamePath;
	private List<GameChangeListener> listenerList;
	private List<Room> rooms;

	/**
	 * Create the game and initialize its internal map.
	 */
	public Game(String path) {
		parser = new Parser();
		p1 = new Player();
		gameOver = false;
		gameDescription = DEFAULT_DESCRIPTION;
		listenerList = new ArrayList<GameChangeListener>();
		rooms = new ArrayList<Room>();
		gamePath = path;
		// initializeGame();
		initializeLevel();
		System.out.println(this.toXML());
	}

	public Game() {
		this(DEFAULT_GAMEPATH);
	}

	public synchronized void addGameListenerList(List<GameChangeListener> g) {
		listenerList.addAll(g);
		p1.addGameListenerList(g);
	}

	public synchronized void addGameListener(GameChangeListener g) {
		listenerList.add(g);
		p1.addGameListener(g);
	}

	// removes people that don't want to listen to the game
	public synchronized void removeGameListener(GameChangeListener g) {
		listenerList.remove(g);
		p1.removeGameListener(g);
	}

	// announce the game change has occured to all that want to listen
	protected void announceGameChange(GameChangeEvent e) {
		for (GameChangeListener g : listenerList)
			g.gameCmdProcessed(e);
	}

	// announce that info is requested by the user
	protected void announceGameInfo(GameInfoEvent e) {
		for (GameChangeListener g : listenerList) {
			if (g instanceof GameEventListener)
				((GameEventListener) g).gameInfoRequested(e);
		}
	}

	protected void announceGameBattle(GameEvent e) {
		for (GameChangeListener g : listenerList) {
			if (g instanceof GameEventListener)
				((GameEventListener) g).gameBattleBegins(e);
		}
	}

	protected void annouceGameEnded(GameOverEvent e) {
		for (GameChangeListener g : listenerList)
			g.gameEnded(e);
	}

	/**
	 * Create all the rooms and link their exits together.
	 */
	public void initializeGame() {
		Room outside, theater, pub, lab, office;

		// create the rooms
		outside = new Room("outside the main entrance of the university",
				"Outside");
		theater = new Room("in a lecture theater", "Theater");
		theater.spawnMonster(new Monster("Vampire"));
		theater.getMonster().insertItem(new Weapon("SuperSword", 3));
		pub = new Room("in the campus pub", "Pub");
		lab = new Room("in a computing lab", "Lab");
		office = new Room("in the computing admin office", "Office");

		// initialise room exits

		outside.setExits(Exit.east, theater);
		outside.setExits(Exit.south, lab);
		outside.setExits(Exit.west, pub);
		outside.setExits(Exit.teleporter, office);
		theater.setExits(Exit.west, outside);
		pub.setExits(Exit.east, outside);
		lab.setExits(Exit.north, outside);
		lab.setExits(Exit.east, office);
		office.setExits(Exit.west, lab);

		// outside.insertItem(new Item("GoldenKey"));
		pub.insertItem(new Weapon("Sword", 2));
		lab.insertItem(new Consumable("SmallPotion", 100));
		theater.insertItem(new Powerup("mini_powerup",
				"Attack Boost: 2, Health Boost: 5", 2, 5));

		rooms.add(outside);
		rooms.add(theater);
		rooms.add(pub);
		rooms.add(lab);
		rooms.add(office);

		p1.setRoom(outside);
		gameOver = false;
	}

	public void initializeLevel() {

		File file = new File(gamePath);
		if (file.exists()) {

			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder d = factory.newDocumentBuilder();
				Document doc = d.parse(file);

				Element element = doc.getDocumentElement();

				NodeList listOfRooms = element.getElementsByTagName("room");

				if (listOfRooms != null && listOfRooms.getLength()>0) {
					for (int i = 0; i < listOfRooms.getLength(); i++) {
						Element e = (Element) listOfRooms.item(i);
						rooms.add(getRoom(e));
					}
				}

				if (listOfRooms != null && listOfRooms.getLength()>0) {
					for (int i = 0; i < listOfRooms.getLength(); i++) {
						Element e = (Element) listOfRooms.item(i);
						String roomName = e.getAttribute("rname");
						
						NodeList listOfExits = e.getElementsByTagName("exit");
						if (listOfExits != null && listOfExits.getLength() >0) {
							for (int j = 0; j < listOfExits.getLength(); j++) {
								Element exit = (Element) listOfExits.item(j);
								
								String exitDirection = exit.getAttribute("rdirection");
								String exitRoom = exit.getTextContent();
								getRoom(roomName).setExits(Exit.valueOf(exitDirection), getRoom(exitRoom));
							}
						}
					}
				}
				
				NodeList playerStart = element.getElementsByTagName("player");

				if (playerStart != null && playerStart.getLength() > 0) {
					Element e = (Element) playerStart.item(0);
					p1.setName(e.getAttribute("pname"));
					///////////////////// FIX THIS!!!
					for (Room r : rooms) {
						if (r.getRoomName().equals(
								getTextValue(e, "currentroom")))
							p1.setRoom(r);
					}

					NodeList listOfItems = e.getElementsByTagName("playeritem");

					if (listOfItems != null && listOfItems.getLength()>0) {
						for (int i = 0; i < listOfItems.getLength(); i++) {
							Element el = (Element) listOfItems.item(i);
							p1.insertItem(getItem(el));
						}

						Weapon equippedWeapon = p1.findWeapon(getTextValue(e,
								"equippeditem"));
						if (equippedWeapon != null)
							p1.setWeapon(equippedWeapon);
					}

				}

				// once all the rooms are uploaded, set up the exits

			} catch (Exception e) {
				System.out.println("moo2");
			}
		} else {

		}
	}

	private Room getRoom(Element element) {
		String rname = element.getAttribute("rname");
		String rdescription = getTextValue(element, "rdescription");

		Room room = new Room(rdescription, rname);

		NodeList listOfItems = element.getElementsByTagName("roomitem");

		if (listOfItems != null) {
			for (int i = 0; i < listOfItems.getLength(); i++) {
				Element e = (Element) listOfItems.item(i);
				room.insertItem(getItem(e));
			}
		}

		NodeList monsters = element.getElementsByTagName("monster");

		if (monsters != null && monsters.getLength() > 0) {
			Element e = (Element) monsters.item(0);
			room.spawnMonster(getMonster(e));
		}

		return room;
	}

	private Monster getMonster(Element element) {
		String mname = element.getAttribute("mname");
		int mattack = getIntValue(element, "attack");
		int mdamage = getIntValue(element, "damagerange");
		int maxHP = getIntValue(element, "maxHP");
		int currentHP = getIntValue(element, "currentHP");

		Monster m = new Monster(mname, mattack, mdamage, maxHP, currentHP);

		NodeList monsterItems = element.getElementsByTagName("monsteritem");
		if (monsterItems != null) {
			for (int i = 0; i < monsterItems.getLength(); i++) {
				Element e = (Element) monsterItems.item(i);
				m.insertItem(getItem(e));
			}
		}

		return m;
	}

	private Item getItem(Element element) {
		String name = getTextValue(element, "name");
		String description = getTextValue(element, "description");

		String type = element.getAttribute("type");
		Item item;

		if (type.equals("Weapon")) {
			int attack = getIntValue(element, "attack");
			item = new Weapon(name, description, attack);
		} else if (type.equals("Consumable")) {
			int health = getIntValue(element, "regenHP");
			item = new Weapon(name, description, health);
		} else if (type.equals("Powerup")) {
			int attack = getIntValue(element, "attack");
			int health = getIntValue(element, "health");
			item = new Powerup(name, description, attack, health);
		} else {
			item = new Item(name, description);
		}

		return item;
	}

	private String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if (nl != null && nl.getLength() > 0) {
			Element el = (Element) nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}
		return textVal;
	}

	private int getIntValue(Element ele, String tagName) {
		return (new Integer(getTextValue(ele, tagName)));
	}

	public String getGameDescription() {
		return gameDescription;
	}

	/**
	 * processes user inputs and converts it into a command that the game can
	 * read (battle command or regular command)
	 */
	public void playGame(String userInput) {
		if (!(p1.inBattle())) {
			Command command = parser.getUserCommand(userInput);
			processGameCmd(command);
			if (p1.inBattle()) {
				announceGameBattle(new GameEvent(p1.getRoom().getMonster()));
			}
		} else {
			Combat combat = new Combat(p1, p1.getRoom().getMonster(), parser);
			combat.addGameListenerList(listenerList);
			combat.fight(userInput);
		}
		if (p1.isDead()) {
			gameOver = true;
			annouceGameEnded(new GameOverEvent(GameResult.LOSE));
		}
	}

	/**
	 * Given a command, process (that is: execute) the command.
	 * 
	 * @param command
	 *            The command to be processed.
	 * @return true If the command ends the game, false otherwise.
	 */
	private void processGameCmd(Command command) {
		CommandTypes commandWord = command.getCommandWord();

		if (commandWord == CommandTypes.UNKNOWN) {
			announceGameInfo(new GameInfoEvent(this, CommandTypes.UNKNOWN));
		} else if (commandWord == CommandTypes.HELP) {
			announceGameInfo(new GameInfoEvent(this.getParser()
					.getCommandWords().getCommandList(), CommandTypes.HELP));
		} else if (commandWord == CommandTypes.QUIT) {
			gameOver = true;
			annouceGameEnded(new GameOverEvent(GameResult.QUIT));
		} else {
			p1.processPlayerCmd(command);
		}
	}

	/*
	 * returns whether a game is over
	 */
	public boolean isGameOver() {
		return gameOver;
	}

	/*
	 * returns the player
	 */
	public Player getPlayer() {
		return p1;
	}
	
	public Room getRoom(String rname) {
		Room room = null;
		for (Room r: rooms) {
			if(r.getRoomName().equals(rname)) room = r;
		}
		return room;
	}

	/*
	 * returns the parser
	 */
	public Parser getParser() {
		return parser;
	}

	public String toXML() {
		String temp = "";
		temp += "<game>\n";
		temp += p1.toXML();
		for (Room r : rooms) {
			temp += r.toXML();
		}
		temp += "</game>";
		return temp;
	}
}
